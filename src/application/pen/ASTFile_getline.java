package application.pen;
/* Generated By:JJTree: Do not edit this line. ASTFile_getline.java */

public class ASTFile_getline extends SimpleNode {
  public ASTFile_getline(int id) {
    super(id);
  }

  public ASTFile_getline(IntVParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(IntVParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
