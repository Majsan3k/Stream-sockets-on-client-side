/**
 *
 * A chatt client with GUI that communicates over socket.
 *
 * @author Maja Lund
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Client extends JFrame{

    private Socket socket;
    private PrintWriter outPrint;
    private BufferedReader serverIn;
    private JTextArea messageView;
    private JTextField writeMessageField;

    /**
     * Creates new Client GUI.
     *
     * @param host socket host
     * @param port socket port
     */
    public Client(String host, int port){

        setTitle("CONNECTED TO: " + host + " - ON PORT: " + port);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 200));
        setLayout(new BorderLayout());

        writeMessageField = new JTextField();
        writeMessageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    outPrint.println(writeMessageField.getText());
                    if(writeMessageField.getText().equals("exit")) {
                        outPrint.close();
                        System.exit(1);
                    }
                    writeMessageField.setText("");
                }
            }
        });

        messageView = new JTextArea();
        messageView.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(writeMessageField, BorderLayout.NORTH);
        add(new JScrollPane(messageView));
        setVisible(true);
        pack();
    }

    /**
     * Show message in message view
     * @param message new message to show
     */
    public void printMessage(String message){
        messageView.append(message + "\n");
    }

    /**
     * Close PrintWriter, BufferedReader and socket.
     */
    public void closeClient(){
        try {
            outPrint.close();
            serverIn.close();
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates Socket, BufferedReader, Printwriter.
     * Starts new ServerReader thread that listen to the BufferedReader
     *
     * @param host socket host
     * @param port socket port
     */
    public void initialize(String host, int port){

        try {
            socket = new Socket(host, port);
            serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Thread threadIn = new Thread(new ServerReader(serverIn, this));
            outPrint = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "ISO-8859-1"), true);
            threadIn.start();
        } catch (SocketException s){
            System.out.println(s.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Start the program. Uses host 127.0.0.1 and port 2000 if nothing else is specified by user.
     * Host should be specified at index 0 and host at index 1
     * @param args
     */
    public static void main(String args[]) {

        String host = "127.0.0.1";
        int port = 2000;

        if(args.length > 2){
            System.out.println("To many arguments");
            System.exit(1);
        }
        if(args.length > 0){
            host = args[0];
        }
        if(args.length > 1){
            port = Integer.parseInt(args[1].toString());
        }

        Client client = new Client(host, port);
        client.initialize(host, port);
    }
}
