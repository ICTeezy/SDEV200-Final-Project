package me.catdoescode.dev;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Main 
{

    public static void main(String[] args) 
    {

        if (args.length < 1)
        {
            System.out.println("Please provide a port to start the server on.");
            return;
        }

        int port;
        try
        {
            port = Integer.parseInt(args[0]);
        } 
        catch(IllegalArgumentException e)
        {
            System.out.println("The port number must be an integer.");
            return;
        }
        
        ServerSocketChannel serverSocket;
        Selector selector;

        try 
        {
            selector = Selector.open();
            serverSocket = ServerSocketChannel.open();

            serverSocket.configureBlocking(false); //Set the server socket to be non-blocking
            serverSocket.bind(new InetSocketAddress("localhost", port));
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
            System.out.println("Failed to start listening on port " + port + " is it available?");
			e.printStackTrace();
            return;
		}

        System.out.println("Started listening on port " + port + "...");

        while (true)
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
                        SocketChannel clientChannel = serverSocket.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("Accepted incoming connection.");
                    }
                    else if (key.isReadable())
                    {
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                    
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int bytesRead = clientChannel.read(buffer);

                        if (bytesRead == -1)
                        {
                            clientChannel.close();
                            System.out.println("Client disconnected.");
                            continue;
                        }

                        buffer.flip();
                        clientChannel.write(buffer);
                    }
                }

            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
    }

}
