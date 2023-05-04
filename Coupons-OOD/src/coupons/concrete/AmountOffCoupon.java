package coupons.concrete;

import coupons.AbstractCoupon;
import coupons.CartItem;
import coupons.Coupon;

/**
 * A {@link Coupon} implementation where the discount is a set amount.
 */
public class AmountOffCoupon extends AbstractCoupon {

  private final double amountOff;

  /**
   * The basic constructor for the {@link AmountOffCoupon}.
   *
   * @param name      item name.
   * @param amountOff dollar amount off for item.
   * @param stackable whether or not coupon can be stacked.
   */
  public AmountOffCoupon(String name, double amountOff, boolean stackable) {
    super(name, stackable);
    if (amountOff <= 0) {
      throw new IllegalArgumentException("amount off must be >0");
    }
    this.amountOff = amountOff;
  }

  /**
   * The stacking internal constructor for the {@link AmountOffCoupon}.
   *
   * @param amountOff dollar amount off for item.
   * @param coupon1   usually the caller).
   * @param coupon2   the "other" {@link Coupon} called in stacking.
   */
  private AmountOffCoupon(double amountOff, AmountOffCoupon coupon1, AmountOffCoupon coupon2) {
    super(coupon1, coupon2);
    this.amountOff = amountOff;
  }

  @Override
  protected String toStringHelper() {
    return "$" + String.format("%.2f", amountOff) + " off item " + itemName;
  }

  @Override
  protected boolean canStackWithHelper(Coupon other) {
    return other instanceof AmountOffCoupon;
  }

  @Override
  protected boolean canApplyToHelper(CartItem initial) {
    return amountOff < initial.getUnitPrice();
  }

  @Override
  protected Coupon stackHelper(Coupon other) throws CouponStackException {
    AmountOffCoupon casted = ((AmountOffCoupon) other);
    double amtOff = this.amountOff + casted.amountOff;
    return new AmountOffCoupon(amtOff, this, casted);
  }

  @Override
  protected CartItem applyHelper(CartItem item) throws CouponApplicationException {
    double price = item.getUnitPrice() - amountOff;
    return CartItem.discountedCartItem(item, price);
  }
}
