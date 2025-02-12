package me.eeshe.gtmobs.models.config;

import me.eeshe.gtmobs.util.StringUtil;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public record ConfigTitle(String title, String subtitle, int fadeInTicks, int durationTicks, int fadeOutTicks) {

  public void send(Player player) {
    send(player, new HashMap<>());
  }

  public void send(Player player, Map<String, String> placeholders) {
    if (placeholders == null) {
      send(player);
      return;
    }
    String formattedTitle = title;
    String formattedSubtitle = subtitle;
    for (Map.Entry<String, String> entry : placeholders.entrySet()) {
      formattedTitle = formattedTitle.replace(entry.getKey(), entry.getValue());
      formattedSubtitle = formattedSubtitle.replace(entry.getKey(), entry.getValue());
    }
    player.sendTitle(StringUtil.formatColor(formattedTitle), StringUtil.formatColor(formattedSubtitle),
        fadeInTicks, durationTicks, fadeOutTicks);
  }
}
