package application.pen;
/* Generated By:JJTree: Do not edit this line. ASTGENode.java */

public class ASTGENode extends SimpleNode {
	public ASTGENode(int id) {
		super(id);
	}

	public ASTGENode(IntVParser p, int id) {
		super(p, id);
	}


	/** Accept the visitor. **/
	public Object jjtAccept(IntVParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
