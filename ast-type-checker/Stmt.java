package ast2;

/**
 * Root of the statement subhierarchy.  
 *
 */
public abstract class Stmt implements ASTNode {
    @Override
    public abstract String text();   
}
