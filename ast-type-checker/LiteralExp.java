package ast2;

/**
 * ASTNode representing a literal expression 
 */
public abstract class LiteralExp extends Exp {
    @Override
    public abstract String text();

    protected enum Type{
        NUMBER,
        STRING;
    }
}
