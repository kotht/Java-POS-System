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

package controllers.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ClientApplication extends Application{
    /**
     * This method overrides the start method in application. This method prompts the user with the initial connect
     * scene.
     * @param stage
     */
    @Override
    public void start(Stage stage){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("connection-view.fxml")); // load xml file
            Scene connectionScene = new Scene(root); // scene creation
            stage.setTitle("Koth Register System"); // set stage title
            stage.setScene(connectionScene);
            stage.setResizable(false); // set resizable false -> all scenes are created specific to their resolution
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Application.launch(args);
    }
}
