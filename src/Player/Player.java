package Player;
import java.util.*;

import CardPiles.Deck;
import CardPiles.Discard;
import Cardstuff.Card;
import Cardstuff.Card.CardType;
import Game.Game;

import java.io.*; 
import java.net.*;
public class Player
{
    private int playerID;

    private boolean online;
    private boolean isBot;
    private boolean exploded = false;

    private Socket connection;
    private ObjectInputStream inFromClient;
    private ObjectOutputStream outToClient;

    private ArrayList<Card> hand = new ArrayList<Card>();

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

    public int getID()
    {
        return playerID;
    }

    //Kills em
    public void KABOOM()
    {
        this.exploded = true;
        Game.announce("KABOOM!!");
        //Stoppar hela handen i discard
        for(Card c : hand)
        {
            Discard.Add(c);
        }
        Game.removePlayer(this);
    }

    public void SortHand()
    {
        hand.sort(null);
    }
    public void draw()
    {
        Card drawn = Deck.getDeck().get(0);
        Deck.draw();
        if(drawn.getType() != CardType.ExplodingKitten)
        {
            Deck.draw();
        }
        else
        {
            //Bomben går igång instantly.
            drawn.Action();
            //Tar bort bomben så vi inte drar den.
            Deck.removeFromTop();
        }
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

    public boolean has(String s)
    {
        for(Card c : hand)
        {
            Game.announce("\n" + c.getType().toString().toLowerCase());
            Game.announce("\n" + s);
            if(s.equals(c.getType().toString().toLowerCase()))
            {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Card> getHand()
    {
        return hand;
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
                    while(!br.ready() && millisecondsWaited<(Game.getNopeTime()*1000)) 
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
