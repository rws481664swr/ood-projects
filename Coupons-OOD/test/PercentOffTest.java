import org.junit.Assert;
import org.junit.Test;

import coupons.CartItem;
import coupons.Coupon;
import coupons.CouponUseException;
import coupons.concrete.AmountOffCoupon;
import coupons.concrete.BuyXGetYCoupon;
import coupons.concrete.PercentOffCoupon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * test class for the {@link PercentOffCoupon} class.
 */
public class PercentOffTest extends CouponTest {


  @Override
  protected Coupon getDefault(boolean stackable) {
    return new PercentOffCoupon("item", 50, stackable);
  }

  @Override
  public void testToString() {
    Coupon coupon = new PercentOffCoupon("pizza", 50, true);
    assertEquals("50% off item pizza", coupon.toString());
  }

  @Override
  public void testConstructor() {
    for (boolean b : new boolean[]{false, true}) {
      try {
        new PercentOffCoupon("", 50, b);
        fail();
      } catch (IllegalArgumentException e) {
        e.getMessage();
      }
      int[] illegalVals = {-1, 0, 101};

      for (int i : illegalVals) {
        try {
          new PercentOffCoupon("burgers", i, b);
          fail();
        } catch (IllegalArgumentException e) {
          e.getMessage();
        }
      }
    }

  }

  @Override
  public void testApplyDiscountToItemSingleCoupon() {
    CartItem burgers = CartItem.newCartItem("burgers", 2, 5);
    Coupon coupon = new PercentOffCoupon("burgers", 50, true);
    CartItem discounted = coupon.applyDiscount(burgers);

    assertEquals(5, discounted.getTotalPrice(), .001);
  }

  @Override
  public void testApplyDiscountToItemStackedCoupon() {
    CartItem cartItem = CartItem.newCartItem("guac", 3, 8);
    Coupon coupon = new PercentOffCoupon("guac", 25, true);
    Coupon coupon2 = new PercentOffCoupon("guac", 25, true);
    Coupon stacked = coupon.stackWith(coupon2);
    CartItem discounted = stacked.applyDiscount(cartItem);
    assertEquals(12, discounted.getTotalPrice(), .001);

    cartItem = CartItem.newCartItem("guac", 3, 8);
    coupon = new PercentOffCoupon("guac", 25, true);
    coupon2 = new PercentOffCoupon("guac", 25, true);
    //commutative
    discounted = coupon2.stackWith(coupon).applyDiscount(cartItem);
    assertEquals(12, discounted.getTotalPrice(), .001);
  }

  @Override
  public void testStackableAgainstItsOwnKindOnSameItem() {
    Coupon coupon = new PercentOffCoupon("pizza", 30, true);
    Coupon coupon1 = new PercentOffCoupon("pizza", 60, true);
    assertTrue(coupon.canStackWith(coupon1));
    assertTrue(coupon1.canStackWith(coupon));
  }

  @Override
  public void testStackableAgainstOwnKindDifferentItem() {
    Coupon coupon = new PercentOffCoupon("pizza", 50, true);
    Coupon coupon1 = new PercentOffCoupon("cheese", 20, true);

    assertFalse(coupon.canStackWith(coupon1));
    assertFalse(coupon1.canStackWith(coupon));

    try {
      coupon.stackWith(coupon1);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }

    try {
      coupon1.stackWith(coupon);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }
  }

  @Override
  public void cannotStackWithItself() {
    try {
      Coupon coupon = new PercentOffCoupon("pizza", 50, true);
      coupon.stackWith(coupon);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }
  }

  @Override
  public void cannotReuseCoupon() {
    CartItem cheddar = CartItem.newCartItem("cheddar", 1, 1);
    Coupon coupon = new PercentOffCoupon("cheddar", 30, true);
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
    Coupon freebie = new PercentOffCoupon("cartItem", 100, true);
    CartItem cartItem = CartItem.newCartItem("cartItem", 1, 1);
    try {
      freebie.applyDiscount(cartItem);
      fail();
    } catch (IllegalArgumentException e) {
      e.getMessage();
    }
  }

  @Override
  public void testCannotStackUsedAndUnused() {
    CartItem a = CartItem.newCartItem("a", 1, 32);
    Coupon one;
    Coupon two;
    one = new PercentOffCoupon("a", 2, true);
    two = new PercentOffCoupon("a", 5, true);
    one.applyDiscount(a);


    try {
      one.stackWith(two);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }

    a = CartItem.newCartItem("a", 1, 32);
    one = new PercentOffCoupon("a", 2, true);
    two = new PercentOffCoupon("a", 5, true);
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
    CartItem fries = CartItem.newCartItem("fries", 1, 4);
    Coupon coupon = new PercentOffCoupon("steak", 50, false);
    try {
      coupon.applyDiscount(fries);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }
  }

  /**
   * class specific test for stacking above 100%.
   */
  @Test
  public void testCannotStackAbove100Percent() {
    Coupon coupon1;
    Coupon coupon2;
    coupon1 = new PercentOffCoupon("a", 50, true);
    coupon2 = new PercentOffCoupon("a", 51, true);
    try {
      coupon1.stackWith(coupon2);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }

    coupon1 = new PercentOffCoupon("a", 50, true);
    coupon2 = new PercentOffCoupon("a", 51, true);

    try {
      coupon2.stackWith(coupon1);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }
  }


  @Override
  public void testIsUsed() {
    CartItem item = CartItem.newCartItem("steak", 3, 10);
    Coupon pctOff = new PercentOffCoupon("steak", 10, true);
    Coupon pctOff2;
    assertFalse(pctOff.hasBeenUsed());
    pctOff.applyDiscount(item);
    assertTrue(pctOff.hasBeenUsed());

    item = CartItem.newCartItem("steak", 3, 10);
    pctOff = new PercentOffCoupon("steak", 10, true);
    pctOff2 = new PercentOffCoupon("steak", 10, true);

    Coupon stack = pctOff.stackWith(pctOff2);
    assertFalse(stack.hasBeenUsed());
    stack.applyDiscount(item);
    assertTrue(stack.hasBeenUsed());
  }


  @Override
  public void testCanStackWithDoesntHaveSideEffects() {
    Coupon coupon = new PercentOffCoupon("steak", 20, true);
    Coupon coupon2 = new PercentOffCoupon("steak", 20, true);
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
    Coupon coupon = new PercentOffCoupon("steak", 20, true);
    CartItem item = CartItem.newCartItem("steak", 2, 20);
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
  public void testStackMultiple() {
    CartItem bread = CartItem.newCartItem("bread", 2, 20);
    Coupon one;
    Coupon two;
    Coupon three;


    one = new PercentOffCoupon("bread", 25, true);
    two = new PercentOffCoupon("bread", 20, true);
    three = new PercentOffCoupon("bread", 5, true);

    Coupon stack1 = one.stackWith(two);
    Coupon stack2 = stack1.stackWith(three);

    CartItem item = stack2.applyDiscount(bread);

    assertEquals(20, item.getTotalPrice(), 0.01);

  }

  @Override
  public void testStackMultipleSameResult() {
    CartItem bread = CartItem.newCartItem("bread", 2, 20);
    Coupon one;
    Coupon two;
    Coupon three;
    CartItem result;
    Coupon stack1;
    Coupon stack2;

    one = new PercentOffCoupon("bread", 25, true);
    two = new PercentOffCoupon("bread", 20, true);
    three = new PercentOffCoupon("bread", 5, true);

    stack1 = one.stackWith(two);
    stack2 = stack1.stackWith(three);
    result = stack2.applyDiscount(bread);

    assertEquals(20, result.getTotalPrice(), 0.01);

    bread = CartItem.newCartItem("bread", 2, 20);
    one = new PercentOffCoupon("bread", 25, true);
    two = new PercentOffCoupon("bread", 20, true);
    three = new PercentOffCoupon("bread", 5, true);

    stack1 = three.stackWith(one);
    stack2 = stack1.stackWith(two);
    result = stack2.applyDiscount(bread);

    assertEquals(20, result.getTotalPrice(), 0.01);

    bread = CartItem.newCartItem("bread", 2, 20);
    one = new PercentOffCoupon("bread", 25, true);
    two = new PercentOffCoupon("bread", 20, true);
    three = new PercentOffCoupon("bread", 5, true);

    stack1 = two.stackWith(three);
    stack2 = stack1.stackWith(one);
    result = stack2.applyDiscount(bread);

    assertEquals(20, result.getTotalPrice(), 0.01);


  }

  @Override
  public void testFailsToStackUsedPart() {
    CartItem bread = CartItem.newCartItem("bread", 2, 20);
    Coupon one;
    Coupon two;
    Coupon three;
    Coupon stack1;
    Coupon stack2;


    one = new PercentOffCoupon("bread", 1, true);
    two = new PercentOffCoupon("bread", 2, true);
    three = new PercentOffCoupon("bread", 7, true);

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
    CartItem bread = CartItem.newCartItem("bread", 2, 20);
    Coupon one;
    Coupon two;
    Coupon three;
    Coupon stack1;
    Coupon stack2;


    one = new PercentOffCoupon("bread", 1, true);
    two = new PercentOffCoupon("bread", 2, true);
    three = new PercentOffCoupon("bread", 7, true);

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
    CartItem item = CartItem.newCartItem("name", 1, 2);
    Coupon coupon;

    assertFalse(new PercentOffCoupon("nam", 99, true).canApplyTo(item));

    coupon = new PercentOffCoupon("name", 50, true);
    Coupon coupon1 = new PercentOffCoupon("name", 25, true);
    coupon1 = coupon.stackWith(coupon1);
    coupon.applyDiscount(item);
    assertFalse(coupon1.canApplyTo(item));

  }

  @Override
  public void testCannotStackWithAnotherType() {
    PercentOffCoupon percentCoup = new PercentOffCoupon("beans", 50, true);
    BuyXGetYCoupon buyGet = new BuyXGetYCoupon("beans", 1, 1, true);
    AmountOffCoupon singleAmount = new AmountOffCoupon("beans", 3, true);

    assertFalse(percentCoup.canStackWith(buyGet));
    assertFalse(percentCoup.canStackWith(singleAmount));

    try {
      percentCoup.stackWith(buyGet);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }
    try {
      percentCoup.stackWith(singleAmount);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }

  }

  @Override
  public void testCannotDiscountDiscountedItem() {
    CartItem item = CartItem.newCartItem("food", 30, 30);
    Coupon coup = new PercentOffCoupon("food", 10, true);
    assertFalse(item.hasBeenDiscounted());
    Assert.assertTrue(coup.canApplyTo(item));
    coup.applyDiscount(item);
    assertFalse(coup.canApplyTo(item));
    Assert.assertTrue(item.hasBeenDiscounted());


    item = CartItem.newCartItem("food", 30, 30);
    coup = new PercentOffCoupon("food", 10, true);
    Coupon coup2 = new PercentOffCoupon("food", 10, true);
    assertFalse(item.hasBeenDiscounted());
    Assert.assertTrue(coup.canApplyTo(item));

    coup.stackWith(coup2).applyDiscount(item);
    assertFalse(coup.canApplyTo(item));
    Assert.assertTrue(item.hasBeenDiscounted());

  }
}
