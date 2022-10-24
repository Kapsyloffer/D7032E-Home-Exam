package Rewrite;

public class ExplodingKittens 
{
	public int secondsToInterruptWithNope = 5;

	//OK
	public ExplodingKittens(String[] params) throws Exception 
	{
		if(params.length == 2) 
		{
			//Game.initGame(Integer.valueOf(params[0]).intValue(), Integer.valueOf(params[1]).intValue());
		} 
		else if(params.length == 1) 
		{
			new Client(params[0]);
		} 
		else 
		{
			Game.initGame(3, 1);
			//TODO: Prompt, vill du vara server eller client?
			//Client: Ange IP. -> Bam
			//Server: Hur många spelare?
			//Server: check if good. -> print port.
			//System.out.println("Server syntax: java ExplodingKittens numPlayers numBots");
			//System.out.println("Client syntax: IP");
		}
	}

	//OK
	public static void main(String argv[]) 
	{
		try 
		{
			new ExplodingKittens(argv);
		} 
		catch(Exception e) 
		{

		}
	}
//TODO: FUck this in the ass. Stupid ass.
	/*public void addToDiscardPile(Player currentPlayer, String card) throws Exception 
	{
		//After an interruptable card is played everyone has 5 seconds to play Nope
		int nopePlayed = Discard.Nopes();
		ExecutorService threadpool = Executors.newFixedThreadPool(players.size());
		for(Player p : players) 
		{
			p.sendMessage("Action: Player " + currentPlayer.playerID + " played " + card);
			
			if(p.HasNope())
			{ //only give the option to interrupt to those who have a Nope card
				p.sendMessage("Press <Enter> to play Nope");
				Runnable task = new Runnable() 
				{
		        	@Override
		        	public void run() 
		        	{
	        			/*try 
	        			{
			        		String nextMessage = p.readMessage(true); //Read that is interrupted after secondsToInterruptWithNope
			        		//TODO: Rewrite this
							if(!nextMessage.equals(" ") && p.hand.contains(CardType.Nope)) 
			        		{
								for(Card c : p.hand)
								{
									if (c.getType() == CardType.Nope)
									{
										discard.add(c);
										currentPlayer.hand.remove(c);
										break;
									}
								}
		    	    			//TODO: He dessa i en funktion?
		    	    			for(Player notify: players)
		    	    			{
		    	    				notify.sendMessage("Player " + p.playerID + " played Nope");
		    	    			}
			        		}
	        			} catch(Exception e) {
	        				System.out.println("addToDiscardPile: " +e.getMessage());
	        			}
	        		}
	        	};
            	threadpool.execute(task);
			}
		}
		threadpool.awaitTermination((secondsToInterruptWithNope*1000)+500, TimeUnit.MILLISECONDS); //add an additional delay to avoid concurrancy problems with the ObjectInputStream
		for(Player notify: players)
		{
			notify.sendMessage("The timewindow to play Nope passed");
		}
		if(Discard.Nopes()>nopePlayed) 
		{
			for(Player notify: players)
			{
				notify.sendMessage("Play another Nope? (alternate between Nope and Yup)");
			}
			addToDiscardPile(currentPlayer, card);
		}
	}*/
 
}