package application.pen;
/* Generated By:JJTree: Do not edit this line. ASTNull.java */

public class ASTEOF_STR extends SimpleNode {
  public ASTEOF_STR(int id) {
    super(id);
  }

  public ASTEOF_STR(IntVParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(IntVParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }

	/** customizing codes **/
	public int char_code;
}