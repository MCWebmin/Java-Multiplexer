package mcplexer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joe Stein
 */
public class NetworkHandler implements Handler
{
   private final int MAX_CONNECTIONS = 3;
   private final int THIS_PORT;
   private int connectionCount = 0;
   private int idAt = 0;
   private HashMap clients = new HashMap(MAX_CONNECTIONS);
   private ServerSocket servSock;
   private final Server server;

   public NetworkHandler(int port, Server relay)
   {
      server = relay;
      THIS_PORT = port;
      init();
   }

   private void init()
   {
      try {
         servSock = new ServerSocket(THIS_PORT);
         Thread accept = new Thread(new Connector());
         accept.start();
      } catch (IOException ex) {
         Logger.getLogger(NetworkHandler.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   public void write(String msg)
   {
      Iterator<NetworkReader> clientIterator = clients.values().iterator();
      while (clientIterator.hasNext())
      {
         clientIterator.next().write(msg);
      }
      //throw new UnsupportedOperationException("Not supported yet.");
   }

   public void read(String s)
   {
      System.out.println("Received " + s);
      server.pipeToProcess(s);
   }

   public void remove(int id)
   {
      connectionCount--;
      clients.remove(id);
   }

   private class Connector implements Runnable
   {
      private boolean keepRunning = true;

      public void run()
      {
         while (keepRunning)
         {
            try {
               Socket client = servSock.accept();
               System.out.println("Socket established");
               if (connectionCount >= MAX_CONNECTIONS)
               {
                  // send reject
                  client.getOutputStream().write("Maximum clients connected".getBytes());
                  client.close();
               } else
               {
                  int key = idAt;
                  try {
                     NetworkReader nread = new NetworkReader(client,NetworkHandler.this,idAt);
                     clients.put(key, nread);
                     Thread t = new Thread(nread);
                     t.start();
                     connectionCount++;
                     idAt++;
                  } catch (IOException ioe)
                  {
                    System.out.println("Client " + idAt + " died at initialization!");
                  }
               }
            } catch (IOException ex) {
               Logger.getLogger(NetworkHandler.class.getName()).log(Level.SEVERE, null, ex);
               kill();
            }
         }
      }

      protected void kill()
      {
         keepRunning = false;
      }
   }
}
