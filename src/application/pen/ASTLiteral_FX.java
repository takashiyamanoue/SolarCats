package application.pen;

/* Generated By:JJTree: Do not edit this line. ASTLiteral.java */

public class ASTLiteral_FX extends SimpleNode {
  public ASTLiteral_FX(int id) {
    super(id);
  }

  public ASTLiteral_FX(IntVParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(IntVParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }

/** customizing codes **/
    public float litValue;

}