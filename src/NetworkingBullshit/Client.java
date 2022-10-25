package NetworkingBullshit;
import java.io.*; 
import java.net.*;
import java.util.concurrent.*;

public class Client 
{
    	//Networking bullshit
        public Client(String ip) throws Exception
        {
            ClientRun(ip);
        }
        public void ClientRun(String ipAddress) throws Exception 
        {
            //Connect to server
            Socket aSocket = new Socket(ipAddress, 2048);
            ObjectOutputStream outToServer = new ObjectOutputStream(aSocket.getOutputStream());
            ObjectInputStream inFromServer = new ObjectInputStream(aSocket.getInputStream());
            //Stänger för att preventa memory leak.
            //aSocket.close();
            //lmao bruh
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
                                while(!br.ready() && millisecondsWaited<(5*1000)) 
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
}
