package ast2;


public class PlusFactory {


  public static PlusExp plus(Exp exp, Exp exp2) {
    return new PlusExp(exp, exp2);
  }

  public static PlusExp plus(Exp exp, String literal) {
    return new PlusExp(exp, new StringLiteral(literal));
  }

  public static PlusExp plus(Exp exp, int literal) {
    return new PlusExp(exp, new NumericLiteral(literal));
  }

  public static PlusExp plus(String literal, Exp ex) {
    return new PlusExp(new StringLiteral(literal), ex);
  }

  public static PlusExp plus(int literal, Exp ex) {
    return new PlusExp(new NumericLiteral(literal), ex);
  }

  public static PlusExp plus(int number, int other) {
    return new PlusExp(new NumericLiteral(number), new NumericLiteral(number));
  }

  public static PlusExp plus(int number, String str) {
    return new PlusExp(new NumericLiteral(number), new StringLiteral(str));
  }

  public static PlusExp plus(String str, int number) {
    return new PlusExp(new StringLiteral(str), new NumericLiteral(number));
  }

  public static PlusExp plus(String str, String str2) {
    return new PlusExp(new StringLiteral(str), new StringLiteral(str2));
  }


}
