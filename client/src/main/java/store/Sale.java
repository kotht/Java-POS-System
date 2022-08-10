package store;

/** imports */
import java.util.*;

/**
 * Sale class is used to process a sale. It is used by the register class, and contains the SalesLineItem class,
 * and uses the Payment class.
 */
public class Sale {
    /** Member Variables */
    private List<SalesLineItem> saleLineItems;
    private Date date;
    private boolean isComplete = false;
    private Payment payment;
    private float change;
    private final float TAX_RATE = 1.06f;

    /**
     * Default constructor for Sale
     */
    public Sale(){
        this.saleLineItems = new ArrayList<>();
        this.date = new Date();
        this.isComplete = false;
        this.payment = new Payment();
    }

    public List<SalesLineItem> getSLI(){
        return saleLineItems;
    }

    /**
     * This method, when invoked, will also invoke the add method of the salesLineItems class to create a salesLineItem.
     * @param spec - ProductSpecification
     * @param quantity - Quantity of items user request to purchase
     */
    public void makeLineItem(ProductSpecification spec, int quantity) {
        saleLineItems.add(new SalesLineItem(spec,quantity));
    }

    /**
     * This method, when invoked, will check if the itemCode passed as a param is unique or already exists. If it
     * already exists it will return false, else if no salesLineItem contains this itemCode it will return true.
     * @param itemCode
     * @return boolean
     */
    public boolean unique(String itemCode){
        for(SalesLineItem item : saleLineItems){
            if(item.getId().equals(itemCode))
                return false;
        }
        return true;
    }

    /**
     * This method, when invoked, will check if any itemCode already exists and will add the quanitity to that item.
     * @param quantity
     */
    public void addToItem(ProductSpecification spec, int quantity){
        for(SalesLineItem item : saleLineItems){
            if(item.getId().equals(spec.getId())){
                item.setQuantity(quantity);
            }
        }
    }

    /**
     * This method, when invoked, will calculate the subtotal of the sale. It will return a float that represents the
     * sale subtotal.
     * @return float - salesSubtotal
     */
    public float getSubtotal(){
        float subTotal = 0;
        Iterator it = saleLineItems.iterator();
        while(it.hasNext()) {
            SalesLineItem sli = (SalesLineItem) it.next();
            subTotal += sli.getSubtotal();
        }
        return subTotal;
    }

    /**
     * This method, when invoked, will calculate the total of the sale. If itemID begins with B it does not have sales
     * tax, however all other items do have sales tax.
     * @return float - salesTotal
     */
    public float getTotal() {
        float total = 0;
        Iterator it = saleLineItems.iterator();
        while(it.hasNext()) {
            SalesLineItem sli = (SalesLineItem) it.next();
            if(sli.getId().charAt(0) == ('B'))
                total += sli.getSubtotal();
            else
                total += (sli.getSubtotal() * TAX_RATE);
        }
        return total;
    }

    /**
     * This method, when invoked, will allow the user to make a payment for the sale. This method allows the Payment
     * class to store the values of the payment, as a means of encapsulation for security purposes. This method will
     * then prompt the user accordingly based on their input.
     * @param cashTendered - amount user has tendered
     * @param
     */
    public float makePayment(float cashTendered) {
        try{
            payment = new Payment(cashTendered);
            if(payment.getAmount() > getTotal()){
                isComplete = true;
            }
            else{
                payment.setAmount(-1f);
                isComplete = false;
            }
            change = getTotal()-payment.getAmount();
        } catch (InputMismatchException e){
            System.out.println("!!! Invalid data type");
        }
        finally {
            return change;
        }
    }

    /**
     * This method, when invoked, will return the boolean varaible isComplete, which represents if the sale is complete.
     * @return isComplete - Boolean
     */
    public boolean getIsCompleted(){
        return isComplete;
    }

    /**
     * This method, when invoked, will return all member data variable information in a formatted string to the caller.
     * @return String
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("Date: " + date + "\nItems: ");

        saleLineItems.forEach((e)->{
            str.append("\nItemID: "+ e.getId() +"\tName: " + e.getName() +"\tQuantity: " + e.getQuantity() + "\tPrice: $" + String.format("%.2f",e.getPrice()));
        });

        str.append("\nSubtotal: $" + String.format("%.2f",getSubtotal()) + "\nTotal: $" + String.format("%.2f",getTotal()) + "\nPayment Amount: $" + payment.getAmount());
        return str.toString();
    }
}
