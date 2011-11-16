package org.system;

import gui.FlasherGUI;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Scanner;

import org.adb.AdbUtility;
import org.logger.MyLogger;

public class AdbStatus  {
   private ProcessBuilder builder;
   private Process adb;
   private static String fsep = OS.getFileSeparator();

   private InputStream processInput;

   /**
    * Starts the shell 
    */
   public void start() throws IOException  {
	   if (OS.getName().equals("linux"))
		   builder = new ProcessBuilder(new File(System.getProperty("user.dir")+fsep+"x10flasher_lib"+fsep+"adb").getAbsolutePath(), "status-window");
	   else
		   builder = new ProcessBuilder(new File(System.getProperty("user.dir")+fsep+"x10flasher_lib"+fsep+"adb.exe").getAbsolutePath(), "status-window");
      adb = builder.start();
      processInput = adb.getInputStream();
      Scanner sc = new Scanner(processInput);
      while (sc.hasNextLine()) {
    	  String line = sc.nextLine();
    	  if (OS.getName().equals("linux")) {
	    	  if (line.contains("State: device")) {
	    		  if (!AdbUtility.isConnected()) { 
		    		  while (!AdbUtility.isConnected()) {
		    			  try {
		    				  Thread.sleep(1000);
		    			  }
		    			  catch (Exception e) {
		    			  }
		    		  }
	    		  }
	    		  MyLogger.getLogger().debug("Device connected, continuing with identification");
	    		  FlasherGUI.doIdent();
	    	  }
	    	  else if (line.contains("State: unknown")) FlasherGUI.doDisableIdent();
    	  }
      }
   }

   /**
    * Stop the shell;
    */
   public void stop()   {
      try   {
    	  processInput.close();
    	  adb.destroy();
      }
   catch(Exception ignore)  {}
   }

}
