package me.shockpast.roflan.runnables;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.injector.netty.WirePacket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.shockpast.roflan.Roflan;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

// much love to https://github.com/LoreSchaeffer/CustomF3Brand
// i took his implementation
public class BrandRunnable {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final ProtocolManager protocolManager;
    private final FileConfiguration config;
    private final List<String> names;
    private Integer index = 0;

    public BrandRunnable() {
        this.protocolManager = Roflan.getInstance().protocolManager;
        this.config = Roflan.getInstance().getConfig();

        this.names = config.getStringList("features.custom_brand.names");
    }

    public void run() {
        if (names.size() > 1 && index++ == names.size() - 1)
            index = 0;

        String name = names.get(index);

        for (Player player : Bukkit.getOnlinePlayers()) {
            try {
                Class<?> dataSerializer = Class.forName("net.minecraft.network.PacketDataSerializer");
                Constructor<?> dataConstructor = dataSerializer.getConstructor(ByteBuf.class);
                ByteBuf buf = (ByteBuf)dataConstructor.newInstance(Unpooled.buffer());

                Method writeString = dataSerializer.getDeclaredMethod("a", String.class);
                writeString.invoke(buf, "minecraft:brand");
                writeString.invoke(buf, LegacyComponentSerializer.legacySection()
                    .serialize(miniMessage.deserialize(name)));

                byte[] data = new byte[buf.readableBytes()];
                for (int i = 0; i < data.length; i++) data[i] = buf.getByte(i);

                WirePacket packet = new WirePacket(PacketType.Play.Server.CUSTOM_PAYLOAD, data);
                protocolManager.sendWirePacket(player, packet);
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
                | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
