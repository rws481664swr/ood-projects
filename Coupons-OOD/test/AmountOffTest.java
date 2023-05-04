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
 * test class for AmountOffCoupon.
 */
public class AmountOffTest extends CouponTest {

  @Override
  protected Coupon getDefault(boolean stackable) {
    return new AmountOffCoupon("item", 1, stackable);
  }

  @Override
  public void testToString() {
    Coupon coupon = new AmountOffCoupon("eggs", 1, false);
    assertEquals("$1.00 off item eggs", coupon.toString());
    coupon = new AmountOffCoupon("eggs", 1, true);
    assertEquals("$1.00 off item eggs", coupon.toString());
  }

  @Override
  public void testConstructor() {
    for (boolean b : new boolean[]{false, true}) {
      try {
        new AmountOffCoupon("", 1, b);
        fail();
      } catch (IllegalArgumentException e) {
        e.getMessage();
      }
      try {
        new AmountOffCoupon("itemname", -1, b);
        fail();
      } catch (IllegalArgumentException e) {
        e.getMessage();
      }
    }
  }

  @Override
  public void testApplyDiscountToItemSingleCoupon() {
    CartItem eggs = CartItem.newCartItem("eggs", 1, 2.50);
    Coupon couponForEggs = new AmountOffCoupon("eggs", 1, false);
    CartItem discountedEggs = couponForEggs.applyDiscount(eggs);
    assertEquals(1, discountedEggs.getQuantity());
    assertEquals(1.50, discountedEggs.getTotalPrice(), .01);

    CartItem milk = CartItem.newCartItem("milk", 4, 3);
    Coupon couponForMilk = new AmountOffCoupon("milk", 1, false);
    CartItem discountedMilk = couponForMilk.applyDiscount(milk);
    assertEquals(4, discountedMilk.getQuantity());
    assertEquals(8, discountedMilk.getTotalPrice(), .01);

  }

  @Override
  public void testApplyDiscountToItemStackedCoupon() {
    CartItem steak = CartItem.newCartItem("steak", 2, 12);
    Coupon steakCoupon = new AmountOffCoupon("steak", 1, true);
    Coupon secondCouponForSteak;


    CartItem discountedSteak = steakCoupon.applyDiscount(steak);
    assertEquals(2, discountedSteak.getQuantity());
    assertEquals(22, discountedSteak.getTotalPrice(), .01);


    steakCoupon = new AmountOffCoupon("steak", 1, true);
    secondCouponForSteak = new AmountOffCoupon("steak", 2, true);

    steak = CartItem.newCartItem("steak", 2, 12);
    discountedSteak = steakCoupon.stackWith(secondCouponForSteak).applyDiscount(steak);
    assertEquals(2, discountedSteak.getQuantity());
    assertEquals(18, discountedSteak.getTotalPrice(), .01);


    steakCoupon = new AmountOffCoupon("steak", 1, true);
    secondCouponForSteak = new AmountOffCoupon("steak", 2, true);
    steak = CartItem.newCartItem("steak", 2, 12);
    //test for commutativity
    discountedSteak = secondCouponForSteak.stackWith(steakCoupon).applyDiscount(steak);
    assertEquals(2, discountedSteak.getQuantity());
    assertEquals(18, discountedSteak.getTotalPrice(), .01);

  }

  @Override
  public void testStackableAgainstItsOwnKindOnSameItem() {
    Coupon coupon = new AmountOffCoupon("cartItem", 1, true);
    Coupon coupon2 = new AmountOffCoupon("cartItem", 1, true);
    assertTrue(coupon.canStackWith(coupon2));
    assertTrue(coupon2.canStackWith(coupon));
  }

  @Override
  public void testStackableAgainstOwnKindDifferentItem() {
    Coupon eggs = new AmountOffCoupon("eggs", 1, true);
    Coupon milk = new AmountOffCoupon("milk", 2, true);

    assertFalse(eggs.canStackWith(milk));
    assertFalse(milk.canStackWith(eggs));
    try {
      eggs.stackWith(milk);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }

    eggs = new AmountOffCoupon("eggs", 1, true);
    milk = new AmountOffCoupon("milk", 2, true);
    try {
      milk.stackWith(eggs);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }
  }

  @Override
  public void cannotStackWithItself() {
    Coupon coupon = new AmountOffCoupon("item", 1, true);
    assertFalse(coupon.canStackWith(coupon));
    try {
      coupon.stackWith(coupon);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }
  }

  @Override
  public void cannotReuseCoupon() {
    CartItem cheddar = CartItem.newCartItem("cheddar", 1, 2);
    Coupon coupon = new AmountOffCoupon("cheddar", 1, true);
    coupon.applyDiscount(cheddar);
    try {
      coupon.applyDiscount(cheddar);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }
  }

  @Override
  public void testGetForFree() {
    String milkName = "milk";
    Coupon coupon = new AmountOffCoupon(milkName, 5, true);
    CartItem milk = CartItem.newCartItem(milkName, 1, 5);

    try {
      coupon.applyDiscount(milk);
      fail();
    } catch (CouponUseException c) {
      c.getMessage();
    }

    Coupon coupon1 = new AmountOffCoupon(milkName, 1, true);
    milk = CartItem.newCartItem(milkName, 1, 5);
    coupon = new AmountOffCoupon(milkName, 1, true);
    Coupon stack = coupon.stackWith(coupon1);
    coupon.applyDiscount(milk);
    try {
      stack.applyDiscount(milk);
      fail();
    } catch (CouponUseException c) {
      c.getMessage();
    }

  }

  @Override
  public void testCannotStackUsedAndUnused() {
    CartItem a = CartItem.newCartItem("a", 1, 32);
    Coupon one;
    Coupon two;
    one = new AmountOffCoupon("a", 2, true);
    two = new AmountOffCoupon("a", 5, true);
    one.applyDiscount(a);


    try {
      one.stackWith(two);
    } catch (CouponUseException e) {
      assertFalse(one.canStackWith(two));
    }


    one = new AmountOffCoupon("a", 2, true);
    two = new AmountOffCoupon("a", 5, true);
    a = CartItem.newCartItem("a", 1, 32);
    two.applyDiscount(a);
    try {
      one.stackWith(two);
    } catch (CouponUseException e) {
      assertFalse(one.canStackWith(two));
    }
  }

  @Override
  public void testApplyCouponToIncorrectItem() {
    CartItem steak = CartItem.newCartItem("steak", 2, 2);
    Coupon couponForBeans = new AmountOffCoupon("beans", 5, false);
    try {
      couponForBeans.applyDiscount(steak);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }

  }

  /**
   * test specific to amount makes sure that the discount isn't >=price.
   */
  @Test
  public void cannotDiscountAbovePrice() {
    CartItem milk = CartItem.newCartItem("milk", 2, 10);
    Coupon coupon = new AmountOffCoupon("milk", 100, true);
    try {
      coupon.applyDiscount(milk);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }

    milk = CartItem.newCartItem("milk", 1, 20);
    coupon = new AmountOffCoupon("milk", 10, true);
    Coupon coupon2 = new AmountOffCoupon("milk", 10, true);
    coupon = coupon.stackWith(coupon2);
    try {
      coupon.applyDiscount(milk);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }

    milk = CartItem.newCartItem("milk", 1, 19);
    coupon = new AmountOffCoupon("milk", 10, true);
    coupon2 = new AmountOffCoupon("milk", 10, true);
    coupon = coupon.stackWith(coupon2);
    try {
      coupon.applyDiscount(milk);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }
  }

  @Override
  public void testCanStackWithDoesntHaveSideEffects() {
    Coupon coupon = new AmountOffCoupon("steak", 10, true);
    Coupon coupon2 = new AmountOffCoupon("steak", 5, true);
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
    Coupon coupon = new AmountOffCoupon("steak", 10, true);
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
  public void testIsUsed() {
    CartItem item = CartItem.newCartItem("steak", 3, 10);
    Coupon singleAmountOffCoupon = new AmountOffCoupon("steak", 5, true);
    Coupon singleAmountOffCoupon2;
    assertFalse(singleAmountOffCoupon.hasBeenUsed());
    item = CartItem.newCartItem("steak", 3, 10);
    singleAmountOffCoupon.applyDiscount(item);
    assertTrue(singleAmountOffCoupon.hasBeenUsed());


    singleAmountOffCoupon = new AmountOffCoupon("steak", 2, true);
    singleAmountOffCoupon2 = new AmountOffCoupon("steak", 2, true);
    item = CartItem.newCartItem("steak", 3, 10);


    Coupon stack = singleAmountOffCoupon.stackWith(singleAmountOffCoupon2);
    assertFalse(stack.hasBeenUsed());
    stack.applyDiscount(item);
    assertTrue(stack.hasBeenUsed());
  }

  @Override
  public void testStackMultiple() {
    CartItem bread = CartItem.newCartItem("bread", 2, 20);
    Coupon one;
    Coupon two;
    Coupon three;


    one = new AmountOffCoupon("bread", 1, true);
    two = new AmountOffCoupon("bread", 2, true);
    three = new AmountOffCoupon("bread", 7, true);

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


    one = new AmountOffCoupon("bread", 1, true);
    two = new AmountOffCoupon("bread", 2, true);
    three = new AmountOffCoupon("bread", 7, true);

    stack1 = one.stackWith(two);
    stack2 = stack1.stackWith(three);
    result = stack2.applyDiscount(bread);

    assertEquals(20, result.getTotalPrice(), 0.01);

    bread = CartItem.newCartItem("bread", 2, 20);
    one = new AmountOffCoupon("bread", 1, true);
    two = new AmountOffCoupon("bread", 2, true);
    three = new AmountOffCoupon("bread", 7, true);

    stack1 = three.stackWith(one);
    stack2 = stack1.stackWith(two);
    result = stack2.applyDiscount(bread);

    assertEquals(20, result.getTotalPrice(), 0.01);

    bread = CartItem.newCartItem("bread", 2, 20);
    one = new AmountOffCoupon("bread", 1, true);
    two = new AmountOffCoupon("bread", 2, true);
    three = new AmountOffCoupon("bread", 7, true);

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


    one = new AmountOffCoupon("bread", 1, true);
    two = new AmountOffCoupon("bread", 2, true);
    three = new AmountOffCoupon("bread", 7, true);

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


    one = new AmountOffCoupon("bread", 1, true);
    two = new AmountOffCoupon("bread", 2, true);
    three = new AmountOffCoupon("bread", 7, true);

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
    CartItem item = CartItem.newCartItem("name", 1, 10);
    Coupon coupon;

    assertFalse(new AmountOffCoupon("nam", 1, true).canApplyTo(item));

    //test stacked cant apply after one in stack is used.
    coupon = new AmountOffCoupon("name", 2, true);
    Coupon coupon1 = new AmountOffCoupon("name", 3, true);
    coupon1 = coupon.stackWith(coupon1);
    coupon.applyDiscount(item);
    item = CartItem.newCartItem("name", 1, 10);
    assertFalse(coupon1.canApplyTo(item));

    coupon = new AmountOffCoupon("name", 11, true);
    assertFalse(coupon.canApplyTo(item));

    //stacked can't apply  after use
    coupon = new AmountOffCoupon("name", 2, true);
    coupon1 = new AmountOffCoupon("name", 9, true);
    coupon1 = coupon.stackWith(coupon1);
    coupon.applyDiscount(item);
    item = CartItem.newCartItem("name", 1, 10);
    assertFalse(coupon1.canApplyTo(item));

  }

  @Override
  public void testCannotStackWithAnotherType() {
    PercentOffCoupon percentCoup = new PercentOffCoupon("beans", 50, true);
    BuyXGetYCoupon buyGet = new BuyXGetYCoupon("beans", 1, 1, true);
    AmountOffCoupon singleAmount = new AmountOffCoupon("beans", 3, true);

    assertFalse(singleAmount.canStackWith(buyGet));
    assertFalse(singleAmount.canStackWith(percentCoup));
    try {
      singleAmount.stackWith(buyGet);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }
    try {
      singleAmount.stackWith(percentCoup);
      fail();
    } catch (CouponUseException e) {
      e.getMessage();
    }
  }

  @Override
  public void testCannotDiscountDiscountedItem() {
    CartItem item = CartItem.newCartItem("food", 30, 30);
    Coupon coup = new AmountOffCoupon("food", 1, true);
    assertFalse(item.hasBeenDiscounted());
    assertTrue(coup.canApplyTo(item));
    coup.applyDiscount(item);
    assertFalse(coup.canApplyTo(item));
    assertTrue(item.hasBeenDiscounted());


    item = CartItem.newCartItem("food", 30, 30);
    coup = new AmountOffCoupon("food", 1, true);
    Coupon coup2 = new AmountOffCoupon("food", 1, true);
    assertFalse(item.hasBeenDiscounted());
    assertTrue(coup.canApplyTo(item));

    coup.stackWith(coup2).applyDiscount(item);
    assertFalse(coup.canApplyTo(item));
    assertTrue(item.hasBeenDiscounted());

  }
}
