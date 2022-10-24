package Rewrite;
import java.util.*; 
import Rewrite.Card.*;
public class CardAction 
{
    public CardAction(CardType c)
    {
        switch(c)
        {
            case Attack:
                Attack();
                break;
            case Favor:
                Favor();
                break;
            case Nope:
                Nope();
                break;
            case Shuffle:
                Shuffle();
                break;
            case Skip:
                Skip();
                break;
            case SeeTheFuture:
                SeeTheFuture();
                break;
            default:
                //Three and twos
                break;
        }
    }

    private void Attack()
    {

    }

    private void Favor()
    {

    }

    private void Nope()
    {

    }

    //OK
    private void Shuffle()
    {
        Deck.Shuffle();
        Game.Announce("The deck has been shuffled!");
    }


    private void Skip()
    {
        
    }

    //TODO: Test this
    private void SeeTheFuture()
    {
        ArrayList<Card> d = Deck.getDeck();
        for(int i = 0; i<3; i++)
        {
            Game.whisper(i+1 + ". " + d.get(i).getType());
        }
    }

    //Default cats
    public void Two()
    {

    }

    public void Three()
    {

    }
}
