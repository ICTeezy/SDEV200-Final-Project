package me.catdoescode.dev.network;

import java.nio.ByteBuffer;

public interface Packet
{
    ByteBuffer write();
}
