package coupons;

/**
 * An abstract representations of the operations a coupon can perform. Supports stacking.
 */
public interface Coupon {


  /**
   * determines if two coupons can stack. this is determined using general stackability (including
   * whether the coupon has been used), as well as coupon type and specific properties.
   *
   * @param other the coupon to check with this.
   * @return true if the two coupons can stack together.
   */
  boolean canStackWith(Coupon other);

  /**
   * determines if a coupon is stackable. this includes if the coupon has already been used.
   *
   * @return returns true if this coupon is stackable.
   */
  boolean isStackable();

  /**
   * Applies a coupon to an item.
   *
   * @param initial a given item.
   * @return returns a new item with applied discount.
   * @throws CouponUseException if discount is not applicable to the item.
   */
  CartItem applyDiscount(CartItem initial) throws CouponUseException;

  /**
   * stacks a coupon with another.
   *
   * @param other the coupon to stack with
   * @return returns the stacked coupon.
   * @throws CouponUseException if the coupons are not stackable.
   */
  Coupon stackWith(Coupon other) throws CouponUseException;

  /**
   * Determines if the coupon can be applied to the item. This can include making sure that the item
   * corresponds to the coupon, as well as other things such ase whether the coupon has already been
   * used.
   *
   * @param item the item to check against.
   * @return true if the coupon can be applied.
   */
  boolean canApplyTo(CartItem item);

  /**
   * tells if the coupon has been used once already.
   *
   * @return true if the coupon has been used.
   */
  boolean hasBeenUsed();


}
