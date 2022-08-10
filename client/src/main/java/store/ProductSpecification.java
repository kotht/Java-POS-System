package store;

/**
 * The ProductSpecification class stores the specifications of products.
 */
public class ProductSpecification {
    /** Member Variables */
    private String id;
    private float price;
    private String name;

    /**
     * Constructor - set all variables to corresponding values
     * @param id - String itemID
     * @param price - Float unitPrice
     * @param name - String itemName
     */
    public ProductSpecification(String id, float price, String name) {
        this.id = id;
        this.price = price;
        this.name = name;
    }

    /**
     * Get method used to get itemId
     * @return String id - itemId
     */
    public String getId(){return id;}

    /**
     * Get method used to get unitPrice
     * @return float price - unitPrice
     */
    public float getPrice(){return price;}

    /**
     * Get method used to get itemName
     * @return String name - itemName
     */
    public String getName(){return name;}

    /**
     * This method will override the toString of the class that invokes it and will return a String containing
     * a formatted response.
     * @return String - id price name
     */
    @Override
    public String toString(){
        return this.id + " " + this.price + " " + this.name;
    }
}


