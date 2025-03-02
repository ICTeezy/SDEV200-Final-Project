package me.catdoescode.dev;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
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
import me.catdoescode.dev.network.packets.clientbound.CBMessagePacket;
import me.catdoescode.dev.network.packets.clientbound.CBUserJoinPacket;
import me.catdoescode.dev.network.packets.clientbound.CBUsernameInUsePacket;
import me.catdoescode.dev.network.packets.serverbound.SBMessagePacket;
import me.catdoescode.dev.network.packets.serverbound.SBUsernameSetPacket;

public class CatChatServer 
{

    private final Map<SocketChannel, String> users = new HashMap<>(); 
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
                        onUserDisconnect(clientChannel);
                    }
                }
            }
        }
        catch (IOException e)
        {

        }
    }

    private void onPacketBuild(Packet packet, SocketChannel connection)
    {
        ServerboundPacket serverPacket = (ServerboundPacket) packet;

        switch (serverPacket.type())
        {
            case MESSAGE:
            {
                SBMessagePacket messagePacket = (SBMessagePacket) serverPacket;

                if (users.containsKey(connection)) //Connection has sent a UsernameSetPacket
                {
                    users.forEach((channel, ignored) -> 
                    {
                        String username = users.get(connection);
                        CBMessagePacket cbMessagePacket = new CBMessagePacket(messagePacket.message(), username);
                        ByteBuffer buffer = PacketRegistry.Client.write(cbMessagePacket);
                        buffer.flip();

                        System.out.println("Sending message from `" + username + "`: " + messagePacket.message());

                        try 
                        {
                            channel.write(buffer);
                        } 
                        catch (IOException e) 
                        {
                            e.printStackTrace();
                        }
                    });
                }

                break;
            }
            case USERNAME:
            {
                SBUsernameSetPacket usernamePacket = (SBUsernameSetPacket) packet;
                String username = usernamePacket.username();

                if (!users.values().contains(username)) 
                {
                    System.out.println("Setting username: " + username);
                    users.put(connection, username);
                }
                else
                {
                    CBUsernameInUsePacket usernameInUsePacket = new CBUsernameInUsePacket(username);
                    ByteBuffer buffer = PacketRegistry.Client.write(usernameInUsePacket);
                    buffer.flip();

                    try 
                    {
                        connection.write(buffer);
                    } 
                    catch (IOException e) 
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void onUserDisconnect(SocketChannel connection)
    {
        if (users.containsKey(connection))
        {

            String disconnectedUser = users.get(connection);
            users.remove(connection); //Remove before iteration so we don't send data on a closed connection.

            System.out.println(disconnectedUser + " disconnected.");

            CBUserJoinPacket disconnectPacket = new CBUserJoinPacket(disconnectedUser);
            ByteBuffer buffer = PacketRegistry.Client.write(disconnectPacket);
            buffer.flip();

            users.forEach((channel, ignored) ->
            {
                try 
                {
                    channel.write(buffer);
                } 
                catch (IOException e) 
                {
                    e.printStackTrace();
                }
            });
        }

        packetBuilders.remove(connection);

        try 
        {
            connection.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

}
