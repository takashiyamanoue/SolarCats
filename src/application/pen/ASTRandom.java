package application.pen;
/* Generated By:JJTree: Do not edit this line. ASTRandom.java */

public class ASTRandom extends SimpleNode {
	public ASTRandom(int id) {
		super(id);
	}

	public ASTRandom(IntVParser p, int id) {
		super(p, id);
	}


	/** Accept the visitor. **/
	public Object jjtAccept(IntVParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
