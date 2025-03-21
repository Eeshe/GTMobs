package me.eeshe.gtmobs.files.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.common.collect.ArrayListMultimap;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.GTMob;
import me.eeshe.gtmobs.models.MobDisguise;
import me.eeshe.gtmobs.models.config.ConfigKnockback;
import me.eeshe.gtmobs.models.config.ConfigParticle;
import me.eeshe.gtmobs.models.config.ConfigSound;
import me.eeshe.gtmobs.models.config.IntRange;
import me.eeshe.gtmobs.models.mobactions.ConsoleCommandMobAction;
import me.eeshe.gtmobs.models.mobactions.EffectMobAction;
import me.eeshe.gtmobs.models.mobactions.MessageMobAction;
import me.eeshe.gtmobs.models.mobactions.MobAction;
import me.eeshe.gtmobs.models.mobactions.MobActionChain;
import me.eeshe.gtmobs.models.mobactions.MobActionTarget;
import me.eeshe.gtmobs.models.mobactions.MobActionType;
import me.eeshe.gtmobs.models.mobactions.ParticleMobAction;
import me.eeshe.gtmobs.models.mobactions.SoundMobAction;
import me.eeshe.gtmobs.models.mobactions.SpawnMobAction;
import me.eeshe.gtmobs.models.mobactions.SuicideMobAction;
import me.eeshe.gtmobs.util.ConfigUtil;
import me.eeshe.gtmobs.util.ItemUtil;
import me.eeshe.gtmobs.util.LogUtil;

public class MobConfig extends ConfigWrapper {

  public MobConfig(GTMobs plugin) {
    super(plugin, null, "mobs.yml");
  }

  @Override
  public void writeDefaults() {
    writeDefaultGTMobs();

    getConfig().options().copyDefaults(true);
    saveConfig();
    reloadConfig();
  }

  /**
   * Writes the default GTMobs to the config file.
   */
  private void writeDefaultGTMobs() {
    for (GTMob gtMob : List.of(
        new GTMob(
            "zombie1",
            EntityType.ZOMBIE,
            false,
            true,
            false,
            "&eGTZombie",
            new MobDisguise(
                true,
                List.of(
                    "Skin1",
                    "Skin2"),
                ItemUtil.generateItemStack(
                    Material.FEATHER,
                    (short) 0,
                    (byte) 0,
                    "drone_model",
                    new ArrayList<>(),
                    true,
                    new HashMap<>(),
                    ArrayListMultimap.create(),
                    null,
                    null)),
            new HashMap<>(),
            new ConfigKnockback(0.5, true),
            Map.of(Attribute.GENERIC_MOVEMENT_SPEED, 1D),
            List.of(new ConfigSound(Sound.ENTITY_EVOCATION_ILLAGER_CAST_SPELL, true, 1.0F, 0.5F)),
            List.of(new ConfigParticle(Particle.VILLAGER_HAPPY, 5, 0.5, 0.5, 0.5, 0.1)),
            List.of(
                new ConfigParticle(Particle.SLIME, 10, 1, 1, 1, 0.5)),
            List.of(
                new ConfigParticle(Particle.SMOKE_NORMAL, 10, 1, 1, 1, 0.5),
                new ConfigParticle(Particle.EXPLOSION_NORMAL, 1, 1, 1, 1, 0.5)),
            new IntRange(1, 5),
            List.of(
                new MobActionChain(List.of(
                    new ConsoleCommandMobAction(List.of(
                        "broadcast &5zombie1 was hit",
                        "broadcast &5Attacker: %player_name%"), 0L)),
                    1),
                new MobActionChain(List.of(
                    new EffectMobAction(MobActionTarget.ATTACKER, new PotionEffect(
                        PotionEffectType.CONFUSION,
                        50,
                        0,
                        false,
                        true)),
                    new ConsoleCommandMobAction(List.of(
                        "broadcast &eApplying nausea to %player_name%"), 0)),
                    0.5)),
            List.of(new MobActionChain(List.of(
                new EffectMobAction(MobActionTarget.ATTACKER, new PotionEffect(
                    PotionEffectType.CONFUSION,
                    50,
                    0,
                    false,
                    true))),
                0.5)),
            List.of(new MobActionChain(List.of(
                new SpawnMobAction("skeleton1", 2, 5, 100),
                new ConsoleCommandMobAction(List.of(
                    "broadcast &cReinforcements arriving in 5 seconds..."), 0)),
                0.5))),
        new GTMob(
            "skeleton1",
            EntityType.SKELETON,
            false,
            true,
            true,
            "&3GTSkeleton",
            new MobDisguise(
                true,
                List.of(
                    "Skin1",
                    "Skin2"),
                ItemUtil.generateItemStack(
                    Material.FEATHER,
                    (short) 0,
                    (byte) 0,
                    "drone_model",
                    new ArrayList<>(),
                    true,
                    new HashMap<>(),
                    ArrayListMultimap.create(),
                    null,
                    null)),
            new HashMap<>(),
            null,
            Map.of(Attribute.GENERIC_MAX_HEALTH, 200D),
            List.of(new ConfigSound(Sound.ENTITY_EVOCATION_ILLAGER_CAST_SPELL, true, 1.0F, 0.5F)),
            List.of(new ConfigParticle(Particle.VILLAGER_HAPPY, 5, 0.5, 0.5, 0.5, 0.1)),
            List.of(
                new ConfigParticle(Particle.SLIME, 10, 1, 1, 1, 0.5),
                new ConfigParticle(Particle.FLAME, 10, 1, 1, 1, 0.5)),
            List.of(
                new ConfigParticle(Particle.SMOKE_NORMAL, 10, 1, 1, 1, 0.5),
                new ConfigParticle(Particle.EXPLOSION_HUGE, 10, 1, 1, 1, 0.5)),
            new IntRange(10, 50),
            List.of(new MobActionChain(List.of(
                new EffectMobAction(MobActionTarget.ATTACKER, new PotionEffect(
                    PotionEffectType.CONFUSION,
                    50,
                    0,
                    false,
                    true))),
                0.5)),
            List.of(new MobActionChain(List.of(
                new EffectMobAction(MobActionTarget.ATTACKER, new PotionEffect(
                    PotionEffectType.CONFUSION,
                    50,
                    0,
                    false,
                    true))),
                0.5)),
            List.of(new MobActionChain(List.of(
                new EffectMobAction(MobActionTarget.ATTACKER, new PotionEffect(
                    PotionEffectType.CONFUSION,
                    50,
                    0,
                    false,
                    true))),
                0.5))))) {
      writeDefaultGTMob(gtMob);
    }
  }

  /**
   * Writes the passed GTMob's to the config file.
   *
   * @param gtMob GTMob to write.
   */
  private void writeDefaultGTMob(GTMob gtMob) {
    FileConfiguration config = getConfig();
    String path = gtMob.getId();
    if (config.contains(path)) {
      return;
    }
    config.addDefault(path + ".entity-type", gtMob.getEntityType().name());
    config.addDefault(path + ".baby", gtMob.isBaby());
    config.addDefault(path + ".aggressive", gtMob.isAggressive());
    config.addDefault(path + ".disable-vanilla-attack", gtMob.hasDisabledVanillaAttack());
    config.addDefault(path + ".display-name", gtMob.getDisplayName());
    writeMobDisguise(path + ".disguise", gtMob.getDisguise());
    writeConfigKnockback(path + ".melee-knockback", gtMob.getMeleeKnockback());
    ConfigUtil.writeAttributeMap(config, path + ".attributes", gtMob.getAttributes());
    writeConfigSounds(path + ".spawn-sounds", gtMob.getSpawnSounds());
    ConfigUtil.writeConfigParticles(config, path + ".spawn-particles", gtMob.getSpawnParticles());
    ConfigUtil.writeConfigParticles(config, path + ".hit-particles", gtMob.getOnHitParticles());
    ConfigUtil.writeConfigParticles(config, path + ".death-particles", gtMob.getOnDeathParticles());
    ConfigUtil.writeIntRange(config, path + ".experience-drop", gtMob.getExperienceDrop());
    writeDefaultMobActions(path + ".events.hit", gtMob.getOnHitActions());
    writeDefaultMobActions(path + ".events.target-hit", gtMob.getOnTargetHitActions());
    writeDefaultMobActions(path + ".events.death", gtMob.getOnDeathActions());
  }

  /**
   * Writes the passed MobDisguise to the config file
   *
   * @param path        Path to write the disguise in
   * @param mobDisguise Disguise to write
   */
  private void writeMobDisguise(String path, MobDisguise mobDisguise) {
    FileConfiguration config = getConfig();
    if (config.contains(path)) {
      return;
    }
    config.addDefault(path + ".enabled", mobDisguise.isEnabled());
    config.addDefault(path + ".skins", mobDisguise.getSkinNames());
    ConfigUtil.writeConfigItemStack(config, path + ".item", mobDisguise.getSkinItem());
  }

  /**
   * Writes the passed ConfigKnockback to the passed path
   *
   * @param path            Path to write in
   * @param configKnockback ConfigKnockback to write
   */
  private void writeConfigKnockback(String path, ConfigKnockback configKnockback) {
    if (configKnockback == null) {
      return;
    }
    FileConfiguration config = getConfig();

    config.addDefault(path + ".strength", configKnockback.getStrength());
    config.addDefault(path + ".airborne", configKnockback.isAirborne());
  }

  /**
   * Writes the passed ConfigSounds to the passed config file
   *
   * @param config       Config file to write
   * @param path         Path to write in
   * @param configSounds ConfigSounds to write
   */
  private void writeConfigSounds(String path, List<ConfigSound> configSounds) {
    getConfig().addDefault(path, String.join(",", configSounds.stream().map(ConfigSound::toString)
        .collect(Collectors.toList())));
  }

  /**
   * Writes the passed List of MobActionChains to the passed path
   *
   * @param path            Path to write in
   * @param mobActionChains MobActionChains to write
   */
  private void writeDefaultMobActions(String path, List<MobActionChain> mobActionChains) {
    getConfig().addDefault(path, String.join(",", mobActionChains.stream()
        .map(MobActionChain::toString).collect(Collectors.toList())));
  }

  /**
   * Fetches and returns the GTMobs configured in the config file.
   *
   * @return Configured GTMobs.
   */
  public List<GTMob> fetchGTMobs() {
    List<GTMob> gtMobs = new ArrayList<>();
    for (String mobId : getConfig().getKeys(false)) {
      GTMob gtMob = fetchGTMob(mobId);
      if (gtMob == null) {
        continue;
      }
      gtMobs.add(gtMob);
    }
    return gtMobs;
  }

  public GTMob fetchGTMob(String id) {
    FileConfiguration config = getConfig();
    ConfigurationSection mobSection = config.getConfigurationSection(id);
    if (mobSection == null) {
      return null;
    }

    String entityTypeName = mobSection.getString("entity-type", "");
    EntityType entityType;
    try {
      entityType = EntityType.valueOf(entityTypeName);
    } catch (Exception e) {
      LogUtil.sendWarnLog("Unknown Entity Type '" + entityTypeName + "' configured for mob '" + id + "'.");
      return null;
    }
    boolean isBaby = mobSection.getBoolean("baby");
    boolean isAggressive = mobSection.getBoolean("aggressive");
    boolean disabledVanillaAttack = mobSection.getBoolean("disable-vanilla-attack");
    String displayName = mobSection.getString("display-name", "");
    MobDisguise disguise = fetchMobDisguise(id + ".disguise");
    Map<EquipmentSlot, ItemStack> equipment = fetchEquipment(id);
    ConfigKnockback meleeKnockback = fetchConfigKnockback(id + ".melee-knockback");
    Map<Attribute, Double> attributes = ConfigUtil.fetchAttributeMap(config, id + ".attributes");
    List<ConfigSound> spawnSounds = computeConfigSounds(mobSection.getString("spawn-sounds"));
    List<ConfigParticle> spawnParticles = ConfigUtil
        .computeConfigParticles(mobSection.getString("spawn-particles"));
    List<ConfigParticle> onHitParticles = ConfigUtil.computeConfigParticles(
        mobSection.getString("hit-particles"));
    List<ConfigParticle> onDeathParticles = ConfigUtil.computeConfigParticles(
        mobSection.getString("death-particles"));
    IntRange experienceDrop = ConfigUtil.fetchIntRange(config, id + ".experience-drop");
    List<MobActionChain> onHitActions = computeMobActionChains(mobSection.getString("events.hit"));
    List<MobActionChain> onTargetHitActions = computeMobActionChains(mobSection.getString("events.target-hit"));
    List<MobActionChain> onDeathActions = computeMobActionChains(mobSection.getString("events.death"));

    return new GTMob(id, entityType, isBaby, isAggressive, disabledVanillaAttack,
        displayName, disguise, equipment, meleeKnockback, attributes, spawnSounds,
        spawnParticles, onHitParticles, onDeathParticles, experienceDrop,
        onHitActions, onTargetHitActions, onDeathActions);
  }

  /**
   * Fetches the MobDisguise configured in the passed path
   *
   * @param path Path to search in
   * @return Configured MobDisguise
   */
  private MobDisguise fetchMobDisguise(String path) {
    ConfigurationSection disguiseSection = getConfig().getConfigurationSection(path);
    if (disguiseSection == null) {
      return null;
    }
    boolean enabled = disguiseSection.getBoolean("enabled");
    List<String> skinNames = disguiseSection.getStringList("skins");
    ItemStack skinItem = ConfigUtil.fetchConfigItemStack(getConfig(), path + ".item");

    return new MobDisguise(enabled, skinNames, skinItem);
  }

  /**
   * Fetches the configured equipment for the passed mob
   *
   * @param mobId ID of the mob whose equipment will be fetched
   * @return Configured equipment for the GTMob
   */
  private Map<EquipmentSlot, ItemStack> fetchEquipment(String mobId) {
    Map<EquipmentSlot, ItemStack> equipment = new HashMap<>();
    ConfigurationSection equipmentSection = getConfig().getConfigurationSection(mobId + ".equipment");
    if (equipmentSection == null) {
      return equipment;
    }
    for (String equipmentSlotName : equipmentSection.getKeys(false)) {
      EquipmentSlot equipmentSlot;
      try {
        equipmentSlot = EquipmentSlot.valueOf(equipmentSlotName);
      } catch (Exception e) {
        LogUtil.sendWarnLog("Unknown EquipmentSlot '" + equipmentSlotName + "' configured for '" + mobId + "'.");
        continue;
      }
      ItemStack item = fetchItemStack(equipmentSection.getString(equipmentSlotName));
      if (item == null) {
        continue;
      }
      equipment.put(equipmentSlot, item);
    }
    return equipment;
  }

  /**
   * Fetches the configured ConfigKnockback in the passed path
   *
   * @param path Path to search in
   * @return Configured ConfigKnockback
   */
  private ConfigKnockback fetchConfigKnockback(String path) {
    ConfigurationSection knockbackSection = getConfig().getConfigurationSection(path);
    if (knockbackSection == null) {
      return null;
    }
    double strength = knockbackSection.getDouble("strength");
    boolean airborne = knockbackSection.getBoolean("airborne");

    return new ConfigKnockback(strength, airborne);
  }

  /**
   * Fetches the ItemStack corresponding to the passed ID.
   * If the ID is a vanilla item, it returns the vanilla item. Otherwise, it
   * fetches a custom item from the items.yml
   *
   * @param itemId Item to fetch
   * @return Fetched item
   */
  private ItemStack fetchItemStack(String itemId) {
    Material material = Material.matchMaterial(itemId);
    if (material != null) {
      return new ItemStack(material);
    }
    return getPlugin().getItemConfig().fetchItem(itemId);
  }

  /**
   * Computes a List of ConfigSounds from the passed String
   *
   * @param configSoundChainString String to compute
   * @return Computed ConfigSounds
   */
  private List<ConfigSound> computeConfigSounds(String configSoundChainString) {
    List<ConfigSound> configSounds = new ArrayList<>();
    if (configSoundChainString == null) {
      return configSounds;
    }
    String[] configParticleStrings = configSoundChainString.split(",");
    if (configParticleStrings.length == 0) {
      ConfigSound configSound = computeConfigSound(configSoundChainString);
      if (configSound != null) {
        configSounds.add(configSound);
      }
    }
    for (String configParticleString : configParticleStrings) {
      ConfigSound configSound = computeConfigSound(configParticleString);
      if (configSound == null) {
        continue;
      }
      configSounds.add(configSound);
    }
    return configSounds;
  }

  /**
   * Computes a ConfigSound from the passed String
   *
   * @param configSoundString String to compute
   * @return Computed ConfigSound
   */
  private ConfigSound computeConfigSound(String configSoundString) {
    String[] params = configSoundString.split("-");
    if (params.length < 3) {
      LogUtil.sendWarnLog("Not enough parameters for ConfigSound '" + configSoundString + "'.");
      return null;
    }
    String soundName = params[0];
    Sound sound;
    try {
      sound = Sound.valueOf(soundName);
    } catch (Exception e) {
      LogUtil.sendWarnLog("Unknown sound '" + soundName + "' configured in '" + configSoundString + "'.");
      return null;
    }
    float volume;
    try {
      volume = (float) Double.parseDouble(params[1]);
    } catch (Exception e) {
      LogUtil.sendWarnLog("Invalid volume '" + params[1] + "' configured in '" + configSoundString + "'.");
      return null;
    }
    float pitch;
    try {
      pitch = (float) Double.parseDouble(params[2]);
    } catch (Exception e) {
      LogUtil.sendWarnLog("Invalid pitch '" + params[2] + "' configured in '" + configSoundString + "'.");
      return null;
    }
    long delayTicks = 0;
    if (params.length > 3) {
      try {
        delayTicks = Long.parseLong(params[3]);
      } catch (Exception e) {
        LogUtil.sendWarnLog("Invalid delay ticks '" + params[3] + "' configured " +
            "in '" + configSoundString + "'. Using 0.");
      }
    }
    return new ConfigSound(sound, volume, pitch, delayTicks);
  }

  /**
   * Computes the MobActionChains found in the passed String
   *
   * @param mobActionChainString String containing the MobActionChains
   * @return List of found MobActionChains
   */
  private List<MobActionChain> computeMobActionChains(String mobActionChainString) {
    List<MobActionChain> mobActionChains = new ArrayList<>();
    if (mobActionChainString == null) {
      return mobActionChains;
    }
    // This list will store the MobActions as they are fetched and will then be
    // added to a MobActionChain and be cleared
    List<MobAction> mobActions = new ArrayList<>();
    Pattern chanceRegex = Pattern.compile("-\\([0-9.]+\\)");
    for (String mobActionString : mobActionChainString.split(",")) {
      // MobAction strings should look something like this:
      // spawnmob:skeleton1-3-10-0-(0.2)
      //
      // Extract the MobActionType
      String[] typeSplit = mobActionString.split(":");
      if (typeSplit.length < 2) {
        LogUtil.sendWarnLog("Invalid Mob Action String '" + mobActionString + "'.");
        continue;
      }
      MobActionType mobActionType = MobActionType.fromName(typeSplit[0]);
      if (mobActionType == null) {
        LogUtil.sendWarnLog(
            "Invalid Mob Action Type '" + typeSplit[0] + "' found in Mob Action '" + mobActionString + "'.");
        continue;
      }
      // Check if the chance of the MobActionChain is included in the current
      // MobAction by search for -(CHANCE)
      Matcher chanceMatcher = chanceRegex.matcher(mobActionString);
      Double chance = null;
      if (chanceMatcher.find()) {
        String chanceString = chanceMatcher.group(0).replace("-(", "").replace(")", "");
        try {
          chance = Double.valueOf(chanceString);
        } catch (Exception e) {
          e.printStackTrace();
          continue;
        }
        if (chance == null || chance < 0 || chance > 1) {
          LogUtil.sendWarnLog("Invalid chance for MobActionChain '" + chanceString + "'.");
          continue;
        }
        mobActionString = mobActionString.replaceAll(chanceRegex.pattern(), "");
      }
      String[] params = String.join(" ", Arrays.copyOfRange(typeSplit, 1, typeSplit.length)).split("-");
      MobAction mobAction = null;
      switch (mobActionType) {
        case CONSOLE_COMMAND:
          mobAction = computeConsoleCommandMobAction(params);
          break;
        case EFFECT:
          mobAction = computeEffectMobAction(params);
          break;
        case MESSAGE:
          mobAction = computeMessageMobAction(params);
          break;
        case PARTICLE:
          mobAction = computeParticleMobAction(params);
          break;
        case SOUND:
          mobAction = computeSoundMobAction(params);
          break;
        case SPAWN:
          mobAction = computeSpawnMobAction(params);
          break;
        case SUICIDE:
          mobAction = computeSuicideMobAction(params);
          break;
        default:
          continue;
      }
      if (mobAction == null) {
        continue;
      }
      mobActions.add(mobAction);
      if (chance == null) {
        continue;
      }
      // Chance was specified, create a MobActionChain with it
      mobActionChains.add(new MobActionChain(new ArrayList<>(mobActions), chance));
      mobActions.clear();
    }
    if (!mobActions.isEmpty()) {
      mobActionChains.add(new MobActionChain(mobActions, 1));
    }
    return mobActionChains;
  }

  /**
   * Computes a ConsoleCommandMobAction based on the passed parameters
   *
   * @param params Parameters to compute
   * @return Computed ConsoleCommandMobAction
   */
  private ConsoleCommandMobAction computeConsoleCommandMobAction(String[] params) {
    if (params.length < 1) {
      LogUtil.sendWarnLog("Not enough parameters provided for ConsoleCommandMobAction. Got: " +
          Arrays.toString(params));
      return null;
    }
    List<String> commands = Arrays.asList(params[0].replace("[", "").replace("]", "").trim().split(";"));
    if (commands.isEmpty()) {
      LogUtil.sendWarnLog("Couldn't find commands for ConsoleCommandMobAction from parameters: " +
          Arrays.toString(params));
      return null;
    }
    long delayTicks = 0;
    if (params.length > 1) {
      try {
        delayTicks = Long.valueOf(params[1]);
      } catch (Exception e) {
        LogUtil.sendWarnLog("Invalid delay ticks '" + params[1] + "' from parameters: " +
            Arrays.toString(params) + ". Using 0.");
        delayTicks = 0;
      }
    }
    return new ConsoleCommandMobAction(commands, delayTicks);
  }

  /**
   * Computes an EffectMobAction from the passed parameters
   *
   * @param params Parameters to compute
   * @return Computed EffecTmobAction
   */
  private EffectMobAction computeEffectMobAction(String[] params) {
    if (params.length < 4) {
      LogUtil.sendWarnLog("Not enough parameters provided for EffectMobAction. Got: " +
          Arrays.toString(params));
      return null;
    }
    PotionEffectType potionEffectType = PotionEffectType.getByName(params[0]);
    if (potionEffectType == null) {
      LogUtil.sendWarnLog("Unknown Potion Effect Type '" + params[0] + "' from parameters: " +
          Arrays.toString(params));
      return null;
    }
    int amplifier;
    try {
      amplifier = Integer.parseInt(params[1]);
    } catch (Exception e) {
      LogUtil.sendWarnLog("Invalid potion level '" + params[1] + "' from parameters: " +
          Arrays.toString(params));
      return null;
    }
    int durationTicks;
    try {
      durationTicks = Integer.parseInt(params[2]);
    } catch (Exception e) {
      LogUtil.sendWarnLog("Invalid potion duration '" + params[2] + "' from parameters: " +
          Arrays.toString(params));
      return null;
    }
    MobActionTarget mobActionTarget = MobActionTarget.fromName(params[3]);
    if (mobActionTarget == null) {
      LogUtil.sendWarnLog("Unknown MobActionTarget '" + params[3] + "' from parameters: " +
          Arrays.toString(params));
      return null;
    }
    return new EffectMobAction(mobActionTarget, new PotionEffect(
        potionEffectType,
        durationTicks,
        amplifier - 1));
  }

  /**
   * Computes a MessageMobAction from the passed parameters
   *
   * @param params Parameters to compute
   * @return Computed MessageMobAction
   */
  private MessageMobAction computeMessageMobAction(String[] params) {
    if (params.length < 3) {
      LogUtil.sendWarnLog("Not enough parameters provided for MessageMobAction. Got: " +
          Arrays.toString(params));
      return null;
    }
    String message = params[0].replace("[", "").replace("]", "").trim();
    double radius;
    try {
      radius = Double.parseDouble(params[1]);
    } catch (Exception e) {
      LogUtil.sendWarnLog("Invalid amount '" + params[1] + "' from parameters: " +
          Arrays.toString(params));
      return null;
    }
    long delayTicks = 0;
    if (params.length > 2) {
      try {
        delayTicks = Long.parseLong(params[2]);
      } catch (Exception e) {
        LogUtil.sendWarnLog("Invalid delay ticks '" + params[2] + "' from parameters: " +
            Arrays.toString(params) + ". Using 0.");
      }
    }
    return new MessageMobAction(message, radius, delayTicks);
  }

  /**
   * Computes a ParticleMobAction from the passed parameters
   *
   * @param params Parameters to compute
   * @return Computed ParticleMobAction
   */
  private ParticleMobAction computeParticleMobAction(String[] params) {
    if (params.length < 6) {
      LogUtil.sendWarnLog("Not enough parameters provided for ParticleMobAction. Got: " +
          Arrays.toString(params));
      return null;
    }
    ConfigParticle configParticle = ConfigUtil.computeConfigParticle(
        String.join("-", Arrays.copyOfRange(params, 0, 6)));
    if (configParticle == null) {
      return null;
    }
    long delayTicks = 0;
    if (params.length > 6) {
      try {
        delayTicks = Long.parseLong(params[6]);
      } catch (Exception e) {
        LogUtil.sendWarnLog("Invalid delay ticks '" + params[6] + "' from parameters: " +
            Arrays.toString(params) + ". Using 0.");
      }
    }
    return new ParticleMobAction(configParticle, delayTicks);
  }

  /**
   * Computes a SoundMobAction from the passed parameters
   *
   * @param params Parameters to compute
   * @return Computed SoundMobAction
   */
  private SoundMobAction computeSoundMobAction(String[] params) {
    if (params.length < 4) {
      LogUtil.sendWarnLog("Not enough parameters provided for SoundMobAction. Got: " +
          Arrays.toString(params));
      return null;
    }
    ConfigSound configSound = computeConfigSound(String.join("-", params));
    if (configSound == null) {
      return null;
    }
    return new SoundMobAction(configSound);
  }

  /**
   * Computes a SpawnMobAction from the passed parameters
   *
   * @param params Parameters to compute
   * @return Computed SpawnMobAction
   */
  private SpawnMobAction computeSpawnMobAction(String[] params) {
    if (params.length < 3) {
      LogUtil.sendWarnLog("Not enough parameters provided for SpawnMobAction. Got: " +
          Arrays.toString(params));
      return null;
    }
    String mobId = params[0];
    int amount;
    try {
      amount = Integer.parseInt(params[1]);
    } catch (Exception e) {
      LogUtil.sendWarnLog("Invalid amount '" + params[1] + "' from parameters: " +
          Arrays.toString(params));
      return null;
    }
    double radius;
    try {
      radius = Double.parseDouble(params[2]);
    } catch (Exception e) {
      LogUtil.sendWarnLog("Invalid amount '" + params[2] + "' from parameters: " +
          Arrays.toString(params));
      return null;
    }
    long delayTicks = 0;
    if (params.length > 3) {
      try {
        delayTicks = Long.parseLong(params[3]);
      } catch (Exception e) {
        LogUtil.sendWarnLog("Invalid delay ticks '" + params[3] + "' from parameters: " +
            Arrays.toString(params) + ". Using 0.");
      }
    }
    return new SpawnMobAction(mobId, amount, radius, delayTicks);
  }

  /**
   * Computes a SuicideMobAction from the passed parameters
   *
   * @param params Parameters to compute
   * @return Computed SuicideMobAction
   */
  private SuicideMobAction computeSuicideMobAction(String[] params) {
    if (params.length < 1) {
      LogUtil.sendWarnLog("Not enough parameters provided for SuicideMobAction. Got: " +
          Arrays.toString(params));
      return null;
    }
    MobActionTarget mobActionTarget = MobActionTarget.fromName(params[0]);
    if (mobActionTarget == null) {
      LogUtil.sendWarnLog("Unknown MobActionTarget '" + params[0] + "' from parameters: " +
          Arrays.toString(params));
      return null;
    }
    return new SuicideMobAction(mobActionTarget);
  }
}
