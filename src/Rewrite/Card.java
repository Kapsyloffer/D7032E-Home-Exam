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
		OverweightBikiniCat,
	}
	private cardTypes type;
	private int maxCards;
	public Card(cardTypes t, int m)
	{
		type = t;
		maxCards = m;
	}
	
	public static cardTypes getType(int index)
	{
		return cardTypes.values()[index];
		//cardTypes.GetValues(cardTypes.GetType())).GetValue(index);
	}
	
	//Då decken byggs så ska vi checka och he in max kort innan shuffle. 
	//Max är by default 4, men man kan göra en exception för korttyp. 
	public static int GetMax(cardTypes t)
	{
		//Exploding kitten 4
		//Defuse 6
		// ^^ These two are fixed in deck.
		//Attack 2x 4
		//Favor 4
		//Shuffle 4
		//Skip 4
		//See the future 3x 5
		//Nope 5
		//Cat cards 5 different, 4 each
		
		//This is stupid dumb retarded, but I will fix it later.
		int m;
			switch(t)
			{
				case SeeTheFuture:
					m = 5;
					break;
				case Nope:
					m = 5;
					break;
				default:
					m = 4;
					break;
			}
		return m; 
	}
	
	public static int getTypes()
	{
		return cardTypes.values().length;
	}
	
	//Typen visar vad för kort vi har.
	public cardTypes getType()
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
		case ExplodingKitten: 
			//playedBy.Explode();
			System.out.println("A KITTEN HAS APPEARED!!");
			return;
		case Defuse:
			//Defuse
			System.out.println("DEFUSE HAS BEEN USED!!");
			return;
		case Attack:
			//Attacks are supposed to stack. Fix that somehow.
			System.out.println("ATTACK 2X!!");
			//playedBy.PassTurn();
			//playedBy.GetNextPlayer().addTurns(2);
			return;
		case Favor:
			System.out.println("Favour:");
			//plaued.TargetPlayer(playedBy);
			//Favour
			//Target player, give card plz
			return;
		case Nope:
			System.out.println("Nopestack");
			//Nope
			return;
		case Shuffle:
			//Shuffle
			//Deck.shuffle
			return;
		case Skip:
			//Skip
			//playedBy.PassTurn();
			return;
		case SeeTheFuture:
			//See The future 3x
			//Deck.view top 3 cards
			return;
		default:
			//2 av samma
			//3 av samma
			//en av varje
			return;
		//Mer plats för annat stuff
		}
	}
}