package application.pen;
/* Generated By:JJTree: Do not edit this line. ASTBlock.java */

public class ASTBlock extends SimpleNode {
	public ASTBlock(int id) {
		super(id);
	}

	public ASTBlock(IntVParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. **/
	public Object jjtAccept(IntVParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
