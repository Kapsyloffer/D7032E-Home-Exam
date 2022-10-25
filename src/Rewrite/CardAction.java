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
                break;
            }
        }
        if(!hasDef)
        {
            Game.getCurrentPlayer().KABOOM();
        }
    }

    private void Defuse()
    {
        Game.announce("The bomb has been defused!");
        Game.whisper("Where do you wish to place the bomb? 0, 1, 2, ...", Game.getCurrentPlayer());
        boolean done = false;
        while(!done)
        {
            try
            {   
                int placement = Integer.parseInt(Game.getCurrentPlayer().readMessage(false));
                if(placement <= Deck.getDeck().size()-1)
                {
                    Deck.Insert(new Card(CardType.ExplodingKitten), placement);
                }
                done = true;
                Game.SwitchPlayer(0, false);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    private void Attack()
    {
        //Pass skip -> next player får 2 turns
        //Attack ger any remaining turns + 2
        Game.SwitchPlayer(2, true);
    }

    private void Favor()
    {
        //Target a player
        Target();
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

    //Test this later
    private void SeeTheFuture()
    {
        ArrayList<Card> d = Deck.getDeck();
        for(int i = 0; i<3; i++)
        {
            //1. Cardname
            //2. Cardname
            //3. Cardname
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
        //Target, och yo gimme a CARDNAME

    }

    private void Target()
    {
        Game.whisper("\nPossible targets;", Game.getCurrentPlayer());
        for(Player p : Game.getPlayers())
        {
            Game.whisper("\nPlayerID:" + p.getID() + " ", Game.getCurrentPlayer());
        }
    }
}
