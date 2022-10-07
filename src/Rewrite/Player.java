package Rewrite;
import java.util.ArrayList;

public class Player 
{
	private String name; //Hi, I'm Paul.
	private int turnsLeft; //1 då man får turn, 2 om man blir attacked
	private int PlayerID; //Checka om playerID+1 finns, annars gå tillbaka till 0.
	private Player NextPlayer; //The target we attack
	private ArrayList<Card> hand = new ArrayList<Card>();
	private boolean alive = true;
	
	//Somehow assigna en player till ett ID och namn
	public void addTurns(int t)
	{
		turnsLeft += t;
	}
	
	public Player(String n)
	{
		name = n;
	}
	
	public void SetID(int i)
	{
		PlayerID = i;
	}
	
	public boolean isAlive()
	{
		return alive;
	}
	
	//Draw
	public void AddToHand(Card c)
	{
		if(c.getType() != 0)
		{
			
		}
		else
		{
			boolean defused = false;
			for(int i = 0; i < hand.size(); i++)
			{
				if(hand.get(i).getType() == 1)
				{
					defused = true;
					hand.remove(i);
					//Place the kitten somewhere in the deck
				}
			}
			if(!defused)
			{
				
			}
			//BOOM
		}
	}
	
	public void PassTurn()
	{
		
	}
	
	public void SetNextPlayer(Player p)
	{
		NextPlayer = p;
	}
	
	public Player GetNextPlayer()
	{
		return NextPlayer;
	}
	
	public void Explode()
	{
		alive = false;
	}
}
