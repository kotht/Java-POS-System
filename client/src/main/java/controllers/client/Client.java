package controllers.client;

import java.io.*;
import java.net.*;

/**
 * This class handles the connection to the server, as well as sending and receiving data from the server.
 */
public class Client {
    /** member variables */
    Socket socket;
    DataOutputStream toServer;
    DataInputStream fromServer;
    private String address;
    private final int port = 8001;

    /**
     * Default Constructor
     */
    public Client(){

    }

    /**
     * This method, when invoked, will attempt to connect to the server. This method implements the use of socket
     * programming to establish the connection between client and server.This method will then return a boolean to the
     * caller representing the connection status to the server.
     * @param address
     * @return
     */
    public boolean connect(String address){
        boolean connected = false;
        try{
            this.socket = new Socket(); // create new socket
            socket.connect(new InetSocketAddress(address,port),2000); // set timeout 2000 -> prevents window.notresponding
            this.address = address;
            connected = true;

            System.out.println("Connected to server");
        }
        catch (SocketTimeoutException e){
            System.out.println("Connection timed out");
        }
        catch (ConnectException e){
            System.out.println("Connection refused!");
        }
        catch (UnknownHostException e){
            System.out.println("Cannot Identify Host");
        }
        catch (SocketException e){
            System.out.println("Invalid Address");
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            return connected;
        }
    }

    /**
     * This method, when invoked, will send data and receive data from the server. The method will send the server a
     * item_Code that the server will process and return as a ProductSpecification. This value is then returned to the
     * caller for further processing.
     * @param itemCode
     * @return
     */
    public String sendItemCode(String itemCode) {
        String prodSpec = null;
        try{
            Socket socket = new Socket(address, port);
            // create an output stream to send itemCode to server
            toServer = new DataOutputStream(socket.getOutputStream());

            // create input stream to receive prod Spec from the server
            fromServer = new DataInputStream(socket.getInputStream());

            toServer.writeUTF(itemCode);
            toServer.flush();

            prodSpec = fromServer.readUTF();
        }
        catch (ConnectException e){
            System.out.println("Connection refused");
        }
        catch (IOException e){
            e.printStackTrace();
            closeEverything(socket, toServer,fromServer); // close sockets, as well as data streams
        }
        finally {
            return prodSpec; // always return prodSpec
        }
    }

    /**
     * This method is used to make sure that the socket, and dataStreams are always closed. This is done to prevent
     * a nullPointerException.
     * @param socket Socket
     * @param toServer DataOutputStream
     * @param fromServer DataInputStream
     */
    private void closeEverything(Socket socket, DataOutputStream toServer, DataInputStream fromServer){
        try{
            if(toServer != null)
                toServer.close();

            if(fromServer!=null)
                fromServer.close();

            if(socket!=null)
                socket.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


}
