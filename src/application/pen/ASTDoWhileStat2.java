package application.pen;
/* Generated By:JJTree: Do not edit this line. ASTDoWhileStat2.java */

public class ASTDoWhileStat2 extends SimpleNode {
	public ASTDoWhileStat2(int id) {
		super(id);
	}

	public ASTDoWhileStat2(IntVParser p, int id) {
		super(p, id);
	}


	/** Accept the visitor. **/
	public Object jjtAccept(IntVParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}