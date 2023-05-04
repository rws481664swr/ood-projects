package ast2;

/**
 * ASTNode representing a string literal 
 */
public class StringLiteral extends LiteralExp {
    public StringLiteral(String literal){
         this.literal = literal;
    }
    
    @Override
    public String text(){
      return "\"" + literal + "\"";
    }

  @Override
  public void accept(ASTVisitor v) {
    v.visit(this);
  }

  private String literal;
}