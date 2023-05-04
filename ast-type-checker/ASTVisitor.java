package ast2;

/**
 * a visitor class for the ASTNode object implementations. ASTNode now has a method accept that
 * takes this interface in as a parameter for visitation.
 */
public interface ASTVisitor {

  /**
   * visitor method for the assignment node. this can be recursive as it has an expression in it.
   *
   * @param node the node to visit.
   */
  public void visit(Assignment node);

  /**
   * visitor method for the decl statement node. this cannot recursive as it has no other ASTNodes
   * in it.
   *
   * @param node the node to visit.
   */
  public void visit(DeclStmt node);

  /**
   * visitor method for the representation of a number.
   *
   * @param node the node to visit.
   */
  public void visit(NumericLiteral node);

  /**
   * visitor method for the binary operator plus node. this method ought to be recursive as it
   * should visit the two Exps/operands of PlusExp.
   *
   * @param node the node to visit.
   */
  public void visit(PlusExp node);

  /**
   * visitor method for the assignment node. this is the "inductive step" of the language so it
   * ought to be recursive.
   *
   * @param node the node to visit.
   */
  public void visit(Sequence node);

  /**
   * visitor method for the node representing a string in this mini language.
   *
   * @param node the node to visit.
   */
  public void visit(StringLiteral node);

  /**
   * visitor method for the varexp node. visits on the variable for reading via name.
   *
   * @param node the node to visit.
   */
  public void visit(VarExp node);

  /**
   * optional reporting method for post visitation data analysis.
   * @return returns a report specified by the implementation.
   */
  String report();
}
