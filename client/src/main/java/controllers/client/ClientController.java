package controllers.client;

/* imports */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import store.ProductSpecification;
import store.Register;
import store.Sale;
import store.SalesLineItem;
import java.io.IOException;
import java.util.List;

public class ClientController {
    /** member variables */
    private Stage stage;
    private Scene scene;
    private Parent root;
    private String address;
    public Client client = new Client();
    public static Register register = new Register();
    public static ObservableList<Sale> salesLog = FXCollections.observableArrayList();

    /** member elements */
    @FXML
    private TextField tfAddress;
    @FXML
    private TextField tfQuantity;
    @FXML
    private TextField tfAmtTendered;
    @FXML
    private TextField tfItemCode;
    @FXML
    private Label lblError;
    @FXML
    private Label lblConnectError;
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblDailyTotal;
    @FXML
    private ListView lvSaleLog;
    @FXML
    private TextArea taDetails;
    @FXML
    private Label lblFunds;

    /**
     * This method, is envoked when the user clicks connect on the connectionScene. This method will invoke a method in
     * the Client class attempting to connect with the server. If the connection is valid, the method with load the
     * scene that allows the user to make purchases, as well as view the sales log. If the connection is invalid, the
     * method will invoke the connectionErr function, which will prompt the client with text regarding the occurred error.
     * @param event ActionEvent
     */
    @FXML
    public void connect(ActionEvent event){
        try{
            address = tfAddress.getText();
            if(!address.isEmpty()){
                System.out.println("Attempting Connection: " + address);
                if(client.connect(address)){
                    register.makeNewSale();
                    root = FXMLLoader.load(getClass().getResource("main-view.fxml"));
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
                else{
                    connectionErr();
                }
            }
            else{
                connectionErr();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * This method, is activated when the addItem button is clicked by the user. This method implements error-handling,
     * to process adding an item to the sale. This method will send the itemCode that the user entered to the server,
     * and wait for a response from the server. The server should return a productSpecification, which is then processed
     * through the use of classes in the store package. This includes generating a valid receipt for the user.
     * @param event ActionEvent
     */
    @FXML
    public void addItem(ActionEvent event){
        lblError.setText(null);

        try{
            String itemCode = tfItemCode.getText().trim(); // get itemCode from textField
            int quantity = Integer.parseInt(tfQuantity.getText().trim()); // get quantity from textField
            if(!itemCode.isEmpty() &&  quantity > 0){ // itemCode != null and quantity must be greater than zero
                String prodSpec = client.sendItemCode(itemCode); // invoking sendItemCode in client class to send req to server
                if(!prodSpec.equals("Invalid Entry")){
                    // Build ongoing sales receipt
                    String[] tokens = prodSpec.split(","); // split res data from server
                    ProductSpecification spec = new ProductSpecification(tokens[0],Float.parseFloat(tokens[2]),tokens[1]);
                    register.enterItem(spec, quantity); // send register new product spec

                    List<SalesLineItem> sli = register.getSalesLineItems(); // get SalesLineItems
                    StringBuilder str = new StringBuilder(); // implement StringBuilder to create string for TextArea
                    str.append("Quantity   Item Name   Subtotal\n");
                    str.append("--------------------------------\n");
                    sli.forEach((elem)->{
                        str.append(elem.toString());
                    });
                    str.append("--------------------------------\n");
                    str.append("Total: $" + String.format("%.2f",register.getTotal()+'\n'));

                    taDetails.setText(String.valueOf(str)); // update TextArea to generatedString
                }
            }
            else {
                lblError.setText("Invalid Entry"); // prompt the user upon invalid entry
            }
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        catch (NumberFormatException e){
            lblError.setText("Invalid Entry"); // prompt the user upon invalid entry
        }
    }

    /**
     * This method, is activated when the pay button is clicked by the user. This method implements error-handling,
     * and will evaluate that the user has entered sufficient funds to purchase the items they selected.
     * @param event ActionEvent
     */
    @FXML
    public void pay(ActionEvent event){
        lblFunds.setText(null); // reset when payment is attempted
        try{
            float amt = Float.parseFloat(tfAmtTendered.getText().trim()); // get amount tendered
            float change = 0 - register.makePayment(amt); // returned value is negative if amt was valid
            if(change < 0){
                lblFunds.setText("Insufficient Funds"); // prompt the user of insufficient funds
            }
            else{
                tfAmtTendered.clear();
                taDetails.appendText("\nAmount Tendered: $"+String.format("%.2f",amt));
                taDetails.appendText("\nChange: $"+String.format("%.2f",change));
                taDetails.appendText("\n--------------------------------");
                salesLog.add(register.getSale()); // add sale to list of sales
                lvSaleLog.getItems().clear(); // clear listview
                salesLog.forEach((e)->{
                    lvSaleLog.getItems().add(e); //listView lists each e of salesLog which contains each sale attempted
                });
                register.makeNewSale(); // creation of new sale upon successful transaction
                lblTotal.setText("Daily Total: $"+String.format("%.2f",register.getDailyTotal())); // update daily totals
                lblDailyTotal.setText("Daily Total: $"+String.format("%.2f",register.getDailyTotal()));
            }
        }
        catch (NumberFormatException err){
            err.printStackTrace();
        }
    }

    /**
     * This method, when invoked, will prompt the user with error text, regarding the error that was produced. Primarily
     * this method is used in the connectScene to a label, and print the terminal the error.
     */
    public void connectionErr(){
        lblConnectError.setText("Connection Unsuccessful. Enter a valid address:");
        tfAddress.clear();
        System.out.println("Connection unsuccessful.\n");
    }
}
