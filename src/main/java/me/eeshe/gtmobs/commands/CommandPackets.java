package me.eeshe.gtmobs.commands;

import java.lang.reflect.Field;

import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.FakePlayer;
import me.eeshe.gtmobs.util.LogUtil;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityMetadata;

public class CommandPackets extends PluginCommand {

  public CommandPackets(GTMobs plugin) {
    super(plugin);

    setName("packets");
    setPlayerCommand(true);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    // protocolManager.addPacketListener(new PacketAdapter(getPlugin(),
    // PacketType.Play.Server.ENTITY_STATUS) {
    //
    // @Override
    // public void onPacketSending(PacketEvent event) {
    // LogUtil.sendWarnLog("PACKET: " + event.getPacket());
    // }
    //
    // @Override
    // public void onPacketReceiving(PacketEvent event) {
    // LogUtil.sendWarnLog("PACKET: " + event.getPacket());
    // }
    //
    // });

    Player player = (Player) sender;

    ChannelDuplexHandler handler = new ChannelDuplexHandler() {
      @Override
      public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
      }

      @Override
      public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof PacketPlayOutEntityMetadata) {
          LogUtil.sendWarnLog("INTERCEPTED ENTITY METADATA PACKET");
          try {
            Class<?> metadataClass = msg.getClass();
            Field idField = metadataClass.getDeclaredField("a");
            idField.setAccessible(true);

            int packetEntityId = (int) idField.get(msg);
            if (FakePlayer.getFakePlayers().containsKey(packetEntityId)) {
              LogUtil.sendWarnLog("CANCELLING FAKE PLAYER PACKET");
              return;
            }
          } catch (Exception ignore) {
          }
          return;
        }
        super.write(ctx, msg, promise);
      }
    };

    EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
    nmsPlayer.playerConnection.networkManager.channel.pipeline().addBefore("packet_handler",
        "some-random-name",
        handler);
  }
}
