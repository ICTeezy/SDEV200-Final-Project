package me.catdoescode.dev.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.function.Consumer;
import java.util.function.Function;

public class PacketBuilder 
{

    private static final int PACKET_HEADER_SIZE = 4; //4 Bytes for the packet size
    
    private final Function<ByteBuffer, Packet> packetRegistry;
    private final Consumer<Packet> packetBuildCallback;
    
    private ByteBuffer buffer;
    private BuildState buildState;
    
    
    public PacketBuilder(Function<ByteBuffer, Packet> packetRegistry, Consumer<Packet> packetBuildCallback)
    {
        this.buffer = ByteBuffer.allocate(PACKET_HEADER_SIZE);
        this.buildState = BuildState.SIZE;

        this.packetRegistry = packetRegistry;
        this.packetBuildCallback = packetBuildCallback;
    }

    public int read(SocketChannel channel) throws IOException
    {
        int bytesRead = channel.read(buffer);

        if (buffer.remaining() == 0)
        {
            if (buildState == BuildState.SIZE)
            {
                buffer.flip();
                int packetSize = buffer.getInt();
                buffer = ByteBuffer.allocate(packetSize);
                buildState = BuildState.PACKET;
            }
            else
            {
                Packet packet = packetRegistry.apply(buffer);
                this.packetBuildCallback.accept(packet);
                buildState = BuildState.SIZE;
            }
        }

        return bytesRead;
    }

    enum BuildState
    {
        SIZE,
        PACKET
    }

}
