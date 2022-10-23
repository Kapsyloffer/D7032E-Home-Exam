package Rewrite;
import java.util.*; 
import java.io.*; 
import java.net.*;
import java.util.concurrent.*;
import Rewrite.Card.*;

public class ExplodingKittens 
{
    public ServerSocket aSocket;
	public static int numberOfTurnsToTake = 1; //attacked?
	public ArrayList<Player> players = new ArrayList<Player>();
	public int secondsToInterruptWithNope = 5;
	private Deck deck;

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
			this.initGame(3, 1);
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
	        //server(numPlayers, numBots);

			//Create the deck
			//TODO: Deckbuilder med for loop. Sätt typer i JSON maybe?
			//Skapa alla maxvärden per kort
			
			//Deck.InitDeck();
			//Initiates a deck
			new Deck(5).initDeck();

	        Random rnd = new Random();
	        game(rnd.nextInt(players.size()));
		} 
		catch(Exception e) 
		{
			System.out.println(e.getMessage());
		}
	}
	
//TODO: FUck this in the ass. Stupid ass.
	public void addToDiscardPile(Player currentPlayer, String card) throws Exception 
	{
		//After an interruptable card is played everyone has 5 seconds to play Nope
		int nopePlayed = Discard.Nopes();
		ExecutorService threadpool = Executors.newFixedThreadPool(players.size());
		for(Player p : players) 
		{
			p.sendMessage("Action: Player " + currentPlayer.playerID + " played " + card);
			//Checks if they may nope
			boolean canNope = false;
			for(Card c : p.hand)
			{
				if (c.getType() == CardType.Nope)
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
	        			}*/
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
	}

	public void game(int startPlayer) throws Exception 
	{
		Player currentPlayer = players.get(startPlayer);
		int playersLeft = players.size();

		//TODO: REWRITE THIS ENTIRE THING
		//So what does it do?
		//While players left > 1, keep playing. OK
		//Whose turn is it?
		//The player whose turn it is gets to see their possible plays
		//The actions are made here. Bad.
		//When passed, check if another player is still in the game.
		//If no players, declare winner.
		do
		{
			
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