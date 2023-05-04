package coupons;

/**
 * An object representing an item in a cart with a quantity and price.
 */
public class CartItem {
  private double price;
  private final int qty;
  private final String name;
  private boolean hasBeenDiscounted;


  /**
   * private constructor.
   *
   * @param name  the name of the item.
   * @param qty   the quantity of the item.
   * @param price the price of the item.
   * @return returns an instance of{@link CartItem} with the given data.
   */
  private CartItem(String name, int qty, double price) {
    if (name == null || name.equals("")) {
      throw new IllegalArgumentException("Name cannot be empty or null.");
    } else if (qty <= 0) {
      throw new IllegalArgumentException("quantity must be greater than zero.");
    } else if (price <= 0) {
      throw new IllegalArgumentException("price must be greater than zero.");
    }
    this.name = name;
    this.price = price;
    this.qty = qty;
    this.hasBeenDiscounted = false;
  }


  /**
   * gets price of an "each".
   *
   * @return unit price.
   */
  public double getUnitPrice() {
    return price;
  }

  /**
   * gets quantity.
   *
   * @return quantity of item.
   */
  public int getQuantity() {
    return qty;
  }

  /**
   * returns total price of these n items in the cart.
   *
   * @return total price.
   */
  public double getTotalPrice() {
    return price * qty;
  }

  /**
   * gets name of item.
   *
   * @return name as string.
   */
  public String getName() {
    return name;
  }

  /**
   * determines whether an item has been discounted already or not.
   *
   * @return true if it has.
   */
  public boolean hasBeenDiscounted() {
    return hasBeenDiscounted;
  }

  /**
   * Factory method for new non-discounted items.
   *
   * @param name  the name of the item.
   * @param qty   the quantity of the item.
   * @param price the price of the item.
   * @return returns an instance of{@link CartItem} with the given data.
   */
  public static CartItem newCartItem(String name, int qty, double price) {
    return new CartItem(name, qty, price);
  }

  /**
   * Factory method for discounted items.
   *
   * @param original the original item.
   * @param price    the discounted price of the item.
   * @return returns the discounted {@link CartItem} with the given data.
   */
  public static CartItem discountedCartItem(CartItem original, double price) {
    if (price <= 0) {
      throw new IllegalArgumentException("price must be greater than zero.");
    } else if (original.hasBeenDiscounted) {
      throw new IllegalArgumentException("cannot double discount on an item");
    }
    original.hasBeenDiscounted = true;
    original.price = price;
    return original;
  }
}
