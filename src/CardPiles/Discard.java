package CardPiles;
import java.util.*;

import Cardstuff.Card;
import Cardstuff.Card.CardType;

public class Discard 
{
    private static ArrayList<Card> dPile = new ArrayList<Card>();

    //Returns number of nopes.
    public static int Nopes()
    {
        int Nopes = 0;
        for(Card c : dPile)
        {
            if(c.getType() == CardType.Nope)
            {
                Nopes++;
            }
        }
        return Nopes;
    }

    public static void Add(Card c)
    {
        dPile.add(c);
    }
    
    public static ArrayList<Card> getDiscard()
    {
        return dPile;
    }
}
