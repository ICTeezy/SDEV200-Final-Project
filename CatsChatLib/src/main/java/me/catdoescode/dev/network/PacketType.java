package me.catdoescode.dev.network;

public class PacketType 
{
    public enum Serverbound
    {
        MESSAGE(1),
        USERNAME(2);
    
        Serverbound(int packetID)
        {
            this.packetID = packetID;
        }

        public int id()
        {
            return this.packetID;
        }

        private int packetID; 
    }

    public enum Clientbound
    {
        MESSAGE(1),
        USER_JOIN(2),
        USER_DISCONNECT(3),
        USERNAME_IN_USE(4),
        USERNAME_SET(5);


        Clientbound(int packetID)
        {
            this.packetID = packetID;
        }

        public int id()
        {
            return this.packetID;
        }

        private int packetID; 
    }
}