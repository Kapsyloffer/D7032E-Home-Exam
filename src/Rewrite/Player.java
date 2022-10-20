package Rewrite;
import java.util.*; 
import java.io.*; 
import java.net.*;
import java.util.concurrent.*;

public class Player 
{
	public int playerID;
    public boolean online;
    public boolean isBot;
    public Socket connection;
    public boolean exploded = false;
    public ObjectInputStream inFromClient;
    public ObjectOutputStream outToClient;
    public ArrayList<Card> hand = new ArrayList<Card>();
    Scanner in = new Scanner(System.in);
    
    public Player(int playerID, boolean isBot, Socket connection, ObjectInputStream inFromClient, ObjectOutputStream outToClient)
    {
    	this.playerID = playerID; 
    	this.connection = connection; 
    	this.inFromClient = inFromClient; 
    	this.outToClient = outToClient; 
    	this.isBot = isBot;
    	//if no connection, then it's a bot. Else if we have connection it's a player.
    	this.online = (connection == null) ? false: true;
	}
    
    public String TargetPrint()
    {
    	return ("ID: " + playerID + " Hand:" + hand.size() + " ");
    }
    
    //Draw
  	public void AddToHand(Card c)
  	{
  		/*if(c.getType(0) )
  		{
  			hand.add(c);
  		}
  		else
  		{
  			boolean defused = false;
  			for(int i = 0; i < hand.size(); i++)
  			{
  				if(hand.get(i).getType() == 1 && !defused)
  				{
  					defused = true;
  					//Ta bort defuse från handen
  					hand.remove(i);
  					//Place the kitten somewhere in the deck
  					
  				}
  			}
  			if(!defused)
  			{
  				
  			}
  			//BOOM
  		}*/
  	}
  	
  	public void RemoveFromHand(Card c)
  	{
  		hand.remove(c);
  	}
    
    //Messaging & networking bs
}
