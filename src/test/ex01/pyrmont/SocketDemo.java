package test.ex01.pyrmont;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketDemo {
	public static void main(String[] args) {
		try {
			Socket socket=new Socket("127.0.0.1",8080);
			OutputStream os=socket.getOutputStream();
			boolean autoflush=true;
			PrintWriter out=new PrintWriter(socket.getOutputStream(),autoflush);
			BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

}
