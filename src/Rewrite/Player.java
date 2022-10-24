package Rewrite;
import java.util.*; 
import java.io.*; 
import java.net.*;

import Rewrite.Card.CardType;
public class Player
{
    public int playerID;

    public boolean online;
    public boolean isBot;
    public boolean exploded = false;

    public Socket connection;
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
        this.online = (connection != null) ? true : false;
    }

    //Kills em
    public void KABOOM()
    {
        exploded = true;
        //Stoppar hela handen i discard
        for(Card c : hand)
        {
            Discard.Add(c);
        }
    }

    public void SortHand()
    {
        hand.sort(null);
    }
    public void draw()
    {
        hand.add(Deck.getDeck().remove(0));
    }
    public void add(Card c)
    {
        hand.add(c);
    }
    public boolean HasNope()
    {
        for(Card c : hand)
        {
            if (c.getType() == CardType.Nope)
            {
                return true;
            }
        }
        return false;
    }
    public void RemoveFromHand(Card.CardType t)
    {
        for(Card c : hand)
        {
            if(c.getType() == t)
            {
                Discard.Add(c);
                hand.remove(c);
                break;
            }
        }
    }
    public void sendMessage(Object message) 
    {
        if(online) 
        {
            try 
            {
                outToClient.writeObject(message);
            } 
            catch (Exception e) 
            {
                //Something went wrong oh no
            }
        } 
        else if(!isBot)
        {
            System.out.println(message);                
        }
    }
    public String readMessage(boolean interruptable) 
    {
        String word = " "; 
        if(online)
        {
            try
            {
                word = (String) inFromClient.readObject();
            } 
            catch (Exception e)
            {
                System.out.println("Reading from client failed: " + e.getMessage());
            }
        }
        else
        {
            try 
            {
                if(interruptable) 
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    int millisecondsWaited = 0;
                    //TODO: Fixa den här.
                    while(!br.ready() && millisecondsWaited<(5*1000)) 
                    {
                        Thread.sleep(200);
                        millisecondsWaited += 200;
                    }
                    if(br.ready())
                        return br.readLine();               		
                } 
                else 
                {
                    in = new Scanner(System.in); 
                    word=in.nextLine();
                }
            } 
            catch(Exception e){System.out.println(e.getMessage());
            }
        }
        return word;
    }

}
