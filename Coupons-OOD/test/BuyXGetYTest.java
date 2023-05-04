import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import coupons.CartItem;
import coupons.Coupon;
import coupons.CouponUseException;
import coupons.concrete.AmountOffCoupon;
import coupons.concrete.BuyXGetYCoupon;
import coupons.concrete.PercentOffCoupon;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * Test Class for the {@link BuyXGetYCoupon} class.
 */
public class BuyXGetYTest extends CouponTest {


  @Override
  protected Coupon getDefault(boolean stackable) {
    return new BuyXGetYCoupon("name", 1, 1, stackable);
  }

  @Override
  public void testToString() {
    Coupon coupon = new BuyXGetYCoupon("steak", 1, 1, false);
    assertEquals("Buy 1 get 1 free", coupon.toString());
  }

  @Override
  public void testConstructor() {
    for (boolean b : new boolean[]{false, true}) {
      try {
        new BuyXGetYCoupon("", 1, 1, b);
        fail();
      } catch (IllegalArgumentException e) {
        e.getMessage();
      }
      List<Coupon> coupons = new ArrayList<>();
      String item = "coffee";
      int[][] pairs = {{1, 0}, {-1, 1}, {1, -1}};
      for (int[] pair : pairs) {
        try {

          new BuyXGetYCoupon(item, pair[0], pair[1], b);

          fail();
        } catch (IllegalArgumentException e) {
          e.getMessage();
        }
      }
    }
  }

  @Override
  public void testApplyDiscountToItemSingleCoupon() {
    String name = "milk";
    CartItem milk = CartItem.newCartItem(name, 2, 2);
    Coupon coupon = new BuyXGetYCoupon(name, 1, 1, false);

    CartItem discount = coupon.applyDiscount(milk);
    assertEquals(2, discount.getQuantity());
    assertEquals(2, discount.getTotalPrice(), .001);

    coupon = new BuyXGetYCoupon(name, 1, 1, false);
    milk = CartItem.newCartItem(name, 2, 1);
    discount = coupon.applyDiscount(milk);
    assertEquals(2, discount.getQuantity());
    assertEquals(1, discount.getTotalPrice(), .001);
  }


  @Override
  public void testApplyDiscountToItemStackedCoupon() {
    CartItem milk = CartItem.newCartItem("milk", 3, 5);

    CartItem discount = new BuyXGetYCoupon("milk", 1, 2, true)
            .applyDiscount(milk);
    assertEquals(5, discount.getTotalPrice(), .001);
    assertEquals(3, discount.getQuantity(), .001);
  }

  @Override
  public void testStackableAgainstItsOwnKindOnSameItem() {
    Coupon coupon = new BuyXGetYCoupon("item", 1, 1, true);
    try {
      Coupon other = new BuyXGetYCoupon("item", 7, 3, true);
      coupon.stackWith(other);
    } catch (CouponUseException ce) {
      fail();
    }
  }

  @Override
  public void testStackableAgainstOwnKindDifferentItem() {
    Coupon coupon = new BuyXGetYCoupon("itemName", 1, 1, true);
    try {
      Coupon other = new BuyXGetYCoupon("hello", 7, 3, true);
      coupon.stackWith(other);
      fail();
    } catch (CouponUseException ce) {
      ce.getMessage();
    }
  }

  @Override
  public void cannotStackWithItself() {
    Coupon coupon = new BuyXGetYCoupon("itemName", 1, 1, true);
    try {
      coupon.stackWith(coupon);
      fail();
    } catch (CouponUseException ce) {
      ce.getMessage();
    }
  }

  @Override
  public void cannotReuseCoupon() {
    CartItem cheddar = CartItem.newCartItem("cheddar", 2, 1);
    Coupon coupon = new BuyXGetYCoupon("cheddar", 1, 1, true);
    coupon.applyDiscount(cheddar);
    try {
      coupon.applyDiscount(cheddar);
      fail();
    } catch (Exception e) {
      e.getMessage();
    }
  }


  @Override
  public void testGetForFree() {
    CartItem cheese = CartItem.newCartItem("cheese", 1, 3);
    Coupon sample = new BuyXGetYCoupon("cheese", 0, 1, false);
    try {
      sample.applyDiscount(cheese);
      fail();
    } catch (IllegalArgumentException e) {
      e.getMessage();
    }
  }

  @Override
  public void testCannotStackUsedAndUnused() {
    CartItem a = CartItem.newCartItem("a", 6, 32);
    Coupon one;
    Coupon two;
    one = new BuyXGetYCoupon("a", 1, 2, true);
    two = new BuyXGetYCoupon("a", 1, 5, true);
    CartItem discount = one.applyDiscount(a);


    try {
      one.stackWith(two);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }

    a = CartItem.newCartItem("a", 6, 32);
    one = new BuyXGetYCoupon("a", 1, 2, true);
    two = new BuyXGetYCoupon("a", 1, 5, true);
    two.applyDiscount(a);
    try {
      one.stackWith(two);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }
  }

  @Override
  public void testApplyCouponToIncorrectItem() {
    CartItem salsa = CartItem.newCartItem("salsa", 3, 5);
    Coupon forPeanuts = new BuyXGetYCoupon("peanuts", 1, 1, false);
    try {
      forPeanuts.applyDiscount(salsa);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }
  }


  /**
   * class specific test about the buy x get y ratio rules when they are different.
   */
  @Test
  public void testXYRatioWhichIsBetter() {
    Coupon worse;
    Coupon better;
    worse = new BuyXGetYCoupon("getName", 1, 1, true);
    better = new BuyXGetYCoupon("getName", 1, 2, true);
    CartItem cartItem = CartItem.newCartItem("getName", 3, 1);
    CartItem disc1 = worse.stackWith(better).applyDiscount(cartItem);

    cartItem = CartItem.newCartItem("getName", 3, 1);
    worse = new BuyXGetYCoupon("getName", 1, 1, true);
    better = new BuyXGetYCoupon("getName", 1, 2, true);
    CartItem disc2 = better.stackWith(worse).applyDiscount(cartItem);

    assertEquals(1, disc1.getTotalPrice(), .001);
    assertEquals(1, disc2.getTotalPrice(), .001);

    assertEquals(3, disc1.getQuantity(), .001);
    assertEquals(3, disc2.getQuantity(), .001);
  }

  /**
   * class specific test about the buy x get y ratio rules when they are the same ratio.
   */
  @Test
  public void testXYRatioEqualChoosesLower() {
    Coupon first;
    Coupon second;
    first = new BuyXGetYCoupon("getName", 1, 2, true);
    second = new BuyXGetYCoupon("getName", 2, 4, true);
    CartItem cartItem = CartItem.newCartItem("getName", 3, 1);
    CartItem disc1 = first.stackWith(second).applyDiscount(cartItem);

    cartItem = CartItem.newCartItem("getName", 3, 1);
    first = new BuyXGetYCoupon("getName", 1, 2, true);
    second = new BuyXGetYCoupon("getName", 2, 4, true);
    CartItem disc2 = second.stackWith(first).applyDiscount(cartItem);

    assertEquals(1, disc1.getTotalPrice(), .001);
    assertEquals(1, disc2.getTotalPrice(), .001);

    assertEquals(3, disc1.getQuantity(), .001);
    assertEquals(3, disc2.getQuantity(), .001);

  }

  /**
   * class specific: tests illegal applications due to amounts.
   */
  @Test
  public void testCannotUseOnIllegalAmounts() {
    CartItem cartItem;
    Coupon coupon;

    cartItem = CartItem.newCartItem("cartItem", 2, 90);

    try {
      coupon = new BuyXGetYCoupon("cartItem", 1, 2, true);
      coupon.applyDiscount(cartItem);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }


    cartItem = CartItem.newCartItem("cartItem", 1, 50);
    try {
      coupon = new BuyXGetYCoupon("cartItem", 0, 2, true);
      coupon.applyDiscount(cartItem);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }

  }


  @Override
  public void testCanStackWithDoesntHaveSideEffects() {
    Coupon coupon = new BuyXGetYCoupon("steak", 3, 1, true);
    Coupon coupon2 = new BuyXGetYCoupon("steak", 7, 3, true);
    String toString = coupon.toString();


    assertTrue(coupon.isStackable());
    assertFalse(coupon.hasBeenUsed());
    assertEquals(toString, coupon.toString());

    assertTrue(coupon.canStackWith(coupon2));

    assertTrue(coupon.isStackable());
    assertFalse(coupon.hasBeenUsed());
    assertEquals(toString, coupon.toString());

    assertTrue(coupon.canStackWith(coupon2));

    assertTrue(coupon.isStackable());
    assertFalse(coupon.hasBeenUsed());
    assertEquals(toString, coupon.toString());


  }

  @Override
  public void testCanApplyDoesntHaveSideEffects() {
    Coupon coupon = new BuyXGetYCoupon("steak", 3, 1, true);
    CartItem item = CartItem.newCartItem("steak", 4, 2);
    String toString = coupon.toString();


    assertTrue(coupon.isStackable());
    assertFalse(coupon.hasBeenUsed());
    assertEquals(toString, coupon.toString());

    assertTrue(coupon.canApplyTo(item));

    assertTrue(coupon.isStackable());
    assertFalse(coupon.hasBeenUsed());
    assertEquals(toString, coupon.toString());

    assertTrue(coupon.canApplyTo(item));

    assertTrue(coupon.isStackable());
    assertFalse(coupon.hasBeenUsed());
    assertEquals(toString, coupon.toString());
  }

  @Override
  public void testIsUsed() {
    CartItem item = CartItem.newCartItem("steak", 3, 10);
    Coupon buyGet1 = new BuyXGetYCoupon("steak", 1, 1, true);
    Coupon buyGet2;
    assertFalse(buyGet1.hasBeenUsed());
    buyGet1.applyDiscount(item);
    assertTrue(buyGet1.hasBeenUsed());

    item = CartItem.newCartItem("steak", 3, 10);
    buyGet1 = new BuyXGetYCoupon("steak", 1, 1, true);
    buyGet2 = new BuyXGetYCoupon("steak", 2, 2, true);

    Coupon stack = buyGet1.stackWith(buyGet2);
    assertFalse(stack.hasBeenUsed());
    stack.applyDiscount(item);
    assertTrue(stack.hasBeenUsed());
  }

  @Override
  public void testStackMultiple() {

    CartItem bread = CartItem.newCartItem("bread", 20, 2);
    Coupon one;
    Coupon two;
    Coupon three;


    one = new BuyXGetYCoupon("bread", 1, 1, true);
    two = new BuyXGetYCoupon("bread", 1, 2, true);
    three = new BuyXGetYCoupon("bread", 1, 4, true);

    Coupon stack1 = one.stackWith(two);
    Coupon stack2 = stack1.stackWith(three);

    CartItem item = stack2.applyDiscount(bread);

    assertEquals(8, item.getTotalPrice(), 0.01);

  }

  @Override
  public void testStackMultipleSameResult() {
    CartItem bread = CartItem.newCartItem("bread", 20, 2);
    Coupon one;
    Coupon two;
    Coupon three;
    CartItem result;
    Coupon stack1;
    Coupon stack2;


    one = new BuyXGetYCoupon("bread", 1, 1, true);
    two = new BuyXGetYCoupon("bread", 1, 2, true);
    three = new BuyXGetYCoupon("bread", 1, 4, true);

    stack1 = one.stackWith(two);
    stack2 = stack1.stackWith(three);
    result = stack2.applyDiscount(bread);

    assertEquals(8, result.getTotalPrice(), 0.01);

    bread = CartItem.newCartItem("bread", 20, 2);
    one = new BuyXGetYCoupon("bread", 1, 1, true);
    two = new BuyXGetYCoupon("bread", 1, 2, true);
    three = new BuyXGetYCoupon("bread", 1, 4, true);

    stack1 = three.stackWith(one);
    stack2 = stack1.stackWith(two);
    result = stack2.applyDiscount(bread);

    assertEquals(8, result.getTotalPrice(), 0.01);

    bread = CartItem.newCartItem("bread", 20, 2);
    one = new BuyXGetYCoupon("bread", 1, 1, true);
    two = new BuyXGetYCoupon("bread", 1, 2, true);
    three = new BuyXGetYCoupon("bread", 1, 4, true);

    stack1 = two.stackWith(three);
    stack2 = stack1.stackWith(one);
    result = stack2.applyDiscount(bread);

    assertEquals(8, result.getTotalPrice(), 0.01);
  }

  @Override
  public void testFailsToStackUsedPart() {
    CartItem bread = CartItem.newCartItem("bread", 2, 20);
    Coupon one;
    Coupon two;
    Coupon three;
    Coupon stack1;
    Coupon stack2;
    one = new BuyXGetYCoupon("bread", 1, 1, true);
    two = new BuyXGetYCoupon("bread", 1, 2, true);
    three = new BuyXGetYCoupon("bread", 1, 4, true);
    stack1 = one.stackWith(two);
    stack2 = stack1.stackWith(three);
    one.applyDiscount(bread);

    try {
      stack2.applyDiscount(bread);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }
  }

  @Override
  public void testStackFailsToApplyUsedPart() {
    CartItem bread = CartItem.newCartItem("bread", 6, 20);
    Coupon one;
    Coupon two;
    Coupon three;
    Coupon stack1;
    one = new BuyXGetYCoupon("bread", 5, 1, true);
    two = new BuyXGetYCoupon("bread", 2, 4, true);
    three = new BuyXGetYCoupon("bread", 4, 2, true);

    stack1 = one.stackWith(two);
    stack1.applyDiscount(bread);
    try {
      stack1.stackWith(three);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }
  }

  @Override
  public void testCanApplyFails() {
    CartItem item = CartItem.newCartItem("name", 10, 10);
    Coupon coupon;
    Coupon stack;
    //can't apply b/c name
    assertFalse(new BuyXGetYCoupon("nam", 1, 1, true).canApplyTo(item));


    coupon = new BuyXGetYCoupon("name", 2, 3, true);
    Coupon coupon1 = new BuyXGetYCoupon("name", 3, 2, true);
    stack = coupon.stackWith(coupon1);
    coupon.applyDiscount(item);
    //can't apply because used.
    assertFalse(stack.canApplyTo(item));

    // can't apply because buy value
    coupon = new BuyXGetYCoupon("name", 11, 10000, true);
    assertFalse(coupon.canApplyTo(item));

    // can't apply because buy value part 2 the revenge
    coupon = new BuyXGetYCoupon("name", 11, 1, true);
    assertFalse(coupon.canApplyTo(item));

    //cannot apply because stacking better ratio forces a higher number
    item = CartItem.newCartItem("name", 40, 10);
    coupon = new BuyXGetYCoupon("name", 11, 39, true);
    coupon1 = new BuyXGetYCoupon("name", 41, 59, true);
    stack = coupon.stackWith(coupon1);
    assertFalse(stack.canApplyTo(item));
    try {
      coupon.applyDiscount(item);
    } catch (CouponUseException e) {
      e.getMessage();
    }

    assertFalse(stack.canApplyTo(item));
    assertTrue(stack.canApplyTo(CartItem.newCartItem("name", 100, 10)));


    //use stack compared
    coupon = new BuyXGetYCoupon("name", 19, 21, true);
    coupon1 = new BuyXGetYCoupon("name", 21, 19, true);
    stack = coupon.stackWith(coupon1);

    assertTrue(stack.canApplyTo(item));

    coupon.applyDiscount(item);
    assertFalse(stack.canApplyTo(item));
  }

  @Override
  public void testCannotStackWithAnotherType() {
    PercentOffCoupon percentCoup = new PercentOffCoupon("beans", 50, true);
    BuyXGetYCoupon buyGet = new BuyXGetYCoupon("beans", 1, 1, true);
    AmountOffCoupon singleAmount = new AmountOffCoupon("beans", 3, true);

    assertFalse(buyGet.canStackWith(singleAmount));
    assertFalse(buyGet.canStackWith(percentCoup));

    try {
      buyGet.stackWith(singleAmount);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }
    try {
      buyGet.stackWith(percentCoup);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }
  }

  @Override
  public void testCannotDiscountDiscountedItem() {
    CartItem item = CartItem.newCartItem("food", 30, 30);
    Coupon coup = new BuyXGetYCoupon("food", 1, 1, true);
    assertFalse(item.hasBeenDiscounted());
    Assert.assertTrue(coup.canApplyTo(item));
    coup.applyDiscount(item);
    assertFalse(coup.canApplyTo(item));
    Assert.assertTrue(item.hasBeenDiscounted());


    item = CartItem.newCartItem("food", 30, 30);
    coup = new BuyXGetYCoupon("food", 1, 1, true);
    Coupon coup2 = new BuyXGetYCoupon("food", 2, 1, true);
    assertFalse(item.hasBeenDiscounted());
    Assert.assertTrue(coup.canApplyTo(item));

    coup.stackWith(coup2).applyDiscount(item);
    assertFalse(coup.canApplyTo(item));
    Assert.assertTrue(item.hasBeenDiscounted());
  }


}
