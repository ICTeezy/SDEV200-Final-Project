package me.catdoescode.dev;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import me.catdoescode.dev.network.packets.serverbound.SBMessagePacket;

public class Main 
{

    public static void main(String[] args) throws IOException 
    {
        SocketChannel connectionSocket = SocketChannel.open();
        connectionSocket.connect(new InetSocketAddress(25565));

        SBMessagePacket packet = new SBMessagePacket("Some message");
        int sentBytes = connectionSocket.write(packet.write());

        System.out.println("Sent: " + sentBytes);

        while (true) {}
    }
    
}
