package Cardstuff;
import java.util.*;

import CardPiles.Deck;
import CardPiles.Discard;
import Cardstuff.Card.*;
import Game.Game;
import Player.Player;
public class CardAction 
{
    private boolean Nopes = false;
    public CardAction(CardType c) 
    {
        Game.announce(Game.getCurrentPlayer().getID() + " played: " + c.toString());
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
        Game.announce("SURPRISE ATTACK");
        //Pass skip -> next player får 2 turns
        //Attack ger any remaining turns + 2
        Game.SwitchPlayer(2, true);
    }

    //TODO: Test, but I guess this works.
    private void Favor()
    {
        //Target a player
        Player p =  Game.getPlayers().get(Target());
        boolean notPicked = true;
        while(notPicked)
        {
            Game.whisper("Do me a flava willya, pick a card", p);
            //foreach player left -> pick one
            //The picked one gets a prompt
            try
            {
                String pick = p.readMessage(false).toLowerCase();
                for(Card c : p.getHand())
                {
                    //if the picked card is in the hand, give it to current player. ELSE try again.
                    if(c.getType().toString().toLowerCase() == pick)
                    {
                        p.RemoveFromHand(c.getType());
                        Game.getCurrentPlayer().add(c);
                    }
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
            //Yo gimme a card
            //They select a card and give
            //Thx
        }
    }

    private void Nope()
    {
        //count
        int nopes = 0;
        for(Card c : Discard.getDiscard())
        {
            if(c.getType() != CardType.Nope)
            {
                break;
            }
            else
            {
                nopes++;
            }
        }
        if(nopes%2 == 1)
        {
            Game.announce("NOPE!");
            Nopes = true;
        }
        else
        {
            Game.announce("YUP!");
            Nopes = false;
        }
    }
    
    public boolean getNopes()
    {
        return Nopes;
    }

    //OK
    private void Shuffle()
    {
        Deck.shuffle();
        Game.announce("The deck has been shuffled!");
    }


    //OK
    private void Skip()
    {
        //Pass and skip draw is true uwu
        Game.Pass(true);
        Game.announce("Skipped");
    }

    //OK
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
        Game.announce("The future has been seen.");
    }

    //OK
    public void Two()
    {
        //Target, och yo ge mig en random
        int target = Target();
        Random r = new Random();
        try
        {
            Game.getPlayers().get(target).getHand().remove(r.nextInt(Game.getPlayers().get(target).getHand().size()));
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //OK
    public void Three()
    {
        //Target, och yo gimme a CARDNAME
        int target = Target();
        boolean t = true;
        while(t)
        {
            try
            {
                Game.whisper("\nName a card to take from Target: ", Game.getCurrentPlayer());
                String prompt = Game.getCurrentPlayer().readMessage(false);
                for(Card c : Game.getPlayers().get(target).getHand())
                {
                    if(c.getType().toString().toLowerCase() == prompt)
                    {
                        Game.getCurrentPlayer().add(c);
                        Game.getPlayers().get(target).RemoveFromHand(c.getType());
                    }
                }
                t = false;
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    //TODO: TEST
    private static int Target()
    {
        Game.whisper("\nPossible targets;", Game.getCurrentPlayer());
        for(Player p : Game.getPlayers())
        {
            if(p != Game.getCurrentPlayer())
            {
            Game.whisper("\nPlayerID:" + p.getID() + "Index:(input this part) " + Game.getPlayers().indexOf(p), Game.getCurrentPlayer());
            }
        }
        int input;
        while(true)
        {
            try
            {
                Game.whisper("\nPlease input target:", Game.getCurrentPlayer());
                input = Integer.parseInt(Game.getCurrentPlayer().readMessage(false));
                if(input < Game.getPlayers().size() && input != Game.getPlayers().indexOf(Game.getCurrentPlayer()))
                {
                    return input;
                }
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
            
        }
    }
}
