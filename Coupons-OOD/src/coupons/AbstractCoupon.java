package coupons;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract model for the implementation of the Coupon interface.
 */
public abstract class AbstractCoupon implements Coupon {

  private final boolean isStackable;
  private boolean used;
  protected final String itemName;
  protected final List<Coupon> stackOfCoupons;

  /**
   * Constructor for the {@link AbstractCoupon}class.
   *
   * @param name        the name of the item that the coupon discounts.
   * @param isStackable whether this coupon can be stacked with others like it.
   */
  protected AbstractCoupon(String name, boolean isStackable) {
    if (name == null || name.length() == 0) {
      throw new IllegalArgumentException("bad item getName");
    }
    stackOfCoupons = new ArrayList<>();
    stackOfCoupons.add(this);
    used = false;
    this.isStackable = isStackable;
    itemName = name;
  }

  /**
   * Stacking coupon constructor.
   *
   * @param coupon1 the first of two {@link AbstractCoupon}s to stack.
   * @param coupon2 the second of two {@link AbstractCoupon}s to stack.
   */
  protected AbstractCoupon(AbstractCoupon coupon1, AbstractCoupon coupon2) {
    stackOfCoupons = new ArrayList<>();
    for (Coupon coup : coupon1.stackOfCoupons) {
      if (!stackOfCoupons.contains(coup)) {
        stackOfCoupons.add(coup);
      }
    }
    for (Coupon coup : coupon2.stackOfCoupons) {
      if (!stackOfCoupons.contains(coup)) {
        stackOfCoupons.add(coup);
      }
    }
    used = false;
    isStackable = true;
    itemName = coupon1.itemName;
  }

  @Override
  public boolean hasBeenUsed() {
    return used;
  }

  @Override
  public final boolean canStackWith(Coupon other) {
    return (this != other
            && isStackable
            && other.isStackable()
            && !used
            && !other.hasBeenUsed()
            && (other instanceof AbstractCoupon)
            && itemName.equals(((AbstractCoupon) other).itemName)
            && canStackWithHelper(other));
  }

  @Override
  public Coupon stackWith(Coupon other) throws CouponUseException {

    if (canStackWith(other)) {
      //can cast because canStackWithOther confirms super-type as AbstractCoupon.
      return stackHelper(other);
    } else {
      throw new CouponStackException("cannot stack " + this + " with " + other);
    }
  }


  @Override
  public final CartItem applyDiscount(CartItem initial) {
    if (!canApplyTo(initial)) {
      throw new CouponApplicationException("cannot apply coupon \"" + this + "\" to " + initial);
    }
    used = true;
    CartItem toReturn = applyHelper(initial);
    for (Coupon coup : stackOfCoupons) {
      if (coup instanceof AbstractCoupon) {
        ((AbstractCoupon) coup).used = true;
      }
    }
    return toReturn;
  }


  @Override
  public final boolean canApplyTo(CartItem item) {
    if (used
            || !itemName.equals(item.getName())
            || item.hasBeenDiscounted()) {
      return false;
    }
    for (Coupon coup : stackOfCoupons) {
      if (coup.hasBeenUsed()) {
        return false;
      }
    }
    return canApplyToHelper(item);
  }


  @Override
  public final boolean isStackable() {
    return isStackable;
  }

  @Override
  public final String toString() {
    return toStringHelper();
  }

  /**
   * class dependent determination if stackable.
   *
   * @param other thing to compare against.
   * @return true if can stack with.
   */
  protected abstract boolean canStackWithHelper(Coupon other);

  /**
   * class dependent determination if applicable.
   *
   * @param initial thing to compare against.
   * @return true if can apply to.
   */
  protected abstract boolean canApplyToHelper(CartItem initial);

  /**
   * performs class dependent stack actions.
   *
   * @param toStackWIth the coupon to stack with.
   * @return returns the stacked coupon.
   * @throws CouponStackException if cannot stack.
   */
  protected abstract Coupon stackHelper(Coupon toStackWIth) throws CouponStackException;

  /**
   * performs class dependent coupon  operations.
   *
   * @param item the item to apply the coupon to.
   * @return returns the discounted {@link CartItem}.
   * @throws CouponApplicationException if cannot apply the coupon.
   */
  protected abstract CartItem applyHelper(CartItem item) throws CouponApplicationException;


  /**
   * An internal implementation of toString to force An override of toString.
   *
   * @return the toString value.
   */
  protected abstract String toStringHelper();


  /**
   * An specification of the {@link CouponUseException} for Stacking errors.
   */
  protected class CouponStackException extends CouponUseException {
    /**
     * Constructor for coupon stacking failure.
     *
     * @param msg the message.
     */
    public CouponStackException(String msg) {
      super(msg);
    }
  }

  /**
   * An specification of the {@link CouponUseException} for describing discount application errors.
   */
  protected class CouponApplicationException extends CouponUseException {

    /**
     * Constructor for coupon application failure.
     *
     * @param msg the message.
     */
    public CouponApplicationException(String msg) {
      super(msg);
    }
  }
}
