package application.pen;
/* Generated By:JJTree: Do not edit this line. ASTNTNode.java */

public class ASTNTNode extends SimpleNode {
	public ASTNTNode(int id) {
		super(id);
	}

	public ASTNTNode(IntVParser p, int id) {
		super(p, id);
	}


	/** Accept the visitor. **/
	public Object jjtAccept(IntVParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
