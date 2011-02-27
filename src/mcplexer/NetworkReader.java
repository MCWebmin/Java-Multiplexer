/*
 *  Copyright 2010 Joe Stein.
 * 
 */

package mcplexer;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joe Stein
 */
public class NetworkReader extends StreamReader
{
   private int id;

   public NetworkReader(InputStream is, NetworkHandler h, int id)
   {
      super(is,h);
      this.id = id;
   }

   @Override
   public void kill()
   {
      try {
         keepRunning = false;
         ((NetworkHandler) handler).remove(id);
         reader.close();
      } catch (IOException ex) {
         Logger.getLogger(StreamReader.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}
