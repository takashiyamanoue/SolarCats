package application.pen;
/* Generated By:JJTree: Do not edit this line. ASTLENode.java */

public class ASTLENode extends SimpleNode {
	public ASTLENode(int id) {
		super(id);
	}

	public ASTLENode(IntVParser p, int id) {
		super(p, id);
	}


	/** Accept the visitor. **/
	public Object jjtAccept(IntVParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
