package mcplexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joe Stein
 */
public class StreamReader implements Runnable
{
   protected BufferedReader reader;
   protected boolean keepRunning = true;
   protected Handler handler;

   public StreamReader(InputStream is, Handler h)
   {
      System.out.println("init serverreader...");
      handler = h;
      reader = new BufferedReader(new InputStreamReader(is));
   }

   public void run()
   {
      while (keepRunning)
      {
         try {
            String in = reader.readLine();
            handler.read(in);
         } catch (IOException ex) {
            Logger.getLogger(StreamReader.class.getName()).log(Level.SEVERE, null, ex);
            kill();
         }
      }
   }

   public void kill()
   {
      try {
         keepRunning = false;
         reader.close();
      } catch (IOException ex) {
         Logger.getLogger(StreamReader.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

}
