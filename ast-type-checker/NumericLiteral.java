package ast2;

/**
 * ASTNode representing a numeric literal 
 */
public class NumericLiteral extends LiteralExp {
    public NumericLiteral(int number){
         this.number = number;
    }
    
    @Override
    public String text(){
      return String.valueOf(number);
    }

  @Override
  public void accept(ASTVisitor v) {
    v.visit(this);
  }

  private int number;
}