package application.pen;
/* Generated By:JJTree: Do not edit this line. ASTSubstring.java */

public class ASTInsert extends SimpleNode {
	public ASTInsert(int id) {
		super(id);
	}

	public ASTInsert(IntVParser p, int id) {
		super(p, id);
	}


	/** Accept the visitor. **/
	public Object jjtAccept(IntVParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}