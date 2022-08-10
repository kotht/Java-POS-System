/* =====================================================================================================================
    Programming Assignment: Cash Register System 6 - Threads, Network, and Database Implementation
    Course: CIS357
    Due date: August, 9 2022
    Name: Tanner J Koth
    GitHub: https://github.com/kotht/cis357-hw6-koth
    YouTube Video: https://www.youtube.com/watch?v=kmntRV2xURs
    Instructor: Il-Hyung Cho
    Program description: This program emulates a POS (Point-of-Sale) system at a store. The POS system allows the user
    to purchase items from our store. The program will input a .txt file and read that file to the catalog of the store.
    This catalog is then passed to the register allowing it to look at the catalog that contains the products
    specifications. The register captures the sale which contains each salesLineItem and payment. The store allows the
    user of the pos system an options to view all sales, add items, modify items, and delete items.
======================================================================================================================*/

/* =====================================================================================================================
Program features:
Implement OOP Features: 90%
Support of item change: full
Support random access file: no
Javadoc conformed comments on the classes, methods, and attributes: full
Handling wrong input and valid input: full
Program does not crash with exceptions: does not crash
Correct handling of payment and taxes: yes
Overall layout of GUI and ease of use: almost perfect
Implement Server: full
Implement Client: full
Implement Database: full
======================================================================================================================*/


import java.io.IOException;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import org.sqlite.JDBC;

/**
 * Primary class for server programming assignment 6. This class will create a new thread upon connection attempt,
 * load the database driver, and connect to the database.
 */
public class Server{

    private static int clientNo = 0;

    public static void main(String[] args){
        final int port = 8001; // set port

        // create new thread -> multi-threaded server
        new Thread(()->{
            try{
                // create new serverSocket
                ServerSocket serverSocket = new ServerSocket(port);
                // set server inetAddress
                InetAddress inetAddress = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
                SocketAddress socketAddress = new InetSocketAddress(inetAddress,port); // create SocketAddress
                System.out.println("Multithreaded Server Started: " + new Date());
                System.out.println("Address: " + socketAddress.toString()+'\n');

                Class.forName("org.sqlite.JDBC"); // locate the JDBC driver
                System.out.println("Driver loaded");

                // Establishing a connection
                String dbUrl = "jdbc:sqlite:C:\\school\\CIS357\\assign6_tjkoth\\database\\items.db";
                Connection conn = DriverManager.getConnection(dbUrl); // init connection to db
                System.out.println("Connected to DB");

                while(!serverSocket.isClosed()){
                    Socket socket = serverSocket.accept(); // listen for a new connection
                    clientNo++; // increment clientNo
//                    InetAddress client = socket.getInetAddress();
                    System.out.println("Client "+ clientNo + "'s host name: " + inetAddress.getHostName()); // prompt client hostname
                    System.out.println("Client "+ clientNo + "'s IP Address is " + inetAddress.getHostAddress()); // prompt client ip address
                    new Thread(new ServerHandler(socket,conn, clientNo)).start(); // start thread -> run
                }

                // close server socket, if not null will throw error
                if(serverSocket!=null)
                    serverSocket.close();

                // close connection, in not null will throw error
                if(conn!=null)
                    conn.close();
            }
            catch (ClassNotFoundException c){
                c.printStackTrace();
            }
            catch (SQLException s){
                s.printStackTrace();
            }
            catch (IOException ex){
                System.err.println(ex);
            }
        }).start();
    }
}
