package me.catdoescode.dev.network.packets;

import me.catdoescode.dev.network.Packet;
import me.catdoescode.dev.network.PacketType;

public interface ServerboundPacket extends Packet
{
    PacketType.Serverbound type();
}
