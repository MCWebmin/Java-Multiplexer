package mcplexer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joe Stein
 */
public class NetworkHandler implements Handler
{
   private final int MAX_CONNECTIONS = 1;
   private int connectionCount = 0;
   private StreamReader[] clients = new StreamReader[MAX_CONNECTIONS];
   private ServerSocket servSock;

   public void write(String s)
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void read(String s)
   {
      System.out.println("Received " + s);
   }

   public void remove(int id)
   {
      connectionCount--;
      clients[id] = null;
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
               if (connectionCount >= MAX_CONNECTIONS)
               {
                  // send reject
                  client.getOutputStream().write("Maximum clients connected".getBytes());
               } else
               {
                  // find empty spot
                  int spot = 0;
                  while (clients[spot] != null && spot < clients.length)
                  {
                     spot++;
                  }
                  clients[spot] = new NetworkReader(client.getInputStream(),NetworkHandler.this,spot);
                  connectionCount++;

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
