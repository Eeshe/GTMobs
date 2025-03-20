package me.eeshe.gtmobs.models;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.eeshe.gtmobs.util.LogUtil;
import net.minecraft.server.v1_12_R1.EntityItem;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import net.minecraft.server.v1_12_R1.PlayerInteractManager;
import net.minecraft.server.v1_12_R1.WorldServer;

public class MobDisguise {
  private static final Map<String, Property> CACHED_SKINS = new HashMap<>();

  private final boolean enabled;
  private final List<String> skinNames;
  private final ItemStack skinItem;

  public MobDisguise(boolean enabled, List<String> skinNames, ItemStack skinItem) {
    this.enabled = enabled;
    this.skinNames = skinNames;
    this.skinItem = skinItem;
  }

  /**
   * Applies the MobDisguise to the passed ActiveMob for all online players
   *
   * @param activeMob ActiveMob to apply the disguise to
   */
  public void apply(ActiveMob activeMob) {
    if (!isEnabled()) {
      return;
    }
    LivingEntity livingEntity = activeMob.getLivingEntity();
    livingEntity.setSilent(true);
    if (!skinNames.isEmpty() && skinItem == null) {
      applySkinDisguise(livingEntity);
    } else if (skinNames.isEmpty() && skinItem != null) {
      applyItemDisguise(livingEntity);
    } else {
      if (ThreadLocalRandom.current().nextBoolean()) {
        applySkinDisguise(livingEntity);
      } else {
        applyItemDisguise(livingEntity);
      }
    }
  }

  /**
   * Applies the MobDisguise to the passed ActiveMob for the passed player
   *
   * @param activeMob ActiveMob to apply the disguise to
   * @param player    Player to disguise the mob for
   */
  public void apply(ActiveMob activeMob, Player player) {
    if (!isEnabled()) {
      return;
    }
    LivingEntity livingEntity = activeMob.getLivingEntity();
    livingEntity.setSilent(true);
    if (!skinNames.isEmpty() && skinItem == null) {
      applySkinDisguise(livingEntity, player);
    } else if (skinNames.isEmpty() && skinItem != null) {
      applyItemDisguise(livingEntity, player);
    } else {
      if (ThreadLocalRandom.current().nextBoolean()) {
        applySkinDisguise(livingEntity, player);
      } else {
        applyItemDisguise(livingEntity, player);
      }
    }
  }

  /**
   * Applies the GTMob's skin disguise to the passed living entity for all
   * online players
   *
   * @param livingEntity Living entity to disguise
   */
  private void applySkinDisguise(LivingEntity livingEntity) {
    FakePlayer fakePlayer = createFakePlayer(livingEntity);
    if (fakePlayer == null) {
      return;
    }
    for (Player player : livingEntity.getWorld().getPlayers()) {
      fakePlayer.spawn(player, livingEntity);
    }
  }

  /**
   * Applies the GTMob's skin disguise to the passed living entity for the
   * passed player
   *
   * @param livingEntity LivingEntity to disguise
   * @param player       Player the entity is being disguised for
   */
  private void applySkinDisguise(LivingEntity livingEntity, Player player) {
    FakePlayer fakePlayer = createFakePlayer(livingEntity);
    if (fakePlayer == null) {
      return;
    }
    fakePlayer.spawn(player, livingEntity);
  }

  /**
   * Creates the fake player used to disguise the passed living entity
   *
   * @param livingEntity Living entity to disguise
   * @return Created fake player
   */
  private FakePlayer createFakePlayer(LivingEntity livingEntity) {
    WorldServer nmsWorld = ((CraftWorld) livingEntity.getWorld()).getHandle();
    EntityLiving nmsLivingEntity = getNmsLivingEntity(livingEntity);
    if (nmsLivingEntity == null) {
      LogUtil.sendWarnLog("NULL NMS LIVING ENTITY");
      return null;
    }

    String[] splitMobName = splitMobName(livingEntity);
    String fakePlayerName;
    if (splitMobName.length == 1) {
      fakePlayerName = splitMobName[0];
    } else if (splitMobName.length > 1) {
      fakePlayerName = splitMobName[1];
    } else {
      fakePlayerName = "";
    }
    GameProfile gameProfile = new GameProfile(UUID.randomUUID(), fakePlayerName);
    EntityPlayer entityPlayer = new EntityPlayer(
        nmsWorld.getMinecraftServer(),
        nmsWorld,
        gameProfile,
        new PlayerInteractManager(nmsWorld));

    // Set the ID to the same as the LivingEntity
    entityPlayer.h(nmsLivingEntity.getId());

    entityPlayer.setLocation(nmsLivingEntity.locX, nmsLivingEntity.locY,
        nmsLivingEntity.locZ, nmsLivingEntity.yaw, nmsLivingEntity.pitch);

    // Set FakePlayer equipment
    EntityEquipment entityEquipment = livingEntity.getEquipment();
    Map<EnumItemSlot, net.minecraft.server.v1_12_R1.ItemStack> equipment = new HashMap<>();
    if (entityEquipment != null) {
      equipment.put(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(entityEquipment.getHelmet()));
      equipment.put(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(entityEquipment.getChestplate()));
      equipment.put(EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(entityEquipment.getLeggings()));
      equipment.put(EnumItemSlot.FEET, CraftItemStack.asNMSCopy(entityEquipment.getBoots()));
      equipment.put(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(entityEquipment.getItemInMainHand()));
      equipment.put(EnumItemSlot.OFFHAND, CraftItemStack.asNMSCopy(entityEquipment.getItemInOffHand()));
    }

    CompletableFuture<Property> skinFuture = fetchSkin(getRandomSkinName())
        .whenComplete((skinProperty, throwable) -> {
          gameProfile.getProperties().put("textures", skinProperty);
        });
    return new FakePlayer(entityPlayer, splitMobName, equipment, skinFuture);
  }

  private String[] splitMobName(LivingEntity livingEntity) {
    String customName = livingEntity.getCustomName();
    if (customName == null) {
      return new String[] { "" };
    }
    List<String> nameSegments = new ArrayList<>();
    char lastCharacter = Character.MIN_VALUE;
    boolean isBoldedText = false;
    for (int index = 0; index < customName.length(); index += 16) {
      if (!nameSegments.isEmpty() && nameSegments.get(nameSegments.size() - 1).endsWith("§")) {
        customName = customName.substring(0, index) + "§" + customName.substring(index);
      }
      if (isBoldedText) {
        customName = customName.substring(0, index) + "§l" +
            customName.substring(index);
        isBoldedText = false;
      }
      String segment = customName.substring(index, Math.min(customName.length(), index + 16));
      nameSegments.add(segment);

      for (int charIndex = segment.length() - 1; charIndex > 0; charIndex--) {
        char segmentCharacter = segment.charAt(charIndex);
        if (segmentCharacter != '§' || lastCharacter != 'l') {
          // Character isn't color code, update last character
          lastCharacter = segmentCharacter;
          continue;
        }
        isBoldedText = true;
      }
    }
    return nameSegments.toArray(new String[0]);
  }

  /**
   * Fetches the textures and signature for the skin corresponding to the passed
   * name
   *
   * @param name Name of the skin to fetch
   * @return Fetch skin texture and signature
   */
  private CompletableFuture<Property> fetchSkin(String name) {
    if (CACHED_SKINS.containsKey(name)) {
      return CompletableFuture.completedFuture(CACHED_SKINS.get(name));
    }
    return CompletableFuture.supplyAsync(() -> {
      try {
        URL uuidUrl = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
        InputStreamReader uuidReader = new InputStreamReader(uuidUrl.openStream());
        String uuid = new JsonParser().parse(uuidReader).getAsJsonObject().get("id").getAsString();

        URL skinUrl = new URL("https://sessionserver.mojang.com/session/minecraft/profile/"
            + uuid + "?unsigned=false");
        InputStreamReader skinReader = new InputStreamReader(skinUrl.openStream());
        JsonObject property = new JsonParser().parse(skinReader).getAsJsonObject()
            .getAsJsonArray("properties").get(0).getAsJsonObject();
        String texture = property.get("value").getAsString();
        String signature = property.get("signature").getAsString();

        Property skinProperty = new Property("textures", texture, signature);
        CACHED_SKINS.put(name, skinProperty);

        return skinProperty;
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    });
  }

  /**
   * Applies the ItemDisguise for the passed LivingEntity for all online players
   *
   * @param livingEntity LivingEntity to disguise
   */
  private void applyItemDisguise(LivingEntity livingEntity) {
    ItemEntity itemEntity = createItemEntity(livingEntity);
    for (Player online : Bukkit.getOnlinePlayers()) {
      itemEntity.spawn(online);
      applyItemDisguise(livingEntity, online);
    }
  }

  /**
   * Applies the ItemDisguse for the passed LivingEntity to the passed player
   *
   * @param livingEntity LivingEntity to disguise
   * @param player       Player to disguise the entity for
   */
  private void applyItemDisguise(LivingEntity livingEntity, Player player) {
    ItemEntity itemEntity = createItemEntity(livingEntity);
    itemEntity.spawn(player);
  }

  /**
   * Creates and returns an ItemEntity to replace the passed LivingEntity
   *
   * @param livingEntity LivingEntity to Replace
   * @return ItemEntity to replace the passed LivingEntity
   */
  private ItemEntity createItemEntity(LivingEntity livingEntity) {
    net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(skinItem);
    EntityLiving nmsLivingEntity = getNmsLivingEntity(livingEntity);

    EntityItem entityItem = new EntityItem(
        nmsLivingEntity.getWorld(),
        nmsLivingEntity.locX,
        nmsLivingEntity.locY,
        nmsLivingEntity.locZ,
        nmsItemStack);
    entityItem.h(nmsLivingEntity.getId());
    if (livingEntity.getCustomName() != null) {
      entityItem.setCustomName(livingEntity.getCustomName());
    }
    entityItem.setCustomNameVisible(livingEntity.isCustomNameVisible());

    return new ItemEntity(entityItem);
  }

  /**
   * Gets the NMS LivingEntity from the passed spigot LivingEntity
   *
   * @param livingEntity LivingEntity to get the NMS LivingEntity of
   * @return NMS LivingEntity of the passed spigot LivingEntity
   */
  private EntityLiving getNmsLivingEntity(LivingEntity livingEntity) {
    return (EntityLiving) (((CraftWorld) livingEntity.getWorld()).getHandle())
        .getEntity(livingEntity.getUniqueId());
  }

  public boolean isEnabled() {
    return enabled;
  }

  public List<String> getSkinNames() {
    return skinNames;
  }

  public String getRandomSkinName() {
    if (skinNames.isEmpty()) {
      return null;
    }
    return skinNames.get(ThreadLocalRandom.current().nextInt(skinNames.size()));
  }

  public ItemStack getSkinItem() {
    return skinItem;
  }
}
