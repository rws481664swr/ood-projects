package coupons;

/**
 * a superclass for exceptions surronding the {@link Coupon} interface.
 */
public abstract class CouponUseException extends RuntimeException {

  /**
   * Message constructor.
   *
   * @param message the message.
   */
  public CouponUseException(String message) {
    super(message);
  }
}
