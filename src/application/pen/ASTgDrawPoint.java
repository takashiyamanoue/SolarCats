package application.pen;
/* Generated By:JJTree: Do not edit this line. ASTgDrawPoint.java */

public class ASTgDrawPoint extends SimpleNode {
  public ASTgDrawPoint(int id) {
    super(id);
  }

  public ASTgDrawPoint(IntVParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(IntVParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
