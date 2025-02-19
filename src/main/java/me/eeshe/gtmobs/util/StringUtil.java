package me.eeshe.gtmobs.util;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.config.Message;

/**
 * This utility class helps to format or parse Strings to other Strings or
 * numeric values.
 */
public class StringUtil {
  private static final Pattern HEX_PATTERN = Pattern.compile("&#[a-fA-F0-9]{6}");
  private static final TreeMap<Integer, String> ROMAN_NUMBERS_MAP = new TreeMap<>(Map.ofEntries(
      Map.entry(1000, "M"),
      Map.entry(900, "CM"),
      Map.entry(500, "D"),
      Map.entry(400, "CD"),
      Map.entry(100, "C"),
      Map.entry(90, "XC"),
      Map.entry(50, "L"),
      Map.entry(40, "XL"),
      Map.entry(10, "X"),
      Map.entry(9, "IX"),
      Map.entry(5, "V"),
      Map.entry(4, "IV"),
      Map.entry(1, "I")));

  private static final Map<String, String> ENCHANTMENT_NAMES = Map.ofEntries(
      Map.entry("aqua_affinity", "Aqua Affinity"),
      Map.entry("bane_of_arthropods", "Bane of Arthropods"),
      Map.entry("blast_protection", "Blast Protection"),
      Map.entry("channeling", "Channeling"),
      Map.entry("binding_curse", "Curse of Binding"),
      Map.entry("vanishing_curse", "Curse of Vanishing"),
      Map.entry("depth_strider", "Depth Strider"),
      Map.entry("efficiency", "Efficiency"),
      Map.entry("feather_falling", "Feather Falling"),
      Map.entry("fire_aspect", "Fire Aspect"),
      Map.entry("fire_protection", "Fire Protection"),
      Map.entry("flame", "Flame"),
      Map.entry("fortune", "Fortune"),
      Map.entry("frost_walker", "Frost Walker"),
      Map.entry("impaling", "Impaling"),
      Map.entry("infinity", "Infinity"),
      Map.entry("knockback", "Knockback"),
      Map.entry("looting", "Looting"),
      Map.entry("loyalty", "Loyalty"),
      Map.entry("luck_of_the_sea", "Luck of the Sea"),
      Map.entry("lure", "Lure"),
      Map.entry("mending", "Mending"),
      Map.entry("multishot", "Multishot"),
      Map.entry("piercing", "Piercing"),
      Map.entry("power", "Power"),
      Map.entry("projectile_protection", "Projectile Protection"),
      Map.entry("protection", "Protection"),
      Map.entry("punch", "Punch"),
      Map.entry("quick_charge", "Quick Charge"),
      Map.entry("respiration", "Respiration"),
      Map.entry("riptide", "Riptide"),
      Map.entry("sharpness", "Sharpness"),
      Map.entry("silk_touch", "Silk Touch"),
      Map.entry("smite", "Smite"),
      Map.entry("soul_speed", "Soul Speed"),
      Map.entry("sweeping", "Sweeping Edge"),
      Map.entry("swift_sneak", "Swift Sneak"),
      Map.entry("thorns", "Thorns"),
      Map.entry("unbreaking", "Unbreaking"));

  /**
   * Translates all the color codes on the passed message and returns it back.
   *
   * @param text Text whose color codes will be translated.
   * @return String with all the translated color codes.
   */
  public static String formatColor(String text) {
    if (text == null)
      return null;

    return ChatColor.translateAlternateColorCodes('&', text);
  }

  /**
   * Formats the passed Enum name, so it is a capitalized text.
   * Ex. WITHER_SKELETON_SPAWN_EGG -> Wither Skeleton Spawn Egg
   *
   * @param enumeration Enum that will be formatted.
   * @return Formatted Enum's name.
   */
  public static String formatEnum(Enum<?> enumeration) {
    return formatEnum(enumeration.name());
  }

  /**
   * Formats the passed Enum name, so it is a capitalized text.
   * Ex. WITHER_SKELETON_SPAWN_EGG -> Wither Skeleton Spawn Egg
   *
   * @param enumName Enum name that will be formatted.
   * @return Formatted Enum's name.
   */
  public static String formatEnum(String enumName) {
    String[] split = enumName.split("_");
    if (split.length == 1) {
      split = enumName.split(" ");
    }
    if (split.length > 0) {
      for (int i = 0; i < split.length; i++) {
        String word = split[i];
        if (word.equalsIgnoreCase("of") || word.equalsIgnoreCase("the")) {
          // Don't capitalize 'of' nor 'the'
          split[i] = word.toLowerCase();
          continue;
        }
        split[i] = capitalize(word);
      }
      return String.join(" ", split);
    } else
      return capitalize(enumName);
  }

  /**
   * Capitalizes and returns the passed text.
   *
   * @param text Text that will be capitalized.
   * @return Capitalized text.
   */
  public static String capitalize(String text) {
    // Extracting the first letter, capitalizing it and joining the rest of the
    // String.
    return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
  }

  /**
   * Formats the passed number to a String using the configured format.
   *
   * @param number Number to format.
   * @return Formatted number string.
   */
  public static String formatNumber(double number) {
    return GTMobs.getInstance().getMainConfig().getDecimalFormat().format(number);
  }

  /**
   * Converts time in milliseconds to a formatted string in the format
   * "dd:hh:mm:ss".
   *
   * @param millis time in milliseconds
   * @return formatted time string in the format "dd:hh:mm:ss"
   */
  public static String formatMillis(long millis) {
    return formatSeconds(TimeUnit.MILLISECONDS.toSeconds(millis));
  }

  /**
   * Converts time in ticks to a formatted string in the format "dd:hh:mm:ss".
   *
   * @param ticks time in ticks
   * @return formatted time string in the format "dd:hh:mm:ss"
   */
  public static String formatTicks(long ticks) {
    return formatSeconds(ticks / 20);
  }

  /**
   * Converts time in seconds to a formatted string in the format "dd:hh:mm:ss".
   *
   * @param seconds time in seconds
   * @return formatted time string in the format "dd:hh:mm:ss"
   */
  public static String formatSeconds(long seconds) {
    long minutes = TimeUnit.SECONDS.toMinutes(seconds) % 60;
    seconds -= TimeUnit.MINUTES.toSeconds(minutes);

    return String.format("%02d:%02d", minutes, seconds);
  }

  /**
   * Attempts to parse the passed String to an Integer value. If it fails it sends
   * a message to the passed CommandSender.
   *
   * @param commandSender CommandSender that will receive error messages if
   *                      needed.
   * @param string        String that will be parsed into an Integer value.
   * @return Parsed integer value.
   */
  public static Integer parseInteger(CommandSender commandSender, String string) {
    try {
      return Integer.parseInt(string);
    } catch (Exception e) {
      Message.NON_NUMERIC_INPUT.sendError(commandSender);
      return null;
    }
  }

  /**
   * Attempts to parse the passed String to a Doubke value. If it fails it sends a
   * message to the passed CommandSender.
   *
   * @param commandSender CommandSender that will receive error messages if
   *                      needed.
   * @param string        String that will be parsed into a Double value.
   * @return Parsed integer value.
   */
  public static Double parseDouble(CommandSender commandSender, String string) {
    try {
      return Double.parseDouble(string);
    } catch (Exception e) {
      Message.NON_NUMERIC_INPUT.sendError(commandSender);
      return null;
    }
  }

  public static String parseToRoman(int level) {
    int l = ROMAN_NUMBERS_MAP.floorKey(level);
    if (level == l) {
      return ROMAN_NUMBERS_MAP.get(level);
    }
    return ROMAN_NUMBERS_MAP.get(l) + parseToRoman(level - l);
  }

  public static String getEnchantmentName(NamespacedKey enchantmentKey) {
    return ENCHANTMENT_NAMES.get(enchantmentKey.getKey());
  }

  public static String formatItem(ItemStack item) {
    if (item.getItemMeta() == null || !item.getItemMeta().hasDisplayName()) {
      return formatEnum(item.getType());
    }
    return item.getItemMeta().getDisplayName();
  }

  public static String formatTime(LocalDateTime dateTime, String format) {
    return format
        .replace("%day%", Integer.toString(dateTime.getDayOfMonth()))
        .replace("%month%", Integer.toString(dateTime.getMonthValue()))
        .replace("%year%", Integer.toString(dateTime.getYear()))
        .replace("%hours%", padLeftZeros(Integer.toString(dateTime.getHour()), 2))
        .replace("%minutes%", padLeftZeros(Integer.toString(dateTime.getMinute()), 2))
        .replace("%seconds%", padLeftZeros(Integer.toString(dateTime.getSecond()), 2));
  }

  public static String padLeftZeros(String inputString, int length) {
    if (inputString.length() >= length) {
      return inputString;
    }

    var sb = new StringBuilder();

    while (sb.length() < length - inputString.length()) {
      sb.append('0');
    }

    sb.append(inputString);

    return sb.toString();
  }

  public static long textToMillis(CommandSender sender, String timeText, boolean emptyEpoch) {
    Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?"
        + "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE);
    final Matcher m = timePattern.matcher(timeText);
    int years = 0;
    int months = 0;
    int weeks = 0;
    int days = 0;
    int hours = 0;
    int minutes = 0;
    int seconds = 0;
    boolean found = false;
    while (m.find()) {
      if (m.group() == null || m.group().isEmpty()) {
        continue;
      }
      for (int i = 0; i < m.groupCount(); i++) {
        if (m.group(i) != null && !m.group(i).isEmpty()) {
          found = true;
          break;
        }
      }
      if (found) {
        if (m.group(1) != null && !m.group(1).isEmpty()) {
          years = Integer.parseInt(m.group(1));
        }
        if (m.group(2) != null && !m.group(2).isEmpty()) {
          months = Integer.parseInt(m.group(2));
        }
        if (m.group(3) != null && !m.group(3).isEmpty()) {
          weeks = Integer.parseInt(m.group(3));
        }
        if (m.group(4) != null && !m.group(4).isEmpty()) {
          days = Integer.parseInt(m.group(4));
        }
        if (m.group(5) != null && !m.group(5).isEmpty()) {
          hours = Integer.parseInt(m.group(5));
        }
        if (m.group(6) != null && !m.group(6).isEmpty()) {
          minutes = Integer.parseInt(m.group(6));
        }
        if (m.group(7) != null && !m.group(7).isEmpty()) {
          seconds = Integer.parseInt(m.group(7));
        }
        break;
      }
    }
    if (!found) {
      return 0;
    }
    final Calendar calendar = new GregorianCalendar();

    if (emptyEpoch)
      calendar.setTimeInMillis(0);
    if (years > 0)
      calendar.add(Calendar.YEAR, years);
    if (months > 0)
      calendar.add(Calendar.MONTH, months);
    if (weeks > 0)
      calendar.add(Calendar.WEEK_OF_YEAR, weeks);
    if (days > 0)
      calendar.add(Calendar.DAY_OF_MONTH, days);
    if (hours > 0)
      calendar.add(Calendar.HOUR_OF_DAY, hours);
    if (minutes > 0)
      calendar.add(Calendar.MINUTE, minutes);
    if (seconds > 0)
      calendar.add(Calendar.SECOND, seconds);

    final Calendar max = new GregorianCalendar();
    max.add(Calendar.YEAR, 10);
    if (calendar.after(max)) {
      return max.getTimeInMillis();
    }
    if (calendar.getTimeInMillis() == 0) {
      Message.INVALID_TIME_FORMAT.sendError(sender);
    }
    return calendar.getTimeInMillis();
  }
}
