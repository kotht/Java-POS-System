import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.JDBC;

/**
 * This class handles the data input and output from the server.
 */
public class ServerHandler implements Runnable{
    /** member variables */
    private Socket socket; //
    private int clientNo = 0; // number a client
    private Connection connection;
    private DataInputStream inputFromClient;
    private DataOutputStream outputToClient;

    /**
     * This constructor accepts a socket, and connection as parameters and sets the values of socket, connection,
     * bufferedReader, and bufferedWriter.
     * @param socket Socket to localhost:8001
     * @param connection Connection to db
     */
    public ServerHandler(Socket socket, Connection connection, int clientNo){
        this.socket = socket;
        this.connection = connection;
        this.clientNo = clientNo;
    }

    /**
     * This method overrides, the run method located in the start method of the thread class. This method will init
     * data streams, and will handle processing of that data passed from the client to the server. It will also,
     * process query requests to the database, or interact with the database.
     */
    @Override
    public void run(){

        try{
            inputFromClient = new DataInputStream(socket.getInputStream()); // create input data stream
            outputToClient = new DataOutputStream(socket.getOutputStream()); // create output data stream

            while(socket.isConnected()){
                String itemCode = inputFromClient.readUTF(); // receive itemCode from client
                String queryResult = query(itemCode); // invoke query to query sqlite db
                if(queryResult != null){
                    outputToClient.writeUTF(queryResult); // send query result back to client
                    outputToClient.flush();
                }
                else{
                    outputToClient.writeUTF("Invalid Entry");
                }
                System.out.println("Client " + clientNo + "'s itemCode: '"+ itemCode);
                System.out.println("Sent Client "+clientNo+ " prodSpec: " +queryResult + "");
            }

        }
        catch (SocketException e){

        }
        catch (IOException e){

        }
        finally {
            closeAll(socket, inputFromClient, outputToClient);
        }
    }

    /**
     * This method, when invoked, will submit a query to the database, and will return the string returned from that
     * query. It begins by creating a statement that is required when executing a query. The query is then executed and
     * sent back to the server for further processing.
     * @param itemCode
     * @return
     */
    public String query(String itemCode){
        String temp = null;

        try{
            // Create statement to connect to db
            Statement statement = connection.createStatement();
            String prodSchemaQuery = "select * from item where item_code = '"+ itemCode+ "';"; // dec/init of queryString

            // Execute statement
            ResultSet resultSet = statement.executeQuery(prodSchemaQuery); // execute query
            temp = resultSet.getString(1)+","+resultSet.getString(2)+","+resultSet.getString(3); // assing return value from query
        }
        catch (SQLException e){
            System.err.println(e);
            System.out.println("Client " + clientNo + " searched for an invalid value");
        }
        finally {
            return temp; // always return temp
        }
    }

    /**
     * This method will make sure to close the socket, as well as any data streams if invoked.
     * @param socket Socket
     * @param dataInputStream DataInputStream
     * @param dataOutputStream DataOutputStream
     */
    public void closeAll(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream){
        try{
            if(dataInputStream != null)
                dataInputStream.close();

            if(dataOutputStream != null)
                dataOutputStream.close();

            if(socket != null)
                socket.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
