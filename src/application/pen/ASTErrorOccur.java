package application.pen;
/* Generated By:JJTree: Do not edit this line. ASTErrorOccur.java */

public class ASTErrorOccur extends SimpleNode {
	public ASTErrorOccur(int id) {
		super(id);
	}

	public ASTErrorOccur(IntVParser p, int id) {
		super(p, id);
	}


	/** Accept the visitor. **/
	public Object jjtAccept(IntVParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
