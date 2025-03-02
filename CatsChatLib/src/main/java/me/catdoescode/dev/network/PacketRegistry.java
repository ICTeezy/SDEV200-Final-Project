package me.catdoescode.dev.network;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import me.catdoescode.dev.network.packets.ClientboundPacket;
import me.catdoescode.dev.network.packets.ServerboundPacket;
import me.catdoescode.dev.network.packets.clientbound.CBMessagePacket;
import me.catdoescode.dev.network.packets.clientbound.CBUserDisconnectPacket;
import me.catdoescode.dev.network.packets.clientbound.CBUserJoinPacket;
import me.catdoescode.dev.network.packets.clientbound.CBUsernameInUsePacket;
import me.catdoescode.dev.network.packets.clientbound.CBUsernameSetPacket;
import me.catdoescode.dev.network.packets.serverbound.SBMessagePacket;
import me.catdoescode.dev.network.packets.serverbound.SBUsernameSetPacket;

public class PacketRegistry 
{
    public static class Server 
    {

        private static Map<Integer, Function<ByteBuffer, ServerboundPacket>> registry = new HashMap<>();

        static 
        {
            registry.put(PacketType.Serverbound.MESSAGE.id(), SBMessagePacket::read);
            registry.put(PacketType.Serverbound.USERNAME.id(), SBUsernameSetPacket::read);
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

        public static ByteBuffer write(ServerboundPacket packet)
        {
            ByteBuffer packetBuffer = packet.write();
            ByteBuffer prefixedBuffer = ByteBuffer.allocate(8 + packetBuffer.capacity()); //8 Bytes for packet size + type

            prefixedBuffer.putInt(packetBuffer.capacity() + 4); //+4 for the packet type
            prefixedBuffer.putInt(packet.type().id());
            
            packetBuffer.flip();
            prefixedBuffer.put(packetBuffer);

            return prefixedBuffer;
        }
        
    }

    public static class Client 
    {
        private static Map<Integer, Function<ByteBuffer, ClientboundPacket>> registry = new HashMap<>();

        static 
        {
            registry.put(PacketType.Clientbound.MESSAGE.id(), CBMessagePacket::read);
            registry.put(PacketType.Clientbound.USER_JOIN.id(), CBUserJoinPacket::read);
            registry.put(PacketType.Clientbound.USER_DISCONNECT.id(), CBUserDisconnectPacket::read);
            registry.put(PacketType.Clientbound.USERNAME_IN_USE.id(), CBUsernameInUsePacket::read);
            registry.put(PacketType.Clientbound.USERNAME_SET.id(), CBUsernameSetPacket::read);
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

        public static ByteBuffer write(ClientboundPacket packet)
        {
            ByteBuffer packetBuffer = packet.write();
            ByteBuffer prefixedBuffer = ByteBuffer.allocate(packetBuffer.capacity() + 8); //8 Bytes for packet size + type

            prefixedBuffer.putInt(packetBuffer.capacity());
            prefixedBuffer.putInt(packet.type().id());
            
            packetBuffer.flip();
            prefixedBuffer.put(packetBuffer);

            return prefixedBuffer;
        }
    }

    private PacketRegistry() {}
    
}
