package Rewrite;
import java.util.ArrayList;

public class Game 
{
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Player> playersLeft = new ArrayList<Player>();
	private Deck d;
	private int currentPlayer = 0;
	
	void initGame()
	{
		d = new Deck();
		d.Setup(4);
	}
	
	void PassTurn()
	{
		
	}
}
