package application.pen;
/* Generated By:JJTree: Do not edit this line. ASTgColor.java */

public class ASTgColor extends SimpleNode {
  public ASTgColor(int id) {
    super(id);
  }

  public ASTgColor(IntVParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(IntVParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
