package application.pen;
/* Generated By:JJTree: Do not edit this line. ASTSubstring.java */

public class ASTSubstring extends SimpleNode {
	public ASTSubstring(int id) {
		super(id);
	}

	public ASTSubstring(IntVParser p, int id) {
		super(p, id);
	}


	/** Accept the visitor. **/
	public Object jjtAccept(IntVParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
