package store;

import java.util.InputMismatchException;
import java.util.List;

/** imports */

/**
 * Register class is used to implement a cash register in the store. It is housed by the driver class, captures the
 * sales class.
 */
public class Register {
    /** Member Variables */
    private Sale sale;
    private double dailyTotal = 0;
    private boolean saleCompleted = false;

    /**
     * Default Constructor
     */
    public Register(){
    }

    /**
     * This method will create a new sale instance, if a new sale is started.
     */
    public void makeNewSale(){sale = new Sale(); setSalesCompleted(false);}

    /**
     * This method is used to make a payment on the sale. The method implements the use of a try-catch block to handle
     * an inputMismatch exception. In the try block the method will invoke the makePayment method in the sales class to
     * handle the rest of the payment as the register class cannot directly access the payment class (security purpose).
     * The method will then wait until the callee has completed and will invoke the getSubtotal method in the sale
     * class. This value is then added to the running dailyTotal.
     * @param amt - float amount representing amtTendered
     * @return temp - represents change amount
     */
    public float makePayment(float amt){
        float temp = 0.00f;
        try{
            temp = sale.makePayment(amt); // invoke makePayment of sale
            if(sale.getIsCompleted() == true){
                dailyTotal += sale.getSubtotal(); // add subtotal to daily total once transaction has completed
            }
        }
        catch (InputMismatchException e){
            System.out.println("!!! Invalid data type");
        }
        finally{
            return temp;
        }

    }

    /**
     * When invoked this function will invoke the makeLineItem method in the sale obj instance with the required params.
     */
    public void enterItem(ProductSpecification prodSpec,int quantity) {
        if(sale.unique(prodSpec.getId()))
            sale.makeLineItem(prodSpec, quantity);
        else
            sale.addToItem(prodSpec, quantity);
    }

    /**
     * When invoked this method will set the boolean value of salesCompleted to true, meaning that all sales have been
     * completed for the day.
     * @param value - Boolean (T/F) - sales completed or not completed
     */
    public void setSalesCompleted(boolean value){
        this.saleCompleted = value;
    }

    /**
     * This method, when invoked, will return the total for the sale.
     * @return float - sale total
     */
    public float getTotal(){
        return sale.getTotal() - 10;
    }

    /**
     * This method, when invoked, will return a list of the salesLineItems.
     * @return List - salesLineItem
     */
    public List<SalesLineItem> getSalesLineItems(){
        return sale.getSLI();
    }

    /**
     * This method, when invoked, will return the dailyTotal to the caller.
     * @return double dailyTotal
     */
    public double getDailyTotal(){
        return dailyTotal;
    }

    /**
     * This method, when invoked, will return the instance sale, or the current sale.
     * @return Sale sale
     */
    public Sale getSale(){
        return sale;
    }
}

