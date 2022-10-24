package Rewrite;
import java.util.*; 
import Rewrite.*;

public class Game 
{
	public static ArrayList<Player> players = new ArrayList<Player>();
	public static int turnsToTake = 1; //attacked?
    private static Player currentPlayer;
	private static int playersLeft;
	public int secondsToInterruptWithNope = 5;

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
	private void play(Player playedBy, Card c)
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
			whisper("It is now your turn.\nThis is your hand:", currentPlayer);
			for(Card c : currentPlayer.getHand())
			{
				whisper("\n" + c.getType(), currentPlayer);
			}
			//get input
			//for each card om input matchar enumname
			//If they do, c.play()
			//if none do: "not a valid move, sorry"

            //game
			if(Discard.Nopes() <= 5)
			{
			  //nopable
			 
			}
			else
			{
			  //not nopable
			}
        }
        while(players.size()>1);
        //Declare winner.
		//Player winner = currentPlayer;
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

	//TODO: Implement this
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
			SwitchPlayer(turnsToTake);
		}
	}

	public static void SwitchPlayer(int turns)
	{
		boolean attacked = (turns > 1) ? true : false;
		//The problem is that modifyability gets crippled, Attack 3x exists in some expansion.
		if(attacked)
		{
			turns +=2;
		}
		//TODO: Potentiell felkälla. Checka om player är sist. woRKS IN THEORY
		if(players.indexOf(currentPlayer) < players.size()-1)
		{
			//Ugly but works in theory
			currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
		}
		else
		{
			currentPlayer = players.get(players.indexOf(currentPlayer) + 1);
		}
	}

	public static Player getCurrentPlayer()
	{
		return currentPlayer;
	}
}
