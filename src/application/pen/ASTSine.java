package application.pen;
/* Generated By:JJTree: Do not edit this line. ASTSine.java */

public class ASTSine extends SimpleNode {
	public ASTSine(int id) {
		super(id);
	}

	public ASTSine(IntVParser p, int id) {
		super(p, id);
	}


	/** Accept the visitor. **/
	public Object jjtAccept(IntVParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}