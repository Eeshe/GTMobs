package me.eeshe.gtmobs.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class PlaceholderUtil {

  /**
   * Formats the placeholder for the passed text using the passed player.
   *
   * @param player Player to format the message with.
   * @param text   Text to format.
   * @return Formatted message.
   */
  public static String formatPapiPlaceholders(OfflinePlayer player, String text) {
    boolean isPapiInstalled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    if (player != null) {
      text = text.replace("%player%", player.getName());
    }
    if (isPapiInstalled) {
      PlaceholderAPI.setPlaceholders(player, text);
    }
    return text;
  }

  /**
   * Formats the passed list of texts using the passed placeholders.
   *
   * @param texts        List of texts to format.
   * @param placeholders Placeholders to use in the formatting.
   * @return Formatted list of texts with the passed placeholders.
   */
  public static List<String> formatPlaceholders(List<String> texts, Map<String, String> placeholders) {
    // Create copy of list
    texts = new ArrayList<>(texts);

    ListIterator<String> textIterator = texts.listIterator();
    while (textIterator.hasNext()) {
      textIterator.set(formatPlaceholders(textIterator.next(), placeholders));
    }
    return texts;
  }

  /**
   * Formats the passed text using the passed placeholders.
   *
   * @param text         Text to format.
   * @param placeholders Placeholders to use in the formatting.
   * @return Formatted text with the passed placeholders.
   */
  public static String formatPlaceholders(String text, Map<String, String> placeholders) {
    for (Map.Entry<String, String> entry : placeholders.entrySet()) {
      text = text.replace(entry.getKey(), StringUtil.formatColor(entry.getValue()));
    }
    return text;
  }
}
