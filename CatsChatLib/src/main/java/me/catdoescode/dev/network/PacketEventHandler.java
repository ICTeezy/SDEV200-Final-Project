package me.catdoescode.dev.network;

import java.nio.channels.SocketChannel;

public interface PacketEventHandler 
{
    void onPacketBuild(Packet packet, SocketChannel channel);
}
