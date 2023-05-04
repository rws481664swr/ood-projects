import org.junit.Test;

import coupons.Coupon;
import coupons.CouponUseException;

import static org.junit.Assert.assertFalse;

/**
 * Abstract test template for the {@link Coupon} interface.
 */
public abstract class CouponTest {

  /**
   * helper method gets a type of coupon with set default values.
   *
   * @param stackable if is stackable.
   * @return the coupon.
   */
  protected abstract Coupon getDefault(boolean stackable);

  /**
   * tests the toString for the given coupon type.
   */
  @Test
  public abstract void testToString();

  /**
   * tests the constructor for the given coupon type.
   */
  @Test
  public abstract void testConstructor();

  /**
   * tests basic Coupon application for the given coupon type.
   */
  @Test
  public abstract void testApplyDiscountToItemSingleCoupon();

  /**
   * tests applying a stacked coupon to an item for the given coupon type.
   */
  @Test
  public abstract void testApplyDiscountToItemStackedCoupon();

  /**
   * tests the isStackable method and general stacking rules for the given coupon type.
   */
  @Test
  public abstract void testStackableAgainstItsOwnKindOnSameItem();

  /**
   * tests expectation to fail for stacking on different items for the given coupon type.
   */
  @Test
  public abstract void testStackableAgainstOwnKindDifferentItem();

  /**
   * tests coupon cannot stack onto itsself.
   */
  @Test
  public abstract void cannotStackWithItself();

  /**
   * tests the basic no re-use rule for the given coupon type.
   */
  @Test
  public abstract void cannotReuseCoupon();

  /**
   * tests cannot earn money from coupon for the given coupon type.
   */
  @Test
  public abstract void testGetForFree();

  /**
   * tests inability to stack a used coupon for the given coupon type.
   */
  @Test
  public abstract void testCannotStackUsedAndUnused();

  /**
   * tests that you cannot get the discount for the given coupon type.
   */
  @Test
  public abstract void testApplyCouponToIncorrectItem();

  /**
   * tests for unintended side effects in canStack method in given  coupon type.
   */
  @Test
  public abstract void testCanStackWithDoesntHaveSideEffects();

  /**
   * tests for unintended side effects in canApply method in given coupon type.
   */
  @Test
  public abstract void testCanApplyDoesntHaveSideEffects();

  /**
   * tests the isUsed method in the coupon interface.
   */
  @Test
  public abstract void testIsUsed();

  /**
   * various tests for stacking multiple coupons together.
   */
  @Test
  public abstract void testStackMultiple();

  /**
   * tests stacking coupons in different order ensuring same result.
   */
  @Test
  public abstract void testStackMultipleSameResult();

  /**
   * tests if one of the coupons in a stack is used after stacking that that stack cannot stack
   * anymore.
   */
  @Test
  public abstract void testFailsToStackUsedPart();

  /**
   * tests if one of the coupons in a stack is used after stacking that that stack cannot apply a
   * discount anymore.
   */
  @Test
  public abstract void testStackFailsToApplyUsedPart();

  /**
   * tests canApplyTo when there are various reasons for it to not apply.
   */
  @Test
  public abstract void testCanApplyFails();


  /**
   * tests no type can stack with another.
   */
  @Test
  public abstract void testCannotStackWithAnotherType();

  /**
   * tests a discounted item can't receive a new discount.
   */
  @Test
  public abstract void testCannotDiscountDiscountedItem();


  /**
   * tests true/false stacking.
   */
  @Test
  public void testNotStackableWithOthers() {
    Coupon coupon1;
    Coupon coupon2;

    coupon1 = getDefault(false);
    coupon2 = getDefault(true);

    assertFalse(coupon1.canStackWith(coupon2));
    assertFalse(coupon2.canStackWith(coupon1));
    try {
      coupon1.stackWith(coupon2);
    } catch (CouponUseException e) {
      e.getMessage();
    }
    //clean start.
    coupon1 = getDefault(false);
    assertFalse(coupon1.isStackable());
    coupon2 = getDefault(true);
    try {
      coupon2.stackWith(coupon1);
    } catch (CouponUseException e) {
      e.getMessage();
    }
  }


}
