package coupons.concrete;

import coupons.AbstractCoupon;
import coupons.CartItem;
import coupons.Coupon;

/**
 * Implementation of {@link Coupon} representing the discount as a percent off.
 */
public class PercentOffCoupon extends AbstractCoupon {
  public final int percentOff;

  /**
   * Basic Constructor for the {@link PercentOffCoupon}.
   *
   * @param name       item name.
   * @param percentOff percent off.
   * @param stackable  if the coupon can be stacked.
   * @throws IllegalArgumentException if percentOff is not between 0 and 100 exclusive.
   */
  public PercentOffCoupon(String name, int percentOff, boolean stackable) {
    super(name, stackable);
    if (percentOff <= 0 || percentOff > 100) {
      throw new IllegalArgumentException("percent must be <100 and >0");
    }
    this.percentOff = percentOff;
  }

  /**
   * The internal stacking constructor for the {@link PercentOffCoupon}.
   *
   * @param pctOff  ther percent off combination from stacking.
   * @param coupon1 usually the caller).
   * @param coupon2 the "other" {@link Coupon} called in stacking.
   */
  private PercentOffCoupon(int pctOff, PercentOffCoupon coupon1, PercentOffCoupon coupon2) {
    super(coupon1, coupon2);
    this.percentOff = pctOff;
  }

  @Override
  protected String toStringHelper() {
    return percentOff + "% off item " + itemName;
  }

  @Override
  protected boolean canStackWithHelper(Coupon other) {
    if (!(other instanceof PercentOffCoupon)) {
      return false;
    }
    return (percentOff + ((PercentOffCoupon) other).percentOff <= 100);
  }

  @Override
  protected boolean canApplyToHelper(CartItem initial) {
    //no extra constraints.
    return true;
  }

  @Override
  protected Coupon stackHelper(Coupon other) throws CouponStackException {
    PercentOffCoupon perOther = (PercentOffCoupon) other;
    int pctOff = percentOff + perOther.percentOff;
    return new PercentOffCoupon(pctOff, this, perOther);
  }

  @Override
  protected CartItem applyHelper(CartItem item) throws CouponApplicationException {
    double discountPrice = item.getUnitPrice() * (1 - percentOff / 100.0);
    return CartItem.discountedCartItem(item, discountPrice);
  }


}
