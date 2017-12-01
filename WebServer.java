//Author Mano Toor & Eric Kannampuzha

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

						//we found hello.html
						if(fileInPath.exists()){
							//Read hello.html display 200 message that its okay
							BufferedReader fileBufferedReader = new BufferedReader(new FileReader(fileInPath));
							out.println("HTTP/1.1 200 OK");
							out.println("Content-type: text/html");
							out.println("Content-length: " + fileInPath.length());
							//tell server what was accessed
							System.out.println("Client accessed \"" + fileInPath + "\".");
							String readFile = fileBufferedReader.readLine();
							//Print contents of hello.html
							while(readFile != null){
								out.println(readFile + "\n");
								readFile = fileBufferedReader.readLine();
							}
							//close file buffer and print writer
							fileBufferedReader.close();
							out.close();
							//diplay 404 message
						}else if(!fileInPath.equals("www/hello.html")){
							File error = new File("www/404.html");
							//Print 404 error message
							BufferedReader errorReader = new BufferedReader(new FileReader(error));
							out.println("HTTP:/1.1 404 Not Found");
							out.println("Content-type: text/html");
							out.println("Content-length: " + error.length());
							
							//tell java about the error
							System.out.println("Client tried accessing \"" + fileInPath + "\". Displaying 404 page.");
							
							//Print 404 to browser
							String errorLine = errorReader.readLine();
							while (errorLine != null) {
								out.println(errorLine + "\n");
								errorLine = errorReader.readLine();
							}
							// close reader and out
							errorReader.close();
							out.close();
						}else{
							out.println("500 Error.");
							System.out.println("Something went wrong.");
						}

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
