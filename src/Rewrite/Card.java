package Rewrite;

public class Card 
{
	private static enum cardTypes
	{
		ExplodingKitten,
		Defuse,
		Attack,
		Favor,
		Nope,
		Shuffle,
		Skip,
		SeeTheFuture,
		HairyPotatoCat,
		Cattermelon,
		RainbowRalphingCat,
		TacoCat,
		OverweightBikiniCat
	}
	private int maxCards;
	private int type;
	public Card(int t, int m)
	{
		type = t;
		maxCards = m;
	}
	
	//Då decken byggs så ska vi checka och he in max kort innan shuffle.
	public int getMax()
	{
		//Exploding kitten 4
		//Defuse 6
		// Attack 2x 4
		//Favor 4
		//Nope 5
		//Shuffle 4
		//Skip 4
		//See the future 3x 5
		//Cat cards 5 different, 4 each
		return maxCards; 
	}
	
	public int getTypes()
	{
		return 13;
	}
	
	//Typen visar vad för kort vi har.
	public int getType()
	{
		return type;
	}
	
	public int cardTypes()
	{
		return cardTypes.values().length;
	}
	
	//Om ett kort spelas så kör vi abilitin
	public void cardAbility(Player playedBy)
	{
		switch(type)
		{
		case(0): 
			playedBy.Explode();
			return;
		case(1):
			//Defuse
			return;
		case(2):
			playedBy.PassTurn();
			playedBy.GetNextPlayer().addTurns(2);
			return;
		case(3):
			//Favour
			//Target player, give card plz
			return;
		case(4):
			//Nope
			return;
		case(5):
			//Shuffle
			//Deck.shuffle
			return;
		case(6):
			//Skip
			playedBy.PassTurn();
			return;
		case(7):
			//See The future 3x
			//Deck.view top 3 cards
			return;
		case(8):
			//TACOCAT
			return;
		case(9):
			//Bearded Cat
			return;
		case(10):
			//Rainbow ralphing cat
			return;
		case(11):
			//Hairy Potato Cat
			return;
		case(12):
			//Cattermelon
			return;
		//Mer plats för annat stuff
		}
	}
}
