package Rewrite;

import java.util.ArrayList;

public class Main 
{
	//Create a new lobby
	private static ArrayList<Player> Lobby = new ArrayList<Player>();
	private Player host = Lobby.get(0);
	//When x players are in
	//Create game
	//Play
	Game g = new Game(Lobby);
}
