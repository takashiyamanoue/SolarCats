package application.pen;
/* Generated By:JJTree: Do not edit this line. ASTgDrawBox.java */

public class ASTgDrawBox extends SimpleNode {
  public ASTgDrawBox(int id) {
    super(id);
  }

  public ASTgDrawBox(IntVParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(IntVParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
