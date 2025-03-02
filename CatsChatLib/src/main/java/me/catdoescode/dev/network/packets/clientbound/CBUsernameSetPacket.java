package me.catdoescode.dev.network.packets.clientbound;

import java.nio.ByteBuffer;

import me.catdoescode.dev.network.PacketType;
import me.catdoescode.dev.network.packets.ClientboundPacket;

public class CBUsernameSetPacket implements ClientboundPacket
{

    private final String username;

    public CBUsernameSetPacket(String username)
    {
        this.username = username;
    }

    public String username()
    {
        return this.username;
    }

    public static CBUsernameSetPacket read(ByteBuffer buffer)
    {
        int usernameLength = buffer.getInt();
        byte[] username = new byte[usernameLength];

        for (int i = 0; i < usernameLength; i++)
        {
            username[i] = buffer.get();
        }

        return new CBUsernameSetPacket(new String(username));
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
    public PacketType.Clientbound type() 
    {
        return PacketType.Clientbound.USERNAME_SET;
    }
    
}
