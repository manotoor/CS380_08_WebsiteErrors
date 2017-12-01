//Author Mano Toor

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
	public static void main(String[] args) throws Exception {
		// start server listen on port 8080
		try (ServerSocket serverSocket = new ServerSocket(8080)) {
			System.out.println("Web server has been started.");
			while (true) {
				//Accept anyone trying to connect
				Socket socket = serverSocket.accept();
				//Create a server thread
				Runnable server = () -> {
					try {
						//basic streams for server
						OutputStream os = socket.getOutputStream();
						PrintWriter out = new PrintWriter(os);
						InputStream is = socket.getInputStream();
						InputStreamReader isr = new InputStreamReader(is);
						BufferedReader br = new BufferedReader(isr);
						//get the url
						String url = br.readLine();
						String[] urlExcludeSpace = url.split(" ");
						//get path for file
						String path = "www" + urlExcludeSpace[1];
						File fileInPath = new File(path);
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
				Thread threadServer = new Thread(server);
				threadServer.start();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
