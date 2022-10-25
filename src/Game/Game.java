package Game;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import CardPiles.Deck;
import CardPiles.Discard;
import Cardstuff.Card;
import Cardstuff.CardAction;
import Cardstuff.Card.CardType;
import NetworkingBullshit.Server;
import Player.Player;

public class Game 
{
	private static ArrayList<Player> players = new ArrayList<Player>();
	private static int turnsToTake = 1; //attacked?
    private static Player currentPlayer;
	private static int playersLeft;
	private static int NOPETIME = 5;

    public static void AddPlayer(Player p)
    {
        players.add(p);
    }
	//Setup
    public static void initGame(int numPlayers, int numBots)
    {
		playersLeft = numPlayers + numBots;
        if(playersLeft > 5 || playersLeft < 2)
		{
			System.out.println("Incorrect number of players.");
			System.exit(0);
		}
        try 
		{
			//Initiate a server
	        new Server(numPlayers, numBots);
			//Initiates a deck
			new Deck(playersLeft).initDeck();
			//Randomize a first player
	        Random rnd = new Random();
			System.out.println("We're running boyz");
	        RunGame(rnd.nextInt(players.size()));
		} 
		catch(Exception e) 
		{
			System.out.println(e.getMessage());
		}
    } 

	//If not noped, do the action.
	private static void play(Player playedBy, Card c)
	{
		//Add to discard handled in here
		playedBy.RemoveFromHand(c.getType());
		c.Action();
	}
   
	//gmaeloopen
	private static void RunGame(int firstPlayer)
    {
        currentPlayer = players.get(firstPlayer);
		playersLeft = players.size();

		//TODO: REWRITE THIS ENTIRE THING
		//So what does it do?
		//While players left > 1, keep playing. OK
		//Whose turn is it?
		//The player whose turn it is gets to see their possible plays
		//The actions are made here. Bad.
		//When passed, check if another player is still in the game.
		//If no players, declare winner.
		do
        {
		    try 
		    {
                Card fetchedCard = new Card(CardType.ExplodingKitten);
    			whisper("It is now your turn.\nThis is your hand:", currentPlayer);
    			for(Card c : currentPlayer.getHand())
    			{
    				whisper("\n" + c.getType(), currentPlayer);
    			}
    			//get input
    			//for each card om input matchar enumname
    			//If they do, check for nopes, if no nopes; c.play()
    			//if none do: "not a valid move, sorry"
    			String[] response = currentPlayer.readMessage(false).split(" ");
    			
    			//TODO: Fix this stupid retarded monkey code
    			switch(response[0].toLowerCase())
    			{
    				case "pass":
    					Pass(false);
    				break;
					case "two":
						for(Card c : currentPlayer.getHand())
						{
							int count = 0;
							if(c.getType().toString().toLowerCase() == response[1].toLowerCase())
							{
								count++;
							}
							//so dum but it works I hope
							if(count >= 2)
							{
								currentPlayer.RemoveFromHand(c.getType());
								currentPlayer.RemoveFromHand(c.getType());
								new CardAction(null).Two();
							}
						}
					break;
					case "three":
					for(Card c : currentPlayer.getHand())
						{
							int count = 0;
							if(c.getType().toString().toLowerCase() == response[1].toLowerCase())
							{
								count++;
							}
							//so dum but it works I hope
							if(count >= 3)
							{
								currentPlayer.RemoveFromHand(c.getType());
								currentPlayer.RemoveFromHand(c.getType());
								currentPlayer.RemoveFromHand(c.getType());
								new CardAction(null).Three();
							}
						}
					break;
    				default:
    					if(currentPlayer.has(response[0]))
    					{
    						for(Card c : currentPlayer.getHand())
    						{
    							if(c.getType().toString().toLowerCase() == response[0])
    							{
    								fetchedCard = c;
    								break;
    							}
    						}
    					}
    					break;
    				//if player says nope, check if he has nope, 
    				//if he does, do nope, else MWAH MWAH
    			}
    			if(fetchedCard.getType() != CardType.ExplodingKitten)
    			{
    			    announce(currentPlayer.getID() + " wishes to play: " + fetchedCard.getType());
                    //game
                    if(Discard.Nopes() <= 5)
                    {
                        ExecutorService threadpool = Executors.newFixedThreadPool(players.size());
                          //nopable
                          //if a player has a nope, do they wish to play it?
                          for(Player p : players)
                          {
                              if(p.has("Nope"))
                              {
                                  whisper("Play nope? Press <enter> to play uwu", p);
                                  Runnable task = new Runnable() 
                                  {
                                      @Override
                                      public void run() 
                                      {
                                          try 
                                          {
                                              String nextMessage = p.readMessage(true); //Read that is interrupted after secondsToInterruptWithNope
                                              
                                              boolean hasNope = false;
                                              for(Card c : p.getHand())
                                              {
                                                  if(c.getType() == CardType.Nope)
                                                  {
                                                      hasNope = true;
                                                  }
                                              }
                                              
                                              if(!nextMessage.equals(" ") && hasNope) 
                                              {
                                                  //Dum ful lösning but it works
                                                  p.RemoveFromHand(CardType.Nope);
                                                  play(p, new Card(CardType.Nope));
                                                  announce("Player " + p.getID() + " played Nope");
                                              }
                                          } 
                                          catch(Exception e) 
                                          {
                                              System.out.println("addToDiscardPile: " +e.getMessage());
                                          }
                                      }
                                  };
                                  threadpool.execute(task);
                              }
                         } 
                         //add an additional delay to avoid concurrancy problems with the ObjectInputStream
                         threadpool.awaitTermination((NOPETIME*1000)+500, TimeUnit.MILLISECONDS);
                    }
                    else
                    {
                      //not nopable
                      announce("No nopes left");
                      play(currentPlayer, fetchedCard);
                    }
    			}
		    }
		    catch(Exception e)
		    {
		        System.out.println(e.getMessage());
		    }
        }
        while(players.size()>1);
		
        //Declare winner.
        Game.announce("SVERIGE, VI HAR EN VINNARE.");
        System.exit(0);
    }

    //Säger åt alla i lobbyn vad som just hände
    public static void announce(String s)
    {
        for(Player p : players)
        {
            p.sendMessage(s);
        }
    }

	//Säger bara åt currentPlayer
	public static void whisper(String s, Player p)
    {
	    p.sendMessage(s);
	}

	public static void Pass(boolean skipped)
	{
		//If attacked, turnes left 3. make it 2
		turnsToTake--;
		if(!skipped)
		{
			currentPlayer.draw();
		}
		//Switch player when turns run out.
		if(turnsToTake == 0)
		{
			SwitchPlayer(turnsToTake, false);
		}
	}

	public static void SwitchPlayer(int turns, boolean attacked)
	{
		//The problem is that modifyability gets crippled, Attack 3x exists in some expansion.
		if(attacked)
		{
			turnsToTake += turns;
		}
		else
		{
			turnsToTake = 1;
		}
		//TODO: Potentiell felkälla. Checka om player är sist. woRKS IN THEORY
		if(players.indexOf(currentPlayer) < players.size()-1)
		{
			//Ugly but works in theory
			currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
		}
		else
		{
			currentPlayer = players.get(0);
		}
	}

	public static ArrayList<Player> getPlayers()
	{
		return players;
	}

	public static Player getCurrentPlayer()
	{
		return currentPlayer;
	}

	public static void removePlayer(Player p)
	{
		players.remove(p);
	}
}
