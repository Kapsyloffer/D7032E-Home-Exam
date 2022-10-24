package Rewrite;
import java.util.*; 
import Rewrite.*;

public class Game 
{
	public static ArrayList<Player> players = new ArrayList<Player>();
	public static int numberOfTurnsToTake = 1; //attacked?
    private static Player currentPlayer;
    public static void AddPlayer(Player p)
    {
        players.add(p);
    }
	//Setup
    public static void initGame(int numPlayers, int numBots)
    {
        if(numPlayers + numBots > 5 || numPlayers + numBots < 2)
		{
			System.out.println("Incorrect number of players.");
			System.exit(0);
		}
        try 
		{
	        //server(numPlayers, numBots);

			//Create the deck
			//TODO: Deckbuilder med for loop. Sätt typer i JSON maybe?
			//Skapa alla maxvärden per kort
			
			//Deck.InitDeck();
			//Initiates a deck
			new Deck(5).initDeck();

	        Random rnd = new Random();
	        RunGame(rnd.nextInt(players.size()));
		} 
		catch(Exception e) 
		{
			System.out.println(e.getMessage());
		}
    } 
   
	//gmaeloopen
	public static void RunGame(int firstPlayer)
    {
        Player currentPlayer = players.get(firstPlayer);
		int playersLeft = players.size();

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
            //game
        }
        while(players.size()>1);
        //Declare winner.
		Player winner = currentPlayer;
        Game.Announce("SVERIGE, VI HAR EN VINNARE.");
        System.exit(0);
    }

    //Säger åt alla i lobbyn vad som just hände
    public static void Announce(String s)
    {
        for(Player p : players)
        {
            p.sendMessage(s);
        }
    }

	//Säger bara åt currentPlayer
	public static void Whisper(String s)
    {
		curentPlayer.sendMessage(s);
	}
}
