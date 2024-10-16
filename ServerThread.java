import java.io.*;
import java.net.*;

public class ServerThread extends Thread {
    private Server server;
    private BufferedReader in;
    private PrintWriter out;
    private InputStream inputStream = null;
    public int playerNum;

    private final int DELAY = 50;

    public ServerThread(Socket socket, Server server, int playerNum) {
        this.server = server;
        this.playerNum = playerNum;

        try {
            in = new BufferedReader(new InputStreamReader(inputStream = socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // tell the client what player number they are
        out.println("player " + playerNum);
        start();
    }

    public void run() {
        while (true) {
            cycle();
        }
    }

    public void cycle() {
        try {
            String line;
            String message = "";
            while (inputStream.available() > 0 && (line = in.readLine()) != null && line != "") {
                message = line;
            }

            String[] tokens = message.split(" ");

            // when the client joins, send that to the server
            if (tokens[0].equals("joined")) {
                System.out.println("joined");
                server.updateCount();
            }

            else if (tokens[0].equals("start")) {
                System.out.println("received start");
                send(message);
            }

            // if client moved, send that to all other players
            else if (tokens[0].equals("moved")) {
                double x = Double.parseDouble(tokens[1]);
                double y = Double.parseDouble(tokens[2]);
                double angle = Double.parseDouble(tokens[3]);
                server.sendToOthers("moved " + x + " " + y + " " + angle + " " + playerNum, playerNum);
            }

            else if (tokens[0].equals("finished")) {
                server.sendToAll("won " + playerNum);
            }

            else if (tokens[0].equals("restart")) {
                server.sendToAll("restart");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String message) {

        out.println(message);
        // System.out.println("ServerThread " + playerNum + " sent " + message);
    }
}
