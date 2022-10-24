package Rewrite;
import java.util.*; 
import Rewrite.Card.*;
public class CardAction 
{
    public CardAction(CardType c)
    {
        switch(c)
        {
            case ExplodingKitten:
                Bomb();
                break;
            case Defuse:
                Defuse();
                break;
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

    private void Bomb()
    {
        Game.announce("A WILD BOMBCAT APPEARS!!! SURPRISE ATTACK!!!");
        boolean hasDef = false;
        for(Cards c : Game.getCurrentPlayer().getHand())
        {
            if(c.getType() == CardType.Defuse)
            {   
                Game.announce("Defused.");
                Game.getCurrentPlayer().RemoveFromHand(c);
                Discard.Add(c);
                //defuse action
                c.Action();
            }
        }
        if(!hasDef)
        {
            KABOOM();
        }
    }

    private Defuse()
    {

        
    }

    private void Attack()
    {
        //Pass skip -> next player får 2 turns
        //Om attack på attack -> Next player får 4 turns osv
    }

    private void Favor()
    {
        //Target a player
        //Yo gimme a card
        //Thx
    }

    private void Nope()
    {

    }

    //OK
    private void Shuffle()
    {
        Deck.Shuffle();
        Game.announce("The deck has been shuffled!");
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
