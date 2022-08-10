package store;

/**
 * The SalesLineItem class represents an itemLine for the sale. This class is contained by the Sale class, and
 * describes the productSpecification class.
 */
public class SalesLineItem {
    /** Member Variables */
    private int quantity;
    private ProductSpecification spec;

    /**
     * Constructor - assign values to quantity and spec
     * @param spec - ProductSpecification
     * @param quantity - Quantity of items user would like to purchase
     */
    public SalesLineItem(ProductSpecification spec, int quantity){
        this.spec = spec;
        this.quantity = quantity;
    }

    /**
     * Set method allowing for the quantity to be set. This method adds the quantity to the spec of the existing
     * salesLineItem.
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity += quantity;
    }

    /**
     * Get method allowing the Sale class to obtain the id of the spec.
     * @return String - itemId
     */
    public String getId(){
        return spec.getId();
    }

    /**
     * Get method allowing the Sale class to obtain the name of the spec.
     * @return String - itemName
     */
    public String getName() {return spec.getName();}

    /**
     * Get method allowing the Sale class to obtain the price of the spec.
     * @return float - unitPrice
     */
    public float getPrice(){
        return spec.getPrice();
    }

    /**
     * This method, when invoked, will calculate the subtotal for the salesLineItem.
     * @return
     */
    public float getSubtotal(){
        return quantity * spec.getPrice();
    }

    /**
     * Get method allowing the Sale class to obtain the quantity
     * @return
     */
    public int getQuantity(){return quantity;}

    /**
     * This method will override the toString of the class that invokes it and will return a String containing
     * a formatted response.
     * @return temp - String
     */
    @Override
    public String toString() {
        String FORMAT = "%2s   %-15s $ %-9.2f";
        String temp = String.format(FORMAT, this.quantity, spec.getName(), getSubtotal());
        return temp +'\n';
    }
}


