package ast2;


public class ASTStaticFactory {
  public static Stmt sequence(Stmt one, Stmt two) {
    return new Sequence(one, two);
  }

  public static Stmt assignment(String val, Exp expr) {
    return new Assignment(val, expr);
  }

  public static Stmt decl(String val) {
    return new DeclStmt(val);
  }

  public static Exp varExp(String varName) {
    return new VarExp(varName);
  }

  public static Exp plus(Exp e1, Exp e2) {
    return new PlusExp(e1, e2);
  }

  public static Exp strLiteral(String literal) {
    return new StringLiteral(literal);
  }

  public static Exp numLiteral(int num) {
    return new NumericLiteral(num);
  }


}
