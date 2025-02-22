package me.eeshe.gtmobs.models;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.eeshe.gtmobs.GTMobs;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_12_R1.PlayerInteractManager;
import net.minecraft.server.v1_12_R1.WorldServer;

public class MobDisguise {

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
    livingEntity.setSilent(false);
    if (!skinNames.isEmpty() && skinItem == null) {
      applySkinDisguise(livingEntity);
    } else if (skinNames.isEmpty() && skinItem != null) {
      // TODO: Apply item disguise
    } else {
      if (ThreadLocalRandom.current().nextBoolean()) {
        applySkinDisguise(livingEntity);
      } else {
        // TODO: Apply item disguise
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
    livingEntity.setSilent(false);
    if (!skinNames.isEmpty() && skinItem == null) {
      applySkinDisguise(livingEntity, player);
    } else if (skinNames.isEmpty() && skinItem != null) {
      // TODO: Apply item disguise
    } else {
      if (ThreadLocalRandom.current().nextBoolean()) {
        applySkinDisguise(livingEntity);
      } else {
        // TODO: Apply item disguise
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
    EntityPlayer fakePlayer = createFakePlayer(livingEntity);
    for (Player player : livingEntity.getWorld().getPlayers()) {
      sendDisguisePackets(player, getNmsLivingEntity(livingEntity), fakePlayer);
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
    sendDisguisePackets(player, getNmsLivingEntity(livingEntity),
        createFakePlayer(livingEntity));
  }

  // TODO: UPDATE NPC SKIN WHEN THE fetchSkin COMPLETABLE FUTURE FINISHES

  /**
   * Creates the fake player used to disguise the passed living entity
   *
   * @param livingEntity Living entity to disguise
   * @return Created fake player
   */
  private EntityPlayer createFakePlayer(LivingEntity livingEntity) {
    WorldServer nmsWorld = ((CraftWorld) livingEntity.getWorld()).getHandle();
    EntityLiving nmsLivingEntity = getNmsLivingEntity(livingEntity);

    String disguiseName = getRandomSkinName();
    GameProfile gameProfile = new GameProfile(UUID.randomUUID(), disguiseName);
    EntityPlayer fakePlayer = new EntityPlayer(
        nmsWorld.getMinecraftServer(),
        nmsWorld,
        gameProfile,
        new PlayerInteractManager(nmsWorld));

    fakePlayer.h(nmsLivingEntity.getId());
    fakePlayer.setLocation(nmsLivingEntity.locX, nmsLivingEntity.locY,
        nmsLivingEntity.locZ, nmsLivingEntity.yaw, nmsLivingEntity.pitch);

    fetchSkin(disguiseName).whenComplete((skin, throwable) -> {
      gameProfile.getProperties().put("textures", new Property("textures", skin[0], skin[1]));
    });
    return fakePlayer;
  }

  /**
   * Fetches the textures and signature for the skin corresponding to the passed
   * name
   *
   * @param name Name of the skin to fetch
   * @return Fetch skin texture and signature
   */
  private CompletableFuture<String[]> fetchSkin(String name) {
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
        return new String[] { texture, signature };
      } catch (Exception e) {
        e.printStackTrace();
        return new String[] { "", "" };
      }
    });
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

  private static void sendDisguisePackets(Player player, EntityLiving nmsLivingEntity,
      EntityPlayer fakePlayer) {

    EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();

    // Remove GTMob living entity
    nmsPlayer.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(nmsLivingEntity.getId()));

    // Add fake player to TAB list
    nmsPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, fakePlayer));

    // Spawn fake player
    nmsPlayer.playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(fakePlayer));

    // Remove fake player from TAB list
    nmsPlayer.playerConnection
        .sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, fakePlayer));

    // Add delay to give time for the skin to add
    Bukkit.getScheduler().runTaskLater(GTMobs.getInstance(), () -> {
      // Remove GTMob living entity
      nmsPlayer.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(nmsLivingEntity.getId()));

      // Add fake player to TAB list
      nmsPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, fakePlayer));

      // Spawn fake player
      nmsPlayer.playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(fakePlayer));

      Bukkit.getScheduler().runTaskLater(GTMobs.getInstance(),
          () -> nmsPlayer.playerConnection
              .sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, fakePlayer)),
          1L);

    }, 100L);
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
