package test.ex04.pyrmont;

import test.util.HttpUtil;

public class SendDemo {
	public static void main(String[] args) {
		
		for (int i = 0; i <= 10; i++) {
			new Thread(new SendTest()).start();;
		}
	    	
	        
		
	}

}
