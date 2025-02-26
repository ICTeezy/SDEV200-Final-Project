package me.catdoescode.dev.network.packets;

import me.catdoescode.dev.network.Packet;
import me.catdoescode.dev.network.PacketType;

public interface ClientboundPacket<T extends ClientboundPacket<T>> extends Packet<T> 
{
   PacketType.Clientbound getType();
}
