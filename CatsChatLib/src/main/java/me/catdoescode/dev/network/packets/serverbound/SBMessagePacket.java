package me.catdoescode.dev.network.packets.serverbound;

import java.nio.ByteBuffer;

import me.catdoescode.dev.network.PacketType.Serverbound;
import me.catdoescode.dev.network.packets.ServerboundPacket;

public class SBMessagePacket implements ServerboundPacket
{

    private final String message;

    public SBMessagePacket(String message)
    {
        this.message = message;
    }

    public static SBMessagePacket read(ByteBuffer buffer) 
    {
        int messageLength = buffer.getInt();
        byte[] message = new byte[messageLength];

        for (int i = 0; i < messageLength; i++)
        {
            message[i] = buffer.get();
        }

        return new SBMessagePacket(new String(message));
    }

    public String message()
    {
        return this.message;
    }

    @Override
    public ByteBuffer write() 
    {
        ByteBuffer buffer = ByteBuffer.allocate(4 + message.length());

        buffer.putInt(message.length());
        buffer.put(message.getBytes());
        return buffer;

    }

    @Override
    public Serverbound type() 
    {
        return Serverbound.MESSAGE;
    }
    
}
