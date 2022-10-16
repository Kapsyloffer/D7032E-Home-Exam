package Rewrite;
import java.util.*; 
import java.io.*; 
import java.net.*;
import java.util.concurrent.*;

public class Game 
{
	ArrayList<Player> PlayerList = new ArrayList<Player>();
	
	void Target(Player src)
	{
		System.out.println("Who do you wish to target? \n");
		for(Player p : PlayerList)
		{
			System.out.println(p.TargetPrint() + "\n");
		}
		//Get input of Value
		boolean resolved = false;
		while (!resolved)
		{
			
		}
	}
}
