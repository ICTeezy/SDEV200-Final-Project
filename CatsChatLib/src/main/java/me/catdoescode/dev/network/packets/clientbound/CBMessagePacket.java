package me.catdoescode.dev.network.packets.clientbound;

import java.nio.ByteBuffer;

import me.catdoescode.dev.network.PacketType;
import me.catdoescode.dev.network.PacketType.Clientbound;
import me.catdoescode.dev.network.packets.ClientboundPacket;

public class CBMessagePacket implements ClientboundPacket
{
    
    private final String username;
    private final String message;

    public CBMessagePacket(String message, String username)
    {
        this.username = username;
        this.message = message;
    }
    
    public String message()
    {
        return this.message;
    }

    public String username()
    {
        return this.username;
    }

    public static CBMessagePacket read(ByteBuffer buffer)
    {
        return null;
    }

    @Override
    public ByteBuffer write() 
    {
         ByteBuffer buffer = ByteBuffer.allocate(4 + username.length() + 4 + message.length());

        buffer.putInt(username.length());
        buffer.put(username.getBytes());

        buffer.putInt(message.length());
        buffer.put(message.getBytes());

        return buffer;
    }

    @Override
    public Clientbound type() 
    {
        return PacketType.Clientbound.MESSAGE;
    }
    
}
