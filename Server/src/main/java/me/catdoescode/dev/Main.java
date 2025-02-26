package me.catdoescode.dev;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
        
        ServerSocket serverSocket;

        try 
        {
            serverSocket = new ServerSocket(port);
		} catch (IOException e) {
            System.out.println("Failed to start listening on port " + port + " is it available?");
			e.printStackTrace();
            return;
		}

        System.out.println("Started listening on port " + port + "...");

        Socket client;

        try 
        {
			client = serverSocket.accept();
		} 
        catch (IOException e) 
        {
            System.out.println("Failed to accept client connection.");
			e.printStackTrace();

            try 
            {
			    serverSocket.close();
		    }    
            catch (IOException ignored) {} // Don't care if couldn't close, the program is over anyways.
                
            return;
        }

        try 
        (
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter printWriter = new PrintWriter(client.getOutputStream(), true)
        )
        {       
            String clientMessage;
            while ((clientMessage = bufferedReader.readLine()) != null)
            {
                System.out.println("Client: " + clientMessage);
                printWriter.println("Echo: " + clientMessage);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try 
            {
                client.close();
			} 
            catch (IOException e) 
            {
				e.printStackTrace();
			}
        }

        try 
        {
			serverSocket.close();
		} 
        catch (IOException ignored) {} // Don't care if couldn't close, the program is over anyways.
    }

}
