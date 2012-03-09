package security;

import java.util.ArrayList;

public class Logger {

	ArrayList<String> log = new ArrayList<String>();
	private static Logger instance = null;
	
	private static Boolean debug = true;
	private static Boolean stdOut = true;
	private static Boolean fileOut = true;
	
	public static Logger getInstance(){
		if(instance == null)
			instance = new Logger();
		return instance;
	}
	
	private Logger(){}
	
	public void setDebug(Boolean a){
		this.debug = a;
	}
	public void setStdOut(Boolean a){
		this.stdOut = a;
	}
	public void setFileOut(Boolean a){
		this.fileOut = a;
	}
	
	public void info(String s){
		if(this.debug){
			if(this.stdOut){
				System.out.println("[INFO] " + s);
			}
			if(this.fileOut){
				this.log.add("[INFO] " + s);
			}
		}
	}
	
	public void debug(String s){
		if(this.debug){
			if(this.stdOut){
				System.err.print("[DEBUG] " + s);
			}
			if(this.fileOut){
				this.log.add("[DEBUG] " + s);
			}
		}
	}
	
	public void error(String s){
		if(this.debug){
			if(this.stdOut){
				System.err.print("[ERROR] " + s + "\n");
			}
			if(this.fileOut){
				this.log.add("[ERROR] " + s);
			}
		}
	}

}
