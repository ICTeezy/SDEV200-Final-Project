package me.catdoescode.dev.network.packets.serverbound;

import java.nio.ByteBuffer;

import me.catdoescode.dev.network.PacketType;
import me.catdoescode.dev.network.PacketType.Serverbound;
import me.catdoescode.dev.network.packets.ServerboundPacket;

public class SBUsernameSetPacket implements ServerboundPacket
{

    private final String username;

    public SBUsernameSetPacket(String username)
    {
        this.username = username;
    }

    public String username()
    {
        return this.username;
    }

    public static SBUsernameSetPacket read(ByteBuffer buffer)
    {
        int usernameLength = buffer.getInt();
        byte[] username = new byte[usernameLength];

        for (int i = 0; i < usernameLength; i++)
        {
            username[i] = buffer.get();
        }

        return new SBUsernameSetPacket(new String(username));
    }

    @Override
    public ByteBuffer write() 
    {
        ByteBuffer buffer = ByteBuffer.allocate(4 + username.length());

        buffer.putInt(username.length());
        buffer.put(username.getBytes());

        return buffer;
    }

    @Override
    public Serverbound type() 
    {
        return PacketType.Serverbound.USERNAME;
    }
    
}
