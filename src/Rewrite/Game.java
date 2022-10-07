package Rewrite;
import java.util.ArrayList;

public class Game 
{
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Player> playersLeft = new ArrayList<Player>();
	private Deck d;
	private int currentPlayer = 0;
	
	//init game
	public Game(ArrayList<Player> p)
	{
		for(Player x : p)
		{
			x.SetID(players.size());
			players.add(x);
		}
		initGame();
	}
	
	void initGame()
	{
		d = new Deck();
		d.Setup(players.size());
	}
	
	void PassTurn()
	{
		currentPlayer = (currentPlayer+1)%playersLeft.size();
		playersLeft.get(currentPlayer).addTurns(1);
	}
	
	public Player getNextPlayer()
	{
		return playersLeft.get((currentPlayer+1)%playersLeft.size());
	}
}
