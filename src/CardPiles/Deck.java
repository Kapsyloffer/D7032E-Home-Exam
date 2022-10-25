package CardPiles;
import java.util.*;

import Cardstuff.Card;
import Game.Game;
import Player.Player;

import org.json.*;

public class Deck 
{
    private static HashMap<String, Integer> maxCards = new HashMap<String, Integer>();
    private static ArrayList<Card> cards = new ArrayList<Card>();
	private int players;
    private boolean haveBeenSet = false;

    public Deck(int p)
    {
        this.players = p;
        //Initiate deck
        setCards(this.players);
    }

	//Problemet är att det hårdkodas fast på olika ställen.
	private void setCards(int p)
	{
        //Bara för att preventa double adding
        if(!haveBeenSet)
        {
            //TODO: Gör denna med JSON
            //Dessa dependar på spelare.
            if(p >=2 && p <=4)
            {
                maxCards.put("Defuse", 2);
            }
            else if (p == 5)
            {
                maxCards.put("Defuse", 1);
            }

            //Dessa är basically samma hela tiden, kan läsas in från fil.
            //Men hur gör vi med expansions?

            //Vanilla cards

            //TODO: Foreach card in json maxcard put

	    }   
        haveBeenSet = true;
    }

    //Builds the deck.
    public void initDeck()
    {
        //Clear out the deck
        cards = new ArrayList<Card>();
        for(Map.Entry<String, Integer> mC : maxCards.entrySet())
        {
            System.out.println(mC.getKey() + " " + mC.getValue());
            //T.ex om key Shuffle har 4 som value stoppar vi in 4 shufflekort.
            for(int i = 0; i < mC.getValue(); i++)
            {
                Card c = new Card(mC.getKey());
                cards.add(c);
            }
        }
        shuffle();

       for(Player player : Game.getPlayers()) 
        {
            //Give defuse in hand
            Card c = new Card("Defuse");
            player.add(c);
            //Draw
            for(int i=0; i<7; i++) 
            {
                player.draw();
            }
        }
        for(int i = 0; i < Game.getPlayers().size()-1; i++)
        {
            cards.add(new Card ("ExplodingKitten"));
        }
        shuffle();
    }

    public static ArrayList<Card> getDeck()
    {
        return cards;
    }

    public static Card draw()
    {
        return cards.remove(0);
    }

    public static void shuffle()
    {
        Collections.shuffle(cards);
    }

    public static void Insert(Card c, int p)
    {
        cards.add(p, c);
    }

}
