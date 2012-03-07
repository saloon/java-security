package security;

import java.util.Vector;

public class Debugger {

	Vector<String> messages = new Vector<String>();
	private static Debugger instance = null;
	private static Boolean debug = true;
	
	public static Debugger getInstance(){
		if(instance == null)
			instance = new Debugger();
		return instance;
	}
	
	private Debugger(){}
	
	public void debug(Boolean a){
		debug = a;
	}
	
	public void print(String s){
		if(debug)
			System.out.println("------------ " + s);
	}
	
	public void print(Exception e){
		if(debug)
			System.out.println("------------ " + e.toString());
	}
	
	public void err(String s){
		if(debug)
			System.err.print(s);
	}
	
}
