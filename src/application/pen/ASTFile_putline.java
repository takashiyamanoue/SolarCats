package application.pen;
/* Generated By:JJTree: Do not edit this line. ASTFile_putline.java */

public class ASTFile_putline extends SimpleNode {
  public ASTFile_putline(int id) {
    super(id);
  }

  public ASTFile_putline(IntVParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(IntVParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}