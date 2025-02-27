package me.catdoescode.dev.network.packets.clientbound;

import java.nio.ByteBuffer;

import me.catdoescode.dev.network.PacketType;
import me.catdoescode.dev.network.PacketType.Clientbound;
import me.catdoescode.dev.network.packets.ClientboundPacket;

public class CBMessagePacket implements ClientboundPacket
{
    
    public static CBMessagePacket read(ByteBuffer buffer)
    {
        return null;
    }

    @Override
    public ByteBuffer write() 
    {
        return null;
    }

    @Override
    public Clientbound type() 
    {
        return PacketType.Clientbound.MESSAGE;
    }
    
}
