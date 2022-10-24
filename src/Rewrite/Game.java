package Rewrite;
import java.util.*; 
import Rewrite.*;

public class Game 
{
	public static ArrayList<Player> players = new ArrayList<Player>();
	public static int numberOfTurnsToTake = 1; //attacked?
    private static Player currentPlayer;
	private static int playersLeft;
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
			whisper("It is now your turn.\nThis is your hand:");
			for(Card c : currentPlayer.getHand())
			{
				whisper("\n" + c.getType());
			}
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
	public static void whisper(String s)
    {
	    currentPlayer.sendMessage(s);
	}

	public static void Pass()
	{
		
	}

	public static Player getCurrentPlayer()
	{
		return currentPlayer;
	}
}
