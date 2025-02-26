package me.catdoescode.dev.network.packets.serverbound;

import java.nio.ByteBuffer;

import me.catdoescode.dev.network.PacketType.Serverbound;
import me.catdoescode.dev.network.packets.ServerboundPacket;

public class SBMessagePacket implements ServerboundPacket<SBMessagePacket>
{

    @Override
    public ByteBuffer buffer() 
    {
        return null;
    }

    @Override
    public Serverbound getType() 
    {
        return Serverbound.MESSAGE;
    }
    
}
