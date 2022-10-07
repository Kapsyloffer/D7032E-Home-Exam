package Rewrite;
import java.util.*; 
import java.io.*; 
import java.net.*;
import java.util.concurrent.*;

public class Deck 
{
	public static ArrayList<Card> deck = new ArrayList<Card>();
	public static ArrayList<Card> discard = new ArrayList<Card>();
	//Exploding kitten 4
	//Defuse 6
	// Attack 2x 4
	//Favor 4
	//Nope 5
	//Shuffle 4
	//Skip 4
	//See the future 3x 5
	//Cat cards 5 different, 4 each
	public static int[] maxC = new int[] {4, 6, 4, 4, 5, 4, 4, 5, 4, 4, 4, 4, 4};
	
	public void Setup(int players)
	{
		//6 defuse cards
		//Each player get a card in hand, then
		if(players >= 2 && players <= 3)
		{
			//discard all but 2 cards
			maxC[1] = 2;
		}
		if(players >= 4 && players <= 5)
		{
			//shuffle the rest into the pile
			maxC[1] -= players;
		}
		Card dummy = new Card(0, 4);
		for(int i = 0; i < dummy.getTypes(); i++)
		{
			Card card = new Card(i, maxC[i]);
			for(int j = 0; j < card.getMax(); j++)
			{
				deck.add(card);
			}
		}
		//Shuffle the deck
		//deck.shuffle.
	}
	
	//Players call this on draw
	public void draw(Player p)
	{
		p.AddToHand(deck.get(0));
		deck.remove(0);
	}
}
