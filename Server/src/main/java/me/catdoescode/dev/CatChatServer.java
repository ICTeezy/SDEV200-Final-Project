package me.catdoescode.dev;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.catdoescode.dev.network.Packet;
import me.catdoescode.dev.network.PacketBuilder;
import me.catdoescode.dev.network.PacketRegistry;
import me.catdoescode.dev.network.packets.ServerboundPacket;
import me.catdoescode.dev.network.packets.serverbound.SBMessagePacket;

public class CatChatServer 
{

    private final Map<SocketChannel, PacketBuilder> packetBuilders = new HashMap<>();

    public void start(int port)
    {
        try 
        (
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            Selector selector = Selector.open();
        )
        {
            serverChannel.configureBlocking(false);
            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Server listening on port " + port + " ...");

            while (true)
            {
                tick(serverChannel, selector);
            }
        }
        catch (IOException e)
        {
            System.out.println("Server failed to start listening on port " + port + "!");
            e.printStackTrace();
        }
    }

    private void tick(ServerSocketChannel serverChannel, Selector selector)
    {
        try
        {
            selector.select();

            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

            while (keyIterator.hasNext())
            {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();

                if (key.isAcceptable())
                {
                    SocketChannel clientChannel = serverChannel.accept();
                    clientChannel.configureBlocking(false);
                    clientChannel.register(selector, SelectionKey.OP_READ);
                    packetBuilders.put(clientChannel, new PacketBuilder(PacketRegistry.Server::read, this::onPacketBuild));
                    System.out.println("Accepted incoming connection.");
                }
                else if (key.isReadable())
                {
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    PacketBuilder packetBuilder = packetBuilders.get(clientChannel);
                    int bytesRead = packetBuilder.read(clientChannel);

                    if (bytesRead == -1)
                    {
                        clientChannel.close();
                        onUserDisconnect();
                        packetBuilders.remove(clientChannel);
                    }
                }
            }
        }
        catch (IOException e)
        {

        }
    }

    private void onPacketBuild(Packet packet)
    {
        ServerboundPacket serverPacket = (ServerboundPacket) packet;

        switch (serverPacket.type())
        {
            case MESSAGE:
            {
                SBMessagePacket messagePacket = (SBMessagePacket) serverPacket;
                System.out.println("Message: " + messagePacket.message()); 
            }
        }
    }

    private void onUserDisconnect()
    {
        System.out.println("User disconnected.");
    }

}
