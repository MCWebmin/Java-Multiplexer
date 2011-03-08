/*
 *  Copyright 2010 Joe Stein.
 * 
 */

package mcplexer;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joe Stein
 */
public class NetworkReader extends StreamReader
{
   private int id;
   private Socket socket;

   public NetworkReader(Socket sock, NetworkHandler h, int id) throws IOException
   {
      super(sock.getInputStream(),h);
      socket = sock;
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

   public void write(String message)
   {
      try {
         socket.getOutputStream().write((message + "\n").getBytes());
      } catch (IOException ex) {
         Logger.getLogger(NetworkReader.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
}
