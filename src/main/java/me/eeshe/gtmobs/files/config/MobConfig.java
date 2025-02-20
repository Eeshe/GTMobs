package me.eeshe.gtmobs.files.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.GTMob;
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
            "&eGTZombie",
            Map.of(Attribute.GENERIC_MOVEMENT_SPEED, 1D),
            List.of(new ConfigSound(Sound.ENTITY_EVOCATION_ILLAGER_CAST_SPELL, true, 1.0F, 0.5F)),
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
            "&3GTSkeleton",
            Map.of(Attribute.GENERIC_MAX_HEALTH, 200D),
            List.of(new ConfigSound(Sound.ENTITY_EVOCATION_ILLAGER_CAST_SPELL, true, 1.0F, 0.5F)),
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
    config.addDefault(path + ".display-name", gtMob.getDisplayName());
    ConfigUtil.writeAttributeMap(config, path + ".attributes", gtMob.getAttributes());
    writeConfigSounds(config, path + ".spawn-sounds", gtMob.getSpawnSounds());
    ConfigUtil.writeConfigParticles(config, path + ".hit-particles", gtMob.getOnHitParticles());
    ConfigUtil.writeConfigParticles(config, path + ".death-particles", gtMob.getOnDeathParticles());
    ConfigUtil.writeIntRange(config, path + ".experience-drop", gtMob.getExperienceDrop());
    writeDefaultMobActions(path + ".events.hit", gtMob.getOnHitActions());
    writeDefaultMobActions(path + ".events.target-hit", gtMob.getOnTargetHitActions());
    writeDefaultMobActions(path + ".events.death", gtMob.getOnDeathActions());
  }

  /**
   * Writes the passed ConfigSounds to the passed config file
   *
   * @param config       Config file to write
   * @param path         Path to write in
   * @param configSounds ConfigSounds to write
   */
  private void writeConfigSounds(FileConfiguration config, String path, List<ConfigSound> configSounds) {
    config.addDefault(path, String.join(",", configSounds.stream().map(ConfigSound::toString)
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
    String displayName = mobSection.getString("display-name", "");
    Map<Attribute, Double> attributes = ConfigUtil.fetchAttributeMap(config, id + ".attributes");
    List<ConfigSound> spawnSounds = computeConfigSounds(mobSection.getString("spawn-sounds", ""));
    List<ConfigParticle> onHitParticles = ConfigUtil.computeConfigParticles(
        mobSection.getString("hit-particles"));
    List<ConfigParticle> onDeathParticles = ConfigUtil.computeConfigParticles(
        mobSection.getString("death-particles"));
    IntRange experienceDrop = ConfigUtil.fetchIntRange(config, id + ".experience-drop");
    List<MobActionChain> onHitActions = computeMobActionChains(mobSection.getString("events.hit", ""));
    List<MobActionChain> onTargetHitActions = computeMobActionChains(mobSection.getString("events.target-hit", ""));
    List<MobActionChain> onDeathActions = computeMobActionChains(mobSection.getString("events.death", ""));

    return new GTMob(id, entityType, isBaby, displayName, attributes, spawnSounds, onHitParticles,
        onDeathParticles, experienceDrop, onHitActions, onTargetHitActions, onDeathActions);
  }

  /**
   * Computes a List of ConfigSounds from the passed String
   *
   * @param configSoundChainString String to compute
   * @return Computed ConfigSounds
   */
  private List<ConfigSound> computeConfigSounds(String configSoundChainString) {
    List<ConfigSound> configSounds = new ArrayList<>();
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
