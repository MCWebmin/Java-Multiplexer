package mcplexer;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Handles I/O to and from the server.
 * @author Joe Stein
 */
public class ServerHandler implements Handler
{
   private Process serverProcess;
   private StreamReader serverReader;
   private PrintWriter writer;

   public ServerHandler(Process sp)
   {
      serverProcess = sp;
      serverReader = new StreamReader(sp.getInputStream(),this);
      serverReader = new StreamReader(sp.getErrorStream(),this);
      Thread sr = new Thread(serverReader);
      sr.start();
      writer = new PrintWriter(new OutputStreamWriter(serverProcess.getOutputStream()), true);
   }

   public void write(String s)
   {
      writer.println(s);
   }

   public void read(String s)
   {
      // TODO feed to server which will route to network handler
      System.out.println(s);
   }
}
