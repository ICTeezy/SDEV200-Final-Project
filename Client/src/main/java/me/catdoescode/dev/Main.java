package me.catdoescode.dev;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import me.catdoescode.dev.network.PacketRegistry;
import me.catdoescode.dev.network.packets.serverbound.SBMessagePacket;
import me.catdoescode.dev.network.packets.serverbound.SBUsernameSetPacket;

public class Main 
{

    public static void main(String[] args) throws IOException 
    {
        SocketChannel connectionSocket = SocketChannel.open();
        connectionSocket.connect(new InetSocketAddress(25565));

        ByteBuffer buffer;
        int sentBytes = 0;

        SBUsernameSetPacket usernamePacket = new SBUsernameSetPacket("cteezy");
        buffer = PacketRegistry.Server.write(usernamePacket);
        buffer.flip();

        sentBytes = connectionSocket.write(buffer);
        System.out.println("Sent: " + sentBytes);

        SBMessagePacket packet = new SBMessagePacket("Some message");
        buffer = PacketRegistry.Server.write(packet);
        buffer.flip();

        sentBytes = connectionSocket.write(buffer);
        System.out.println("Sent: " + sentBytes);

    }
    
}
