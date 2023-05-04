package coupons.concrete;

import coupons.AbstractCoupon;
import coupons.CartItem;
import coupons.Coupon;

/**
 * A class representing coupons of the type buy X get Y for free.
 */
public class BuyXGetYCoupon extends AbstractCoupon {
  private final int buy;
  private final int get;

  /**
   * A standard {@link BuyXGetYCoupon} constructor.
   *
   * @param name      item name.
   * @param buy       the "buy" amount.
   * @param get       the "get free" amount.
   * @param stackable whether the coupon is stackable or not.
   */
  public BuyXGetYCoupon(String name, int buy, int get, boolean stackable) {
    super(name, stackable);
    if (buy < 0 || get <= 0) {
      throw new IllegalArgumentException();
    }
    this.buy = buy;
    this.get = get;
  }

  /**
   * the Stacking constructor used internally.
   *
   * @param buy     the buy value.
   * @param get     the get free value.
   * @param coupon1 usually the caller).
   * @param coupon2 the "other" {@link Coupon} called in stacking
   */
  private BuyXGetYCoupon(int buy, int get, BuyXGetYCoupon coupon1, BuyXGetYCoupon coupon2) {
    super(coupon1, coupon2);
    this.buy = buy;
    this.get = get;
  }

  @Override
  protected boolean canStackWithHelper(Coupon other) {
    return other instanceof BuyXGetYCoupon;
  }

  @Override
  protected boolean canApplyToHelper(CartItem initial) {
    return initial.getQuantity() >= buy + get;
  }

  @Override
  protected Coupon stackHelper(Coupon other) {
    int[] theXandTheY = whichCompare((BuyXGetYCoupon) other);
    return new BuyXGetYCoupon(theXandTheY[0], theXandTheY[1], this, ((BuyXGetYCoupon) other));
  }

  @Override
  protected CartItem applyHelper(CartItem item) {
    int buyPlusGet = buy + get;
    int numBuysPlusGets = item.getQuantity() / buyPlusGet;
    int cannotUseCouponQty = item.getQuantity() % buyPlusGet;
    int totalQuantToPayFor = (numBuysPlusGets * buy + cannotUseCouponQty);

    double unitPrice = totalQuantToPayFor
            * item.getUnitPrice() / item.getQuantity();
    return CartItem.discountedCartItem(item, unitPrice);
  }

  @Override
  protected String toStringHelper() {
    return "Buy " + buy + " get " + get + " free";
  }

  /**
   * The ratio comparison between this and another.
   *
   * @param other the {@link BuyXGetYCoupon} to compare with.
   * @return an array of type int = {buy, get} for the better ratio.
   */
  private int[] whichCompare(BuyXGetYCoupon other) {
    double ratio1 = (double) this.get / this.buy;
    double ratio2 = (double) other.get / other.buy;
    if (ratio1 > ratio2) {
      return new int[]{buy, get};
    } else if (ratio2 > ratio1) {
      return new int[]{other.buy, other.get};
    } else {
      if (buy < other.buy) {
        return new int[]{buy, get};
      } else {
        return new int[]{other.buy, other.get};
      }
    }
  }
}
