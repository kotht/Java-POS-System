package store;

/**
 * The Payment class represents the payment function of a sale. It only allows the Sale class to obtain member
 * variable information as the Sale class handles the sale transaction.
 */
public class Payment {
    /** Member Variables */
    private float amount;

    /**
     * Default Constructor
     */
    public Payment(){
        this.amount = 0f;
    }

    /**
     * Constructor - allows the amount to be initialized
     * @param cashTendered - float
     */
    public Payment(float cashTendered) {amount = cashTendered;}

    /**
     * Get method that allows the caller to obtain the value of amount from this class
     * @return float - amount tendered
     */
    public float getAmount() {return amount;}

    /**
     * Set method that allows the caller to set the value of the member variable
     * @param amount - float - amount tendered
     */
    public void setAmount(Float amount) {
        this.amount = amount;
    }
}