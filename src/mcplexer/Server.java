package mcplexer;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joe Stein
 */
public class Server
{
   private Process serverProcess;
   private ServerHandler serverHandler;
   private NetworkHandler networkHandler;

   public Server(int maxMem, int minMem)
   {
      // stdin command reader
      // Thread t = new Thread(new CommandHandler());
      // t.start();
      try {
         File f = new File("resources/minecraft_server.jar");
         System.out.println(f.exists());
         System.out.println(f.getAbsolutePath());
         serverProcess = Runtime.getRuntime().exec("java -jar -Xmx" + maxMem + "M -Xms" + minMem + "M resources/minecraft_server.jar nogui");
         networkHandler = new NetworkHandler(9503,this);
         serverHandler = new ServerHandler(serverProcess,this);
         /* autokill
         try {
            Thread.sleep(3000);
         } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
         }
         serverProcess.destroy(); */
      
      } catch (IOException ex) {
         Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   protected void pipeToNetwork(String message)
   {
      networkHandler.write(message);
   }

   protected void pipeToProcess(String message)
   {
      serverHandler.write(message);
   }

   private class CommandHandler implements Runnable
   {
      private boolean keepRunning = true;

      public void run()
      {
         Scanner scan = new Scanner(System.in);
         while (keepRunning)
         {
            String input = scan.nextLine();
            if (input.equals("kill"))
            {
               serverProcess.destroy();
               keepRunning = false;
            }
            serverHandler.write(input);
         }
      }

   }
}
