package ast2;

/**
 * ASTNode representing a binary "+" expression 
 */
public class PlusExp extends Exp {
    public PlusExp(Exp left, Exp right){
         this.left = left;
         this.right = right;
    }
    @Override
    public String text(){
         return left.text() + " + " + right.text();
    }

  @Override
  public void accept(ASTVisitor v) {
    v.visit(this);
  }

  public Exp getLeft() {
    return left;
  }

  public Exp getRight() {
    return right;
  }

  private Exp left;
    private Exp right;
}