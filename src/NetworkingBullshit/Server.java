package NetworkingBullshit;
import java.io.*; 
import java.net.*;

import Rewrite.Game;
import Rewrite.Player;

public class Server 
{
    public ServerSocket aSocket;
    public Server(int p, int b) throws Exception
    {
        serverRun(p, b);
    }
    private void serverRun(int numberPlayers, int numberOfBots) throws Exception 
    {
    	//add this instance as a player
        Game.AddPlayer(new Player(0, false, null, null, null)); 
        //Open for connections if there are online players
        //add a bot foreach number of bots
        for(int i=0; i<numberOfBots; i++) 
        {
            Game.AddPlayer((new Player(i+1, true, null, null, null)));  
        }
        if(numberPlayers>1)
        {
            aSocket = new ServerSocket(2048);
        }
        for(int i=numberOfBots+1; i<numberPlayers+numberOfBots; i++) 
        {
            Socket connectionSocket = aSocket.accept();
            ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
            ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
            //add an online client
            Game.AddPlayer(new Player(i, false, connectionSocket, inFromClient, outToClient)); 
            System.out.println("Connected to player " + i);
            outToClient.writeObject("You connected to the server as player " + i + "\n");
        }    
    }
}
