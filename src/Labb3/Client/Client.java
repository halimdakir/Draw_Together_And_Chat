package Labb3.Client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    PrintWriter writer;
    public String localH;
    public void runClient() {
        try( Socket socket = new Socket(localH, 8000) ) {
            OutputStream output = socket.getOutputStream();
             writer = new PrintWriter(output, true);
             writer.println("I'm Connected");

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            while (true) {
                String line = reader.readLine();
                System.out.println(line);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
