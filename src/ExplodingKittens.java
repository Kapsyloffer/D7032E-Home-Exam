import NetworkingBullshit.Client;
import Game.Game;

public class ExplodingKittens 
{

	//OK
	public ExplodingKittens(String[] params) throws Exception 
	{
		if(params.length == 2) 
		{
			Game.initGame(Integer.valueOf(params[0]).intValue(), Integer.valueOf(params[1]).intValue());
		} 
		else if(params.length == 1) 
		{
			new Client(params[0]);
		} 
		else 
		{
			//Game.initGame(3, 1);
			//Game.announce("PEEPEE");
			//TODO: Prompt, vill du vara server eller client?
			//Client: Ange IP. -> Bam
			//Server: Hur många spelare?
			//Server: check if good. -> print port.
			//Game.announce("PEEPEE");
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
}