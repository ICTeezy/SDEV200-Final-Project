package me.catdoescode.dev;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import me.catdoescode.dev.network.packets.serverbound.SBMessagePacket;

public class Main 
{

    public static void main(String[] args) throws IOException 
    {
        SocketChannel connectionSocket = SocketChannel.open();
        connectionSocket.connect(new InetSocketAddress(25565));
        SBMessagePacket packet = new SBMessagePacket("Some message");
        ByteBuffer buffer = packet.write();
        buffer.flip();
        int sentBytes = connectionSocket.write(buffer);
        System.out.println("Sent: " + sentBytes);
    }
    
}
