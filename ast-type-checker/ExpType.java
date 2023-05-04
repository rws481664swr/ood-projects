package ast2;

public enum ExpType {
  Number(){
    @Override
    public java.lang.String toString() {
      throw new UnsupportedOperationException("NumericLiteral");
    }
  },
  String(){
    @Override
    public java.lang.String toString() {
      return "StringLiteral";    }
  },
  Variable(){
    @Override
    public java.lang.String toString() {
      return "VarExp";    }
  },
  Recursive(){
    @Override
    public java.lang.String toString() {
      return "PlusExp";
    }
  },
  Illegal(){
    @Override
    public java.lang.String toString() {
     return  "ILLEGAL STATE";
    }
  };
  public  boolean isLegalCombo(ExpType expType){
    return comboTest(this, expType);
  }

  @Override
  public abstract String toString();


//  public static ExpType getType(Exp expression) {
//    if (expression instanceof NumericLiteral) {
//      return ExpType.Number;
//    } else if (expression instanceof StringLiteral) {
//      return ExpType.String;
//
//    } else if (expression instanceof VarExp) {
//      return ExpType.Variable;
//
//    } else if (expression instanceof PlusExp) {
//      return ExpType.Recursive;
//
//    } else {
//      return ExpType.Illegal;
//    }
//  }

  public String getComboStateMessage(ExpType second){
    switch (this){
      case Number:
        if (second.equals(ExpType.Number)){
          return "both are NumericLiterals";
        }
      case String:
        if (second.equals(String)){
          return "both are StringLiterals";
        }
      case Illegal:
        return Illegal + " and "+ second;
      case Variable:
        return    Variable + " and "+ second;
      case Recursive:
        return   Recursive + " and "+ second;
    }
    return Illegal.toString();
  }
  private boolean comboTest(ExpType first, ExpType second){
    switch (first){
      case Number:
        if (second.equals(ExpType.Number)){
          return true;
        }
      case String:
        if (second.equals(String)){
          return true;
        }
      case Illegal:
        return false;
      case Variable:
      case Recursive:
         return true;
    }
    return false;
  }

}