package ast2;

/**
 * Root of the AST Node hierarchy.  
 *
 */
public interface ASTNode {
  /**
   * Generate textual representation for subtree rooted at this node. 
   */
  public String text();

  /**
   * Allows a visitor.
   */
  public void accept(ASTVisitor v);
}