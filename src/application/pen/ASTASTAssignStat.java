package application.pen;
/* Generated By:JJTree: Do not edit this line. ASTASTAssignStat.java */

public class ASTASTAssignStat extends SimpleNode {
	public ASTASTAssignStat(int id) {
		super(id);
	}

	public ASTASTAssignStat(IntVParser p, int id) {
		super(p, id);
	}


	/** Accept the visitor. **/
	public Object jjtAccept(IntVParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}