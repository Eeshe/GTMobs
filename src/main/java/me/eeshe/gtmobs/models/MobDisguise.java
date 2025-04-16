package me.eeshe.gtmobs.models;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.util.LogUtil;
import net.minecraft.server.v1_12_R1.EntityItem;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import net.minecraft.server.v1_12_R1.PlayerInteractManager;
import net.minecraft.server.v1_12_R1.WorldServer;

public class MobDisguise {
  private static final Map<String, Property> CACHED_SKINS = new HashMap<>();
  private static final List<String> SKIN_CACHE_QUEUE = new ArrayList<>();
  private static BukkitTask SKIN_CACHE_TASK;

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
    for (Entity entity : livingEntity.getNearbyEntities(47, 47, 47)) {
      if (!(entity instanceof Player)) {
        continue;
      }
      fakePlayer.spawn((Player) entity, livingEntity);
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
    fakePlayerName = individualizeName(fakePlayerName);

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

    Property skin = CACHED_SKINS.get(getRandomSkinName());
    if (skin != null) {
      gameProfile.getProperties().put("textures", skin);
    }
    return new FakePlayer(entityPlayer, splitMobName, equipment);
  }

  private String[] splitMobName(LivingEntity livingEntity) {
    String customName = livingEntity.getCustomName();
    if (customName == null) {
      return new String[] { "" };
    }
    List<String> segments = new ArrayList<>();
    StringBuilder currentSegment = new StringBuilder();
    char lastColorChar = '\0';
    char lastFormatChar = '\0';
    int maxSegments = 3;
    int maxLength = 16;

    for (int i = 0; i < customName.length(); i++) {
      char currentChar = customName.charAt(i);

      if (currentChar == '§' && i + 1 < customName.length()) {
        char nextChar = customName.charAt(i + 1);
        if (isColorChar(nextChar)) {
          lastColorChar = nextChar;
        } else if (isFormatChar(nextChar)) {
          lastFormatChar = nextChar;
        }
      }
      currentSegment.append(currentChar);
      if (currentSegment.length() != maxLength) {
        continue;
      }
      if (currentSegment.toString().endsWith("§")) {
        currentSegment.setLength(maxLength - 1);
        i += 1;
      }
      segments.add(currentSegment.toString());
      if (segments.size() == maxSegments) {
        break;
      }

      currentSegment.setLength(0);
      if (lastColorChar != '\0') {
        currentSegment.append('§').append(lastColorChar);
        lastColorChar = '\0';
      }
      if (lastFormatChar != '\0') {
        currentSegment.append('§').append(lastFormatChar);
        lastFormatChar = '\0';
      }
    }

    // Add any remaining characters in the current segment
    if (currentSegment.length() > 0) {
      segments.add(currentSegment.toString());
    }

    return segments.toArray(new String[0]);
  }

  private boolean isColorChar(char c) {
    return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'g');
  }

  private boolean isFormatChar(char c) {
    return c == 'l' || c == 'u' || c == 'o' || c == 'k' || c == 'm';
  }

  private String individualizeName(String name) {
    int colorCodeAmount = Math.floorDiv(16 - name.length(), 2);
    Random random = ThreadLocalRandom.current();
    for (int i = 0; i < colorCodeAmount; i++) {
      name = "§" + random.nextInt(10) + name;
    }
    return name;
  }

  // private String[] splitMobName(LivingEntity livingEntity) {
  // String customName = livingEntity.getCustomName();
  // if (customName == null) {
  // return new String[] { "" };
  // }
  // List<String> nameSegments = new ArrayList<>();
  // char lastCharacter = Character.MIN_VALUE;
  // boolean isBoldedText = false;
  // for (int index = 0; index < customName.length(); index += 16) {
  // if (!nameSegments.isEmpty() && nameSegments.get(nameSegments.size() -
  // 1).endsWith("§")) {
  // customName = customName.substring(0, index) + "§" +
  // customName.substring(index);
  // }
  // if (isBoldedText) {
  // customName = customName.substring(0, index) + "§l" +
  // customName.substring(index);
  // isBoldedText = false;
  // }
  // String segment = customName.substring(index, Math.min(customName.length(),
  // index + 16));
  // nameSegments.add(segment);
  //
  // for (int charIndex = segment.length() - 1; charIndex > 0; charIndex--) {
  // char segmentCharacter = segment.charAt(charIndex);
  // if (segmentCharacter != '§' || lastCharacter != 'l') {
  // // Character isn't color code, update last character
  // lastCharacter = segmentCharacter;
  // continue;
  // }
  // isBoldedText = true;
  // }
  // }
  // return nameSegments.toArray(new String[0]);
  // }

  /**
   * Applies the ItemDisguise for the passed LivingEntity for all online players
   *
   * @param livingEntity LivingEntity to disguise
   */
  private void applyItemDisguise(LivingEntity livingEntity) {
    ItemEntity itemEntity = createItemEntity(livingEntity);
    if (itemEntity == null) {
      return;
    }
    for (Entity entity : livingEntity.getNearbyEntities(47, 47, 47)) {
      if (!(entity instanceof Player)) {
        continue;
      }
      Player player = (Player) entity;
      itemEntity.spawn(player);
      applyItemDisguise(livingEntity, player);
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
    if (itemEntity == null) {
      return;
    }
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
    if (nmsLivingEntity == null) {
      return null;
    }

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

  public static void cacheSkin(String playerName) {
    if (SKIN_CACHE_QUEUE.contains(playerName) || CACHED_SKINS.containsKey(playerName)) {
      return;
    }
    SKIN_CACHE_QUEUE.add(playerName);
    startSkinCacheTask();
  }

  private static void startSkinCacheTask() {
    if (SKIN_CACHE_TASK != null) {
      return;
    }
    LogUtil.sendInfoLog("Starting skin cache task.");
    SKIN_CACHE_TASK = new BukkitRunnable() {

      @Override
      public void run() {
        if (SKIN_CACHE_QUEUE.isEmpty()) {
          cancel();
          SKIN_CACHE_TASK = null;
          LogUtil.sendInfoLog("Finished caching " + CACHED_SKINS.size() + " skins.");
          return;
        }
        String playerName = SKIN_CACHE_QUEUE.remove(0);
        fetchSkin(playerName).whenComplete((property, throwable) -> {
          if (throwable != null) {
            throwable.printStackTrace();
            return;
          }
          CACHED_SKINS.put(playerName, property);
        });
      }
    }.runTaskTimerAsynchronously(GTMobs.getInstance(), 0L, 100L);
  }

  /**
   * Fetches the textures and signature for the skin corresponding to the passed
   * name
   *
   * @param name Name of the skin to fetch
   * @return Fetch skin texture and signature
   */
  private static CompletableFuture<Property> fetchSkin(String name) {
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
      } catch (FileNotFoundException e) {
        LogUtil.sendWarnLog("Skin '" + name + "' not found.");
        return null;
      } catch (IOException e) {
        LogUtil.sendWarnLog("Couldn't fetch skin '" + name + "' because the server is being rate limited.");
        return null;
      }
    });
  }
}
