/**
 * A thread that listen after new messages from a server through a BufferedReader
 *
 * @author Maja Lund
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;

public class ServerReader implements Runnable{

    private Client client;
    private BufferedReader serverIn;

    /**
     *
     * @param serverIn the bufferedReader to listen to
     * @param client
     */
    public ServerReader(BufferedReader serverIn, Client client){
        this.client = client;
        this.serverIn = serverIn;
    }

    /**
     * Listens after new messages from server.
     */
    @Override
    public void run() {
        String message;
        try {
            while((message = serverIn.readLine()) != null){
                client.printMessage(message);
            }
            client.closeClient();
        }catch(SocketException s){
            client.closeClient();
            client.printMessage("You lost connection");
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
