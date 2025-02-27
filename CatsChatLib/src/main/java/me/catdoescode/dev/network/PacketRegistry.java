package me.catdoescode.dev.network;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import me.catdoescode.dev.network.packets.ClientboundPacket;
import me.catdoescode.dev.network.packets.ServerboundPacket;
import me.catdoescode.dev.network.packets.clientbound.CBMessagePacket;
import me.catdoescode.dev.network.packets.serverbound.SBMessagePacket;

public class PacketRegistry 
{
    public static class Server 
    {

        private static Map<Integer, Function<ByteBuffer, ServerboundPacket>> registry = new HashMap<>();

        static 
        {
            registry.put(PacketType.Serverbound.MESSAGE.id(), SBMessagePacket::read);
        }

        public static ServerboundPacket read(ByteBuffer buffer)
        {
            buffer.flip();

            int packetID = buffer.getInt();

            if (!registry.containsKey(packetID))
            {
                return null;
            }

            return registry.get(packetID).apply(buffer);
        }
        
    }

    public static class Client 
    {
        private static Map<Integer, Function<ByteBuffer, ClientboundPacket>> registry = new HashMap<>();

        static 
        {
            registry.put(1, CBMessagePacket::read);
        }

        public static ClientboundPacket read(ByteBuffer buffer)
        {
            buffer.flip();

            int packetID = buffer.getInt();

            if (!registry.containsKey(packetID))
            {
                return null;
            }

            return registry.get(packetID).apply(buffer);
        }
    }

    private PacketRegistry() {}
    
}
