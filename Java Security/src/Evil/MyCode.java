package Evil;


import java.io.File;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class MyCode {
	
	public MyCode(){
					
	}
	
	public void endlessLoop(){
		while(true){}
	}
		
	public void fileConnection(){
		try
		{
			File file = new File("/asdf.txt");
			FileWriter fw = null;
			
			if( file.exists() )
				System.out.println("Datei existiert bereits.");
			else	
				System.out.println("Datei wird erstellt.");
			
			fw = new FileWriter( file.getPath() , true );
			
			PrintWriter pw = new PrintWriter( fw );
			pw.println("Etwas Text in einer Zeile");
			
			fw.flush();
			fw.close();
			
			pw.flush();
			pw.close();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
	}
	
	public void socketConnection(){
		try{
		     Socket socket = new Socket("http://google.at", 80);
		     PrintWriter out = new PrintWriter(socket.getOutputStream(), 
		                 true);
		     BufferedReader in = new BufferedReader(new InputStreamReader(
		                socket.getInputStream()));
		   } catch (Exception e) {
		     System.out.println(e);
		     System.exit(1);
		   }
	}
	
	public void overflow(){

	}
}
