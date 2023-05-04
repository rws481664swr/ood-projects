package ast2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;




public class TypeCheckVisitor implements ASTVisitor {
  private Set<String> declaredVars;
  private final StringBuilder typeSafteyReportBuilder;
  private final StringBuilder declarationRedundancyReportBuilder;
  private final Map<Exp, Exp> plusLeftChildren;
  private final Map<Exp, Exp> plusRightChildren;
  private final Map<Exp, ExpType> typeMap;

  public TypeCheckVisitor() {
    declaredVars = new HashSet<>();
    typeSafteyReportBuilder = new StringBuilder();

    plusLeftChildren = new HashMap<>();
    plusRightChildren = new HashMap<>();
    typeMap = new HashMap<>();
    declarationRedundancyReportBuilder = new StringBuilder();
  }

  @Override
  public void visit(Assignment node) {
    Exp exp = node.getExp();
    exp.accept(this);
  }

  @Override
  public void visit(DeclStmt node) {
    String varName = node.text().split(" ")[1];
    if (declaredVars.contains(varName)) {
      this.declarationRedundancyReportBuilder
              .append("duplicate declaration of var: ")
              .append(varName).append("\n");
    } else {
      declaredVars.add(varName);
    }
  }


  @Override
  public void visit(NumericLiteral node) {
    typeMap.put(node, ExpType.Number);
  }

  @Override
  public void visit(PlusExp node) {
    Exp left = node.getLeft();
    Exp right = node.getRight();

    plusLeftChildren.put(node, left);
    plusRightChildren.put(node, right);
    typeMap.put(node, ExpType.Recursive);
    left.accept(this);
    right.accept(this);

    ExpType lefType = typeMap.get(left);
    ExpType righType = typeMap.get(right);
    ExpType coType = getCoType(lefType, righType);
    //RESET
    if (coType.equals(ExpType.Illegal)) {
      typeSafteyReportBuilder.append("illegal exp: ")
              .append(node.text())
              .append("\n");
    }
    typeMap.put(node, coType);


  }

  private ExpType getCoType(ExpType left, ExpType right) {
    if (left.equals(right)) {
      if (left.equals(ExpType.Number)) {
        return left;
      } else if (left.equals(ExpType.String)) {
        return left;
      }else if(left.equals(ExpType.Variable)){
        return left;
      }
    } else if (left.equals(ExpType.Variable)) {
      return right;
    } else if (right.equals(ExpType.Variable)) {
      return left;
    }
    return ExpType.Illegal;
  }

  @Override
  public void visit(Sequence node) {
    node.getStat1().accept(this);
    node.getStat2().accept(this);

  }

  @Override
  public void visit(StringLiteral node) {
    typeMap.put(node, ExpType.String);
  }

  @Override
  public void visit(VarExp node) {
    typeMap.put(node, ExpType.Variable);

  }

  @Override
  public String report() {
    if (typeSafteyReportBuilder.length()
            + declarationRedundancyReportBuilder.length() == 0){
      return "A-OK!";
    }else {
      return typeSafteyReportBuilder
              .append(declarationRedundancyReportBuilder
                      .toString()).toString();
    }

  }

}



