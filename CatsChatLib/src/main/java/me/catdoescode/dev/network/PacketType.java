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
        MESSAGE,
        USER_JOIN,
        USER_LEAVE
    }
}
