package me.catdoescode.dev.network;

public class PacketType 
{
    public enum Serverbound
    {
        MESSAGE,
        USERNAME
    }

    public enum Clientbound
    {
        MESSAGE,
        USER_JOIN,
        USER_LEAVE
    }
}
