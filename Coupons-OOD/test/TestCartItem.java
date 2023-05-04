import org.junit.Test;

import coupons.CartItem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Simple test for the {@link CartItem} class.
 */
public class TestCartItem {

  /**
   * factory method test.
   */
  @Test
  public void testNewCartItemFactoryTest() {
    try {
      CartItem.newCartItem("getName", 0, 1);
      fail();
    } catch (IllegalArgumentException e) {
      e.getMessage();
    }
    try {
      CartItem.newCartItem("getName", -1, 1);
      fail();
    } catch (IllegalArgumentException e) {
      e.getMessage();
    }
    try {
      CartItem.newCartItem("getName", 1, -1);
      fail();
    } catch (IllegalArgumentException e) {
      e.getMessage();
    }

    try {
      CartItem.newCartItem("getName", 0, 1);
      fail();
    } catch (IllegalArgumentException e) {
      e.getMessage();
    }
    try {
      CartItem.newCartItem(null, -1, 1);
      fail();
    } catch (IllegalArgumentException e) {
      e.getMessage();
    }
  }


  /**
   * discount factory method test.
   */
  @Test
  public void testDiscountCartItemFactoryTest() {
    CartItem initial = CartItem.newCartItem("rice", 30, 40);
    try {
      CartItem.discountedCartItem(initial, -1);
      fail();
    } catch (IllegalArgumentException e) {
      e.getMessage();
    }
    try {
      CartItem.discountedCartItem(initial, 0);
      fail();
    } catch (IllegalArgumentException e) {
      e.getMessage();
    }
    CartItem.discountedCartItem(initial, 10);
    try {
      CartItem.discountedCartItem(initial, 1);
      fail();
    } catch (IllegalArgumentException e) {
      e.getMessage();
    }
  }

  /**
   * tests {@link CartItem} accessors.
   */
  @Test
  public void testAccessors() {
    String name = "hello world";
    int quant = 2;
    double price = 1.5;
    CartItem item = CartItem.newCartItem(name, quant, price);

    assertEquals(name, item.getName());
    assertEquals(quant, item.getQuantity());
    assertEquals(price, item.getUnitPrice(), .001);
    assertEquals(price * quant, item.getTotalPrice(), .001);

  }
}
