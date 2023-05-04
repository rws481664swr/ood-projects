package ast2;

/**
 * ASTNode representing an assignment statement  
 */
public
class Assignment extends Stmt {
  public Assignment(String varName, Exp exp){
    this.varName = varName;
    this.exp = exp;
  }

  public String getVarName() {
    return varName;
  }

  public Exp getExp() {
    return exp;
  }

  @Override
  public String text(){
    return varName + " = " + exp.text();
  }

  @Override
  public void accept(ASTVisitor v) {
    v.visit(this);
  }

  private String varName;
  private Exp exp;
}