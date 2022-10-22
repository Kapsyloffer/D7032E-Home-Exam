import java.util.*; 
import java.io.*; 
import java.net.*;
import java.util.concurrent.*;

public class ExplodingKittens 
{
    public ServerSocket aSocket;
	public static ArrayList<Card> deck = new ArrayList<Card>();
	public static ArrayList<Card> discard = new ArrayList<Card>();
	public static int numberOfTurnsToTake = 1; //attacked?
	public ArrayList<Player> players = new ArrayList<Player>();
	public int secondsToInterruptWithNope = 5;

	static HashMap<Card.CardType, Integer> maxCards = new HashMap<Card.CardType, Integer>();
	
	//Problemet är att det hårdkodas fast på olika ställen.
	public static void setCards(int p)
	{
		//TODO: Gör denna med JSON
		//Dessa dependar på spelare.
		maxCards.put(Card.CardType.ExplodingKitten, p-1);
		maxCards.put(Card.CardType.Defuse, 6-p);

		//Dessa är basically samma hela tiden, kan läsas in från fil.
		//Men hur gör vi med expansions?
		maxCards.put(Card.CardType.Attack, 4);
		maxCards.put(Card.CardType.Favor, 4);
		maxCards.put(Card.CardType.Nope, 5);
		maxCards.put(Card.CardType.Shuffle, 4);
		maxCards.put(Card.CardType.Skip, 4);
		maxCards.put(Card.CardType.SeeTheFuture, 5);
		maxCards.put(Card.CardType.HairyPotatoCat, 4);
		maxCards.put(Card.CardType.Cattermelon, 4);
		maxCards.put(Card.CardType.RainbowRalphingCat, 4);
		maxCards.put(Card.CardType.TacoCat, 4);
		maxCards.put(Card.CardType.OverweightBikiniCat, 4);
	}

	//OK
	public ExplodingKittens(String[] params) throws Exception 
	{
		if(params.length == 2) 
		{
			this.initGame(Integer.valueOf(params[0]).intValue(), Integer.valueOf(params[1]).intValue());
		} 
		else if(params.length == 1) 
		{
			client(params[0]);
		} 
		else 
		{
			//TODO: Prompt, vill du vara server eller client?
			//Client: Ange IP. -> Bam
			//Server: Hur många spelare?
			//Server: check if good. -> print port.
			
			System.out.println("Server syntax: java ExplodingKittens numPlayers numBots");
			System.out.println("Client syntax: IP");
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

	//OK
	public void initGame(int numPlayers, int numBots)
	{
		if(numPlayers + numBots > 5 || numPlayers + numBots < 2)
		{
			System.out.println("Incorrect number of players.");
			System.exit(0);
		}
		try 
		{
	        server(numPlayers, numBots);

			//Create the deck
			//TODO: Deckbuilder med for loop. Sätt typer i JSON maybe?
			//Skapa alla maxvärden per kort
			setCards(numPlayers + numBots);

			for(Map.Entry<Card.CardType, Integer> mC : maxCards.entrySet())
			{
				//T.ex om key Shuffle har 4 som value stoppar vi in 4 shufflekort.
				for(int i = 0; i < mC.getValue(); i++)
				{
					Card c = new Card(mC.getKey());
					deck.add(c);
				}
			}
			//Why shuffle twice?
			Collections.shuffle(deck);

			for(Player player : players) 
			{
				Card c = new Card(Card.CardType.Defuse);
				player.add(c);
				//Draw
				for(int i=0; i<7; i++) 
				{
					player.draw();
				}
			}

	        Random rnd = new Random();
	        game(rnd.nextInt(players.size()));
		} 
		catch(Exception e) 
		{
			System.out.println(e.getMessage());
		}
	}

	public void addToDiscardPile(Player currentPlayer, String card) throws Exception 
	{
		//After an interruptable card is played everyone has 5 seconds to play Nope
		int nopePlayed = checkNrNope();
		ExecutorService threadpool = Executors.newFixedThreadPool(players.size());
		for(Player p : players) 
		{
			p.sendMessage("Action: Player " + currentPlayer.playerID + " played " + card);
			//Checks if they may nope
			boolean canNope = false;
			for(Card c : p.hand)
			{
				if (c.getType() == Card.CardType.Nope)
				{
					canNope = true;
					break;
				}
			}
			if(canNope)
			{ //only give the option to interrupt to those who have a Nope card
				p.sendMessage("Press <Enter> to play Nope");
				Runnable task = new Runnable() 
				{
		        	@Override
		        	public void run() 
		        	{
	        			try 
	        			{
			        		String nextMessage = p.readMessage(true); //Read that is interrupted after secondsToInterruptWithNope
			        		if(!nextMessage.equals(" ") && p.hand.contains(Card.CardType.Nope)) 
			        		{
								for(Card c : p.hand)
								{
									if (c.getType() == Card.CardType.Nope)
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
		if(checkNrNope()>nopePlayed) 
		{
			for(Player notify: players)
			{
				notify.sendMessage("Play another Nope? (alternate between Nope and Yup)");
			}
			addToDiscardPile(currentPlayer, card);
		}
	}

	public int checkNrNope() 
	{
		int i=0;
		while(i<discard.size() && discard.get(i).getType() == Card.CardType.Nope) 
		{
			i++;	
		}
		return i;
	}

	public void game(int startPlayer) throws Exception 
	{
		Player currentPlayer = players.get(startPlayer);
		int playersLeft = players.size();
		do 
		{ //while playersLeft>1
			for(Player p : players) 
			{
				if(p == currentPlayer)
				{
					p.sendMessage("It is your turn");
				}
				else
				{
					p.sendMessage("It is now the turn of player " + currentPlayer.playerID);
				}
			}
			currentPlayer.SortHand();

			for(int i=0; i<numberOfTurnsToTake; i++) 
			{
				String otherPlayerIDs = "PlayerID: ";
				for(Player p : players) 
				{
					if(p.playerID != currentPlayer.playerID)
					{
						otherPlayerIDs += p.playerID + " ";
					}
				}

				String response = "";
				while(!response.equalsIgnoreCase("pass")) 
				{
					int turnsLeft = numberOfTurnsToTake-i;
					currentPlayer.sendMessage("\nYou have " + turnsLeft + ((turnsLeft>1)?" turns":" turn") + " to take");
					currentPlayer.sendMessage("Your hand: " + currentPlayer.hand);
					String yourOptions = "You have the following options:\n";
					Set<Card> handSet = new HashSet<Card>(currentPlayer.hand);

					//TODO: Fix whatever the fuck this is
					for(Card card : handSet) 
					{
						int count = Collections.frequency(currentPlayer.hand, card);
						//TODO: Lists options, som Attack, Attack, osv. Visar dock common cards.
						yourOptions += "\t" + card + "\n";
						
						//TODO: Fix this. Cause nu gäller det alla.
							if(count>=2)
							{
								yourOptions += "\tTwo " + card + " [target] (available targets: " + otherPlayerIDs + ") (Steal random card)\n";
							}
							if(count>=3)
							{
								yourOptions += "\tThree " + card + " [target] [Card Type] (available targets: " + otherPlayerIDs + ") (Name and pick a card)\n";
							}
					}  
					//We don't need to offer Nope as an option - it's only viable 5 seconds after another card is played and handled elsewhere
					currentPlayer.sendMessage(yourOptions);
					response = currentPlayer.readMessage(false);
					//TODO: CardActions.java or something. Can be made as switch case
					if(yourOptions.contains(response.replaceAll("\\d",""))) 
					{ 
						//remove targetID to match vs yourOptions
						if(response.equals("Pass")) 
						{ //Draw a card and end turn
							Card drawCard = deck.remove(0);
							if(drawCard.getType() == Card.CardType.ExplodingKitten) 
							{
								if(currentPlayer.hand.contains(Card.CardType.Defuse)) 
								{
									currentPlayer.hand.remove(Card.CardType.Defuse);
									currentPlayer.sendMessage("You defused the kitten. Where in the deck do you wish to place the ExplodingKitten? [0.." + (deck.size()-1) + "]");
									deck.add((Integer.valueOf(currentPlayer.readMessage(false))).intValue(), drawCard);
									for(Player p : players) 
									{
										p.sendMessage("Player " + currentPlayer.playerID + " successfully defused a kitten");
									}
								} 
								else 
								{
									//we discard them to the bottom of the pile, that way we don't end up with 
									//problems of Attack ending up as the last card
									discard.add(drawCard); 
									discard.addAll(currentPlayer.hand);

									currentPlayer.hand.clear();
									for(Player p : players) 
									{
										p.sendMessage("Player " + currentPlayer.playerID + " exploded");
									}
									currentPlayer.exploded = true;
									playersLeft--;
								}
							} 
							else 
							{
								currentPlayer.hand.add(drawCard);
								currentPlayer.sendMessage("You drew: " + drawCard);
							}
						} 
						else if(response.contains("Two")) 
						{ //played 2 of a kind - steal random card from target player
							String[] args = response.split(" ");
							
							//TODO: FIX
							/*for(int j = 0; j < 2; j++)
							{
								currentPlayer.hand.remove(Card.valueOf(args[1])); 
								discard.add(0, Card.valueOf(args[1]));
							}*/

							addToDiscardPile(currentPlayer, "Two of a kind against player " + args[2]);
							if(checkNrNope() % 2 == 0) 
							{
								Player target = players.get((Integer.valueOf(args[2])).intValue());
						        Random rnd = new Random();
						        Card aCard = target.hand.remove(rnd.nextInt(target.hand.size()-1));

						        currentPlayer.hand.add(aCard);
						        target.sendMessage("You gave " + aCard + " to player " + currentPlayer.playerID);
						        currentPlayer.sendMessage("You received " + aCard + " from player " + target.playerID);								
							}
						} 
						else if(response.contains("Three")) 
						{ //played 3 of a kind - name a card and force target player to hand one over if they have it
							String[] args = response.split(" ");

							//TODO: Fix
							/*for(int j = 0; j < 3; j++)
							{
								currentPlayer.hand.remove(Card.CardType.valueOf(args[1])); 
								discard.add(0, Card.CardType.valueOf(args[1]));
							}

							addToDiscardPile(currentPlayer, "Three of a kind against player " + args[2]);
							if(checkNrNope() % 2 == 0) {
								Player target = players.get((Integer.valueOf(args[2])).intValue());
								Card aCard = Card.valueOf(args[3]);
								if(target.hand.contains(aCard)) {
									target.hand.remove(aCard);
									currentPlayer.hand.add(aCard);
						        	target.sendMessage("Player " + currentPlayer.playerID + " stole " + aCard);
						        	currentPlayer.sendMessage("You received " + aCard + " from player " + target.playerID);										
								} 
								else 
								{
									currentPlayer.sendMessage("The player did not have any " + aCard);
								}								
							}*/
						} 
						else if(response.equals("Attack")) 
						{
							int turnsToTake = 0;
							if(discard.size()>0 && discard.get(0).equals(Card.CardType.Attack)) 
							{
								turnsToTake = numberOfTurnsToTake + 2;	
							} 
							else 
							{
								turnsToTake = 2;
							}
							currentPlayer.RemoveFromHand(Card.CardType.Attack);
							addToDiscardPile(currentPlayer, "Attack");

							if(checkNrNope() % 2 == 0) 
							{
								numberOfTurnsToTake = turnsToTake; //do not modify if Nope
								i = numberOfTurnsToTake; //ugly hack - make sure we also exit the for loop
								response="Pass"; //part of the ugly hack
								break; //exit the while-loop and move to the next player - do not draw.								
							}
						} 
						else if(response.contains("Favor")) 
						{
							//This adds to the pile too
							currentPlayer.RemoveFromHand(Card.CardType.Favor);

							String[] args = response.split(" ");
							Player target = players.get((Integer.valueOf(args[1])).intValue());

							addToDiscardPile(currentPlayer, "Favor player " + target.playerID);
							if(checkNrNope() % 2 == 0) 
							{
								boolean viableOption = false;
								if(target.hand.size()==0)
								{
									viableOption=true; //special case - target has no cards to give
								}

								/*while(!viableOption) 
								{
									target.sendMessage("Your hand: " + target.hand);
									target.sendMessage("Give a card to Player " + currentPlayer.playerID);
									String tres = target.readMessage(false);

									if(target.hand.contains(Card.valueOf(tres))) 
									{
										viableOption = true;
										currentPlayer.hand.add(Card.valueOf(tres));
										target.hand.remove(Card.valueOf(tres));
									} 
									else 
									{
										target.sendMessage("Not a viable option, try again");
									}
								}	*/						
							}
						} 
						else if(response.equals("Shuffle")) 
						{
							currentPlayer.RemoveFromHand(Card.CardType.Shuffle);
							addToDiscardPile(currentPlayer, "Shuffle");
							if(checkNrNope() % 2 == 0) 
							{
								Collections.shuffle(deck);
							}
						} 
						else if(response.equals("Skip")) 
						{
							currentPlayer.RemoveFromHand(Card.CardType.Skip);
							addToDiscardPile(currentPlayer, "Skip");
							if(checkNrNope() % 2 == 0) 
							{
								break; //Exit the while loop
							}
						} 
						else if(response.equals("SeeTheFuture")) 
						{
							currentPlayer.RemoveFromHand(Card.CardType.Favor);
							addToDiscardPile(currentPlayer, "SeeTheFuture");

							if(checkNrNope() % 2 == 0) 
							{
								currentPlayer.sendMessage("The top 3 cards are: " + deck.get(0) + " " + deck.get(1) + " " + deck.get(2));
							}							
						} 
					} 
					else 
					{
						currentPlayer.sendMessage("Not a viable option, try again");
					}
					if(i==(numberOfTurnsToTake-1))
					{
						numberOfTurnsToTake=1; //We have served all of our turns, reset it for the next player
					}
				}
			}
			do 
			{ 
				//next player that is still in the game
				int nextID=((currentPlayer.playerID+1)<players.size()?(currentPlayer.playerID)+1:0);
				currentPlayer = players.get(nextID);
			} 
			while(currentPlayer.exploded && playersLeft>1);
		} 
		while(playersLeft>1);

		//Declare winner.
		Player winner = currentPlayer;

		for(Player notify: players)
		{
			winner = (!notify.exploded?notify:winner);
			notify.sendMessage("Player " + winner.playerID + " has won the game");
		}
		System.exit(0);
	}

	//Networking bullshit

    public void client(String ipAddress) throws Exception 
    {
        //Connect to server
        Socket aSocket = new Socket(ipAddress, 2048);
        ObjectOutputStream outToServer = new ObjectOutputStream(aSocket.getOutputStream());
        ObjectInputStream inFromServer = new ObjectInputStream(aSocket.getInputStream());
		//Stänger för att preventa memory leak.
		aSocket.close();
        //Get the hand of apples from server
        ExecutorService threadpool = Executors.newFixedThreadPool(1);
        Runnable receive = new Runnable() 
        {
        	@Override
        	public void run() 
        	{
    			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));	
        		while(true) 
        		{
        			try 
        			{
		        		String nextMessage = (String) inFromServer.readObject();
	    	    		System.out.println(nextMessage);   		
	    	    		//options (your turn), Give (Opponent played Favor), Where (You defused an exploding kitten)
	    	    		if(nextMessage.contains("options") || nextMessage.contains("Give") || nextMessage.contains("Where"))
	    	    		{ 
	    	    			outToServer.writeObject(br.readLine());
	    	    		} 
	    	    		else if(nextMessage.contains("<Enter>")) 
	    	    		{ 
	    	    			//Press <Enter> to play Nope and Interrupt
	    				    int millisecondsWaited = 0;
	    				    while(!br.ready() && millisecondsWaited<(secondsToInterruptWithNope*1000)) 
	    				    {
	    				    	Thread.sleep(200);
	    				    	millisecondsWaited += 200;
	    				    }
	    				    if(br.ready()) 
	    				    {
	    				    	outToServer.writeObject(br.readLine());
	    				    }	    				    
	    				    else
	    				    {
	    				    	outToServer.writeObject(" ");
	    				    }
	    	    		}
        			} 
        			catch(Exception e) 
        			{
        				System.exit(0);
        			}
        		}
        	}
        };

        threadpool.execute(receive);
    }

    public void server(int numberPlayers, int numberOfBots) throws Exception 
    {
    	//add this instance as a player
        players.add(new Player(0, false, null, null, null)); 
        //Open for connections if there are online players
        //add a bot foreach number of bots
        for(int i=0; i<numberOfBots; i++) 
        {
            players.add(new Player(i+1, true, null, null, null));  
        }
        if(numberPlayers>1)
        {
            aSocket = new ServerSocket(2048);
        }
        for(int i=numberOfBots+1; i<numberPlayers+numberOfBots; i++) 
        {
            Socket connectionSocket = aSocket.accept();
            ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
            ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
            //add an online client
            players.add(new Player(i, false, connectionSocket, inFromClient, outToClient)); 
            System.out.println("Connected to player " + i);
            outToClient.writeObject("You connected to the server as player " + i + "\n");
        }    
    }
}