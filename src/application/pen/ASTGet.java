package application.pen;
/* Generated By:JJTree: Do not edit this line. ASTGet.java */

public class ASTGet extends SimpleNode {
	public ASTGet(int id) {
		super(id);
	}

	public ASTGet(IntVParser p, int id) {
		super(p, id);
	}


	/** Accept the visitor. **/
	public Object jjtAccept(IntVParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}