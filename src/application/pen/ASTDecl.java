package application.pen;
/* Generated By:JJTree: Do not edit this line. ASTVarDecl.java */

public class ASTDecl extends SimpleNode {
	public ASTDecl(int id) {
		super(id);
	}

	public ASTDecl(IntVParser p, int id) {
		super(p, id);
	}


	/** Accept the visitor. **/
	public Object jjtAccept(IntVParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	/** customizing codes **/
	public String varName;

}
