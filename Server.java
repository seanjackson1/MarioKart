import java.io.*;
import java.net.*;
import java.util.*;

public class Server
{

  // number of players currently joined
  private int countPlayers;
  private int numPlayers;

  // arraylist of the serverthreads
  private ArrayList<ServerThread> threads = new ArrayList<ServerThread>();
  
  public Server(int n)
  {
    numPlayers = n;
    countPlayers = 0;
    try
    {
      ServerSocket server = new ServerSocket(9000);
      for(int i = 0; i < n; i ++) {
        threads.add(new ServerThread(server.accept(), this, i));
      }
    }
    catch(IOException e)
    {
      throw new RuntimeException(e);
    }

    new Thread() {
      public void run() {
        while(true) {
          if(countPlayers == n) {
            break;
          }
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {
            System.out.println("server error");
          }
        }
        System.out.println("starting");
        for(int i = 0; i < n; i ++) {
          threads.get(i).send("start " + n + " " + (int)(1090 + 25*i) + " " + (int)430);
        }
      }
    }.start();
  }

  public void sendToOthers(String message, int playerNum) {
    for(ServerThread thread : threads) {
      if(thread.playerNum != playerNum) {
        thread.send(message);
      }
    }
  }
  
  public void sendToAll(String message)
  {
    for(ServerThread thread : threads) {
      thread.send(message);
    }
  }

  public void updateCount() {
    countPlayers += 1;
    System.out.println("number of players: " + countPlayers);
  }

  public int getNumPlayers() {
    return numPlayers;
  }
}