package application.pen;
/* Generated By:JJTree: Do not edit this line. ASTSurNode.java */

public class ASTSurNodeX extends SimpleNode {
  public ASTSurNodeX(int id) {
    super(id);
  }

  public ASTSurNodeX(IntVParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(IntVParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
