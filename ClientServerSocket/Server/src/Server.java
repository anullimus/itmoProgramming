import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException{
        ServerSocket server;
        Socket socket;

        //port can be busy, so we need to catch the Exception
        server = new ServerSocket(8000);
        System.out.println("Server started!");

        socket = server.accept();
        System.out.println("Client connected");;

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("hi to Amir's server");
        writer.newLine();
        writer.flush();
        writer.close();
        socket.close();
        server.close();
    }
}
