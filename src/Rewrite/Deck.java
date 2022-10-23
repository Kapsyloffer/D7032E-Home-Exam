package Rewrite;
import java.util.*; 
import Rewrite.Card.*;

public class Deck 
{
    private static HashMap<CardType, Integer> maxCards = new HashMap<Card.CardType, Integer>();
    private static ArrayList<Card> cards = new ArrayList<Card>();
	private int players;

    public Deck(int p)
    {
        this.players = p;
        //Initiate deck
        setCards(this.players);
    }

	//Problemet är att det hårdkodas fast på olika ställen.
	private void setCards(int p)
	{
		//TODO: Gör denna med JSON
		//Dessa dependar på spelare.
		maxCards.put(CardType.ExplodingKitten, p-1);
		maxCards.put(CardType.Defuse, 6-p);

		//Dessa är basically samma hela tiden, kan läsas in från fil.
		//Men hur gör vi med expansions?

        //Vanilla cards
		maxCards.put(CardType.Attack, 4);
		maxCards.put(CardType.Favor, 4);
		maxCards.put(CardType.Nope, 5);
		maxCards.put(CardType.Shuffle, 4);
		maxCards.put(CardType.Skip, 4);
		maxCards.put(CardType.SeeTheFuture, 5);
        //Vanillas
		maxCards.put(CardType.HairyPotatoCat, 4);
		maxCards.put(CardType.Cattermelon, 4);
		maxCards.put(CardType.RainbowRalphingCat, 4);
		maxCards.put(CardType.TacoCat, 4);
		maxCards.put(CardType.OverweightBikiniCat, 4);
	}

    //Builds the deck.
    public void initDeck()
    {
        //Clear out the deck
        cards = new ArrayList<Card>();
        for(Map.Entry<Card.CardType, Integer> mC : maxCards.entrySet())
        {
            System.out.println(mC.getKey() + " " + mC.getValue());
            //T.ex om key Shuffle har 4 som value stoppar vi in 4 shufflekort.
            for(int i = 0; i < mC.getValue(); i++)
            {
                Card c = new Card(mC.getKey());
                cards.add(c);
            }
        }
        //Why shuffle twice?
        Collections.shuffle(cards);

        //TODO: Access playerlist in game.
       /* for(Player player : players) 
        {
            Card c = new Card(Card.CardType.Defuse);
            player.add(c);
            //Draw
            for(int i=0; i<7; i++) 
            {
                player.draw();
            }
        }*/ 
    }

    public static ArrayList<Card> getDeck()
    {
        return cards;
    }

    public static Card draw()
    {
        return cards.remove(0);
    }
}
