package application.pen;
/* Generated By:JJTree: Do not edit this line. ASTInt.java */

public class ASTInt extends SimpleNode {
	public ASTInt(int id) {
		super(id);
	}

	public ASTInt(IntVParser p, int id) {
		super(p, id);
	}


	/** Accept the visitor. **/
	public Object jjtAccept(IntVParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
