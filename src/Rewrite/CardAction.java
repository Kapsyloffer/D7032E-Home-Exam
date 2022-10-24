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
        for(Card c : Game.getCurrentPlayer().getHand())
        {
            if(c.getType() == CardType.Defuse)
            {   
                Game.announce("Defused.");
                //bruh
                Game.getCurrentPlayer().RemoveFromHand(c.getType());
                Discard.Add(c);
                //defuse action
                c.Action();
            }
        }
        if(!hasDef)
        {
            Game.getCurrentPlayer().KABOOM();
        }
    }

    private void Defuse()
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
        //foreach player left -> pick one
        //The picked one gets a prompt
        //Yo gimme a card
        //They select a card and give
        //Thx
    }

    private void Nope()
    {

    }

    //OK
    private void Shuffle()
    {
        Deck.shuffle();
        Game.announce("The deck has been shuffled!");
    }


    private void Skip()
    {
        //Pass and skip draw is true uwu
        Game.Pass(true);
    }

    //TODO: Test this
    private void SeeTheFuture()
    {
        ArrayList<Card> d = Deck.getDeck();
        for(int i = 0; i<3; i++)
        {
            Game.whisper(i+1 + ". " + d.get(i).getType(), Game.getCurrentPlayer());
        }
    }

    //Default cats
    public void Two()
    {
        //Target, och yo ge mig en random
    }

    public void Three()
    {
        //Target, och yo gimme a random
    }

    public void Ladder()
    {
        //If 5 normals cats, pick a card from discard.
    }
}
