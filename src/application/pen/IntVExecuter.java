package application.pen;
import java.awt.Color;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.text.BadLocationException;

public class IntVExecuter implements IntVParserVisitor{
	Hashtable symTable	= new Hashtable();
	Hashtable FunctionTable	= new Hashtable();
	Hashtable TableNoTable	= new Hashtable();
	IntVFileIO IO		= new IntVFileIO();

	Queue	call_var	= new Queue();
	String[] var_title	= {"型" , "変数名" , "値"};
	
	int declaration		= 0;
	boolean flag		= false;
	boolean NodeDump	= false;
	
	String array_var;

	public void NodeDump(){
		NodeDump = true;
	}
	
	public Object visit(SimpleNode node, Object data) {
		IO.FileAllClose();
		throw new Error(); // It better not come here.
	}

	public Object visit(ASTIntVUnit node, Object data) {
		
		if(NodeDump){
			//ノードダンプ出力
			System.out.println("-*-*-*- node dump -*-*-*-");
			node.dump("");
			System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-");
		}
		
		int i, k = node.jjtGetNumChildren();
		for (i = 0; i < k; i++) {
			if( node.jjtGetChild(i) instanceof ASTFunction){
				node.jjtGetChild(i).jjtAccept(this, data);
			}
		}
		
		for (i = 0; i < k; i++) {
			if( !(node.jjtGetChild(i) instanceof ASTFunction)){
				node.jjtGetChild(i).jjtAccept(this, data);
			}
		}
		IO.FileAllClose();
		return null;
	}

												// --> <10>
	public Object visit(ASTErrorOccur node, Object data) {
		IO.FileAllClose();
		throw new Error(); // It better not come here.
	}

	public Object visit(ASTFunction node, Object data) {
		Object[] obj = { node, data };
		symTable.put(node.varName, obj);
		FunctionTable.put(node.varName, obj);
		
		TableNoTable.put(node.varName,new Integer(MainGUI.vt_model.getRowCount()));
		if( node.decl == 0 ){
			String[] stat_data = {"手続き", node.varName ,""};
			MainGUI.vt_model.addRow(stat_data);
		} else if( node.decl == 1){
			String[] stat_data = {"整数", node.varName ,""};
			MainGUI.vt_model.addRow(stat_data);
		} else if( node.decl == 2 ) {
			String[] stat_data = {"実数", node.varName ,""};
			MainGUI.vt_model.addRow(stat_data);
		} else if ( node.decl == 3 ) {
			String[] stat_data = {"文字列", node.varName ,""};
			MainGUI.vt_model.addRow(stat_data);
		}
		
		return null;
	}

	public Object visit(ASTFunctionVar node, Object data) {
		declaration = node.decl;
		String var_name;
		Object obj = new Object();
		var_name = (String) node.jjtGetChild(0).jjtAccept(this, data);
		
		Object get = symTable.get(var_name);
		String var = call_var.pop().toString();
		if( get instanceof Integer){
			obj = new Integer(new Double(var).intValue());
		} else if( get instanceof Double){
			obj = new Double(var);
		} else if( get instanceof String){
			obj = new String(var);
		}
		symTable.put(var_name,obj);

		Integer TableNo = (Integer)TableNoTable.get(var_name);
		MainGUI.vt_model.setValueAt(obj,TableNo.intValue(),2);
		
		if( node.jjtGetNumChildren() > 1) {
			node.jjtGetChild(1).jjtAccept(this, data);
		}
		return null;
	}
	
	public Object visit(ASTVarDecl node, Object data) {
		declaration = node.decl;
		run_flag(node.line_num1, true);
		int i, k = node.jjtGetNumChildren();
		for (i = 0; i < k; i++){
			run_flag(node.line_num1, false);
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		return null;
	}
	
	public Object visit(ASTDecl node, Object data) {
		int k = node.jjtGetNumChildren();
		
		if(k > 0){
			array_var = node.varName + "[";
			node.jjtGetChild(0).jjtAccept(this, data);
		}else{
			if (!symTable.containsKey(node.varName)) {
				TableNoTable.put(node.varName,new Integer(MainGUI.vt_model.getRowCount()));
				if( declaration == 1 ){
					symTable.put(node.varName, new Integer(0));
					String[] stat_data = {"整数", node.varName ,"0"};
					MainGUI.vt_model.addRow(stat_data);
				} else if( declaration == 2 ){
					symTable.put(node.varName, new Double(0.0));
					String[] stat_data = {"実数", node.varName ,"0.0"};
					MainGUI.vt_model.addRow(stat_data);
				} else if( declaration == 3 ){
					symTable.put(node.varName, new String());
					String[] stat_data = {"文字列", node.varName ,""};
					MainGUI.vt_model.addRow(stat_data);
				}
			} else {
				new ConsoleAppend("### "  + MainGUI.run_point.getLineCount() + "行目の \""+ node.varName + "\" は既に変数として宣言されています\n");
				RunBreak();
			}
		}
		return node.varName;
	}
	
	public Object visit(ASTArray node, Object data) {
		String var,array;
		int k = node.jjtGetNumChildren();
		array = array_var;
		Integer i = (Integer)node.jjtGetChild(0).jjtAccept(this, data);
		if(k > 1){
			for( int j = 0; j <= i.intValue(); j++){
				array_var = array + String.valueOf(j) + ",";
				node.jjtGetChild(1).jjtAccept(this, data);
			}
		}else{
			for( int j = 0; j <= i.intValue(); j++){
				String x = "";
				String y = "";
				var = array + String.valueOf(j) + "]";
				if (!symTable.containsKey(var)) {
					if(declaration == 1){
						x = "整数";
						y = "0";
						symTable.put(var, new Integer(0));
					} else if(declaration == 2){
						x = "実数";
						y = "0.0";
						symTable.put(var, new Double(0));
					} else if(declaration == 3){
						x = "文字列";
						y = "";
						symTable.put(var, new String());
					}
					TableNoTable.put(var,new Integer(MainGUI.vt_model.getRowCount()));
					String[] stat_data = {x, var, y};
					MainGUI.vt_model.addRow(stat_data);
				} else {
					new ConsoleAppend("### "  + MainGUI.run_point.getLineCount() + "行目の \""+ var + "\" は既に変数として宣言されています\n");
					RunBreak();
				}
			}
		}
		return null;
	}
	
	
	public Object visit(ASTAssignStats node, Object data) {
		run_flag(node.line_num1, true);
		int i, k = node.jjtGetNumChildren();
		for (i = 0; i < k; i++){
			run_flag(node.line_num1, false);
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		return null;
	}
	
	
	public Object visit(ASTAssignStat node, Object data) {
		node.jjtGetChild(0).jjtAccept(this, data);
		
		Object i = symTable.get(((ASTIdent)(node.jjtGetChild(0))).varName);
		Object input = node.jjtGetChild(1).jjtAccept(this, data);
		
		Object obj = new Object();

		if( i instanceof Integer){
			obj = new Integer(new Double(input.toString()).intValue());
		} else if( i instanceof Double){
			obj = new Double(input.toString());
		} else if( i instanceof String){
			obj = new String(input.toString());
		}
		symTable.put(((ASTIdent)(node.jjtGetChild(0))).varName,obj);

		Integer TableNo = (Integer)TableNoTable.get(((ASTIdent)(node.jjtGetChild(0))).varName);
		MainGUI.vt_model.setValueAt(obj,TableNo.intValue(),2);
		return null;
	}
	
					// --> <4>
	public Object visit(ASTIfStat node, Object data) {
		Boolean b = (Boolean)node.jjtGetChild(0).jjtAccept(this, data);
		run_flag(node.line_num1, true);
		if(b.booleanValue()) {
			Object var = node.jjtGetChild(1).jjtAccept(this, data);
			if(var != null){ return var; }
			if(node.jjtGetNumChildren() == 3) {
				run_flag(node.line_num2, true);
			}
		}else if (node.jjtGetNumChildren() == 3) {
			run_flag(node.line_num2, true);
			Object var = node.jjtGetChild(2).jjtAccept(this, data);
			if(var != null){ return var; }
		}
		if (node.line_num3 != 0) {
			run_flag(node.line_num3, true);
		}

		return null;
	}

												// --> <5>
	public Object visit(ASTWhileStat node, Object data) {
		do {
			Boolean b = (Boolean)node.jjtGetChild(0).jjtAccept(this, data);
			run_flag(node.line_num1, true);
			if (b.booleanValue()) {
				Object var = node.jjtGetChild(1).jjtAccept(this, data);
				if(var != null){ return var; }
			}else{
				run_flag(node.line_num2, true);
				break;
			}
		} while (true);

		return null;
	}

												// --> <5>
	public Object visit(ASTDoWhileStat node, Object data) {
		do {
			run_flag(node.line_num1, true);
			Object var = node.jjtGetChild(0).jjtAccept(this, data);
			if(var != null){ return var; }
			Boolean b = (Boolean)node.jjtGetChild(1).jjtAccept(this, data);
			run_flag(node.line_num2, true);
			if (!b.booleanValue()) {
				run_flag(node.line_num2, true);
				break;
			}
		} while (true);

		return null;
	}

												// --> <5>
	public Object visit(ASTRepeatUntil node, Object data) {
		do {
			run_flag(node.line_num1, true);
			Object var = node.jjtGetChild(0).jjtAccept(this, data);
			if(var != null){ return var; }
			Boolean b = (Boolean)node.jjtGetChild(1).jjtAccept(this, data);
			run_flag(node.line_num2, true);
			if (b.booleanValue()) {
				run_flag(node.line_num2, true);
				break;
			}
		} while (true);
		return null;
	}


	public Object visit(ASTForStat node, Object data) {
		run_flag(node.line_num1, true);
		Integer i, j, k, x, y, TableNo;
		node.jjtGetChild(0).jjtAccept(this, data);
		i = (Integer)node.jjtGetChild(1).jjtAccept(this, data);
		symTable.put(((ASTIdent)(node.jjtGetChild(0))).varName, i);

		TableNo = (Integer)TableNoTable.get(((ASTIdent)(node.jjtGetChild(0))).varName);
		MainGUI.vt_model.setValueAt(String.valueOf(i),TableNo.intValue(),2);

		do {
			x = (Integer)symTable.get(((ASTIdent)(node.jjtGetChild(0))).varName);
			y = (Integer)node.jjtGetChild(2).jjtAccept(this, data);
			if((((ASTForStatAdd)(node.jjtGetChild(3))).op > 0 && x.intValue() <= y.intValue()) || (((ASTForStatAdd)(node.jjtGetChild(3))).op < 0 && x.intValue() >= y.intValue())){
				Object var = node.jjtGetChild(4).jjtAccept(this, data);
				if(var != null){ return var; }
				run_flag(node.line_num1, true);
				j = (Integer)node.jjtGetChild(3).jjtAccept(this, data);
				k = (Integer)symTable.get(((ASTIdent)(node.jjtGetChild(0))).varName);
				symTable.put(((ASTIdent)(node.jjtGetChild(0))).varName, new Integer( k.intValue() + j.intValue()));
				TableNo = (Integer)TableNoTable.get(((ASTIdent)(node.jjtGetChild(0))).varName);
				MainGUI.vt_model.setValueAt(String.valueOf(k.intValue() + j.intValue()),TableNo.intValue(),2);
			}else{
				run_flag(node.line_num2, true);
				break;
			}
		}while (true);
		return null;
	}


	public Object visit(ASTForStatAdd node, Object data) {
		if(node.jjtGetNumChildren() == 0){
			return new Integer( node.op );
		}else{
			Integer i = (Integer)node.jjtGetChild(0).jjtAccept(this, data);
			return new Integer( i.intValue() * node.op );
		}
	}
												// --> <2>
	public Object visit(ASTGetStat node, Object data) {
		node.jjtGetChild(0).jjtAccept(this, data);

		String input = input_wait();
		Double input_data;
		Object obj = new Object();
		if(symTable.get(((ASTIdent)(node.jjtGetChild(0))).varName) instanceof Integer){
			input_data = new Double(input);
			obj = new Integer(input_data.intValue());
		}else if(symTable.get(((ASTIdent)(node.jjtGetChild(0))).varName) instanceof Double){
			input_data = new Double(input);
			obj = new Double(input_data.doubleValue());
		}else if(symTable.get(((ASTIdent)(node.jjtGetChild(0))).varName) instanceof String){
			obj = new String(input);
		}
		Integer TableNo = (Integer)TableNoTable.get(((ASTIdent)(node.jjtGetChild(0))).varName);
		MainGUI.vt_model.setValueAt(obj,TableNo.intValue(),2);
		return null;
	}

												// --> <3>
	public Object visit(ASTPutStat node, Object data) {
		run_flag(node.line_num1, true);
		String str = new String();
		int i, k = node.jjtGetNumChildren();
		for (i = 0; i < k; i++) {
			str += node.jjtGetChild(i).jjtAccept(this, data).toString();
		}
		new ConsoleAppend(str + node.n);
		return null;
	}

	public Object visit(ASTBlock node, Object data) {
		int i, k = node.jjtGetNumChildren();

		for (i = 0; i < k; i++){
		//	if( node.jjtGetChild(i) instanceof ASTReturn){
		//		Object var = node.jjtGetChild(i).jjtAccept(this, data);
		//		System.out.println(node + " : " + var);
		//		return var;
		//	} else {
		//		node.jjtGetChild(i).jjtAccept(this, data);
		//	}
			Object var = node.jjtGetChild(i).jjtAccept(this, data);
			if(var != null){ return var; }
		}
		return null;
	}

	public Object visit(ASTLSNode node, Object data) {
		Double i = new Double(node.jjtGetChild(0).jjtAccept(this, data).toString());
		Double j = new Double(node.jjtGetChild(1).jjtAccept(this, data).toString());
		
		return new Boolean(i.doubleValue() < j.doubleValue());
	}

	public Object visit(ASTGTNode node, Object data) {
		Double i = new Double(node.jjtGetChild(0).jjtAccept(this, data).toString());
		Double j = new Double(node.jjtGetChild(1).jjtAccept(this, data).toString());

		return new Boolean(i.doubleValue() > j.doubleValue());
	}

	public Object visit(ASTLENode node, Object data) {
		Double i = new Double(node.jjtGetChild(0).jjtAccept(this, data).toString());
		Double j = new Double(node.jjtGetChild(1).jjtAccept(this, data).toString());

		return new Boolean(i.doubleValue() <= j.doubleValue());
	}

	public Object visit(ASTGENode node, Object data) {
		Double i = new Double(node.jjtGetChild(0).jjtAccept(this, data).toString());
		Double j = new Double(node.jjtGetChild(1).jjtAccept(this, data).toString());

		return new Boolean(i.doubleValue() >= j.doubleValue());
	}

	public Object visit(ASTEQNode node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		Object j = node.jjtGetChild(1).jjtAccept(this, data);
		
		if(i instanceof String || j instanceof String){
			return new Boolean(i.toString().equals(j.toString()));
		} else {
			return new Boolean(
					new Double(i.toString()).doubleValue()
					== new Double(j.toString()).doubleValue()
				);
		}
	}

	public Object visit(ASTNTNode node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		Object j = node.jjtGetChild(1).jjtAccept(this, data);
		
		if(i instanceof String || j instanceof String){
			return new Boolean(!i.toString().equals(j.toString()));
		} else {
			return new Boolean(
					new Double(i.toString()).doubleValue()
					!= new Double(j.toString()).doubleValue()
				);
		}
	}

	public Object visit(ASTANDNode node, Object data) {
		Boolean i = (Boolean)node.jjtGetChild(0).jjtAccept(this, data);
		Boolean j = (Boolean)node.jjtGetChild(1).jjtAccept(this, data);

		return new Boolean(i.booleanValue() && j.booleanValue());
	}

	public Object visit(ASTORNode node, Object data) {
		Boolean i = (Boolean)node.jjtGetChild(0).jjtAccept(this, data);
		Boolean j = (Boolean)node.jjtGetChild(1).jjtAccept(this, data);
				
		return new Boolean(i.booleanValue() || j.booleanValue());
	}

	public Object visit(ASTNOTNode node, Object data) {
		Boolean i = (Boolean)node.jjtGetChild(0).jjtAccept(this, data);
		
		if(i.booleanValue()){
			return new Boolean(false);
		}else{
			return new Boolean(true);		   
		}
	}
	
	public Object visit(ASTAddNode node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		Object j = node.jjtGetChild(1).jjtAccept(this, data);
		if(i instanceof Integer && j instanceof Integer){
			return new Integer(
				new Integer(i.toString()).intValue()
				+ new Integer(j.toString()).intValue()
			);
		} else if(i instanceof String || j instanceof String){
			return new String(i.toString() + j.toString());
		} else {
			return new Double(
				new Double(i.toString()).doubleValue()
				+ new Double(j.toString()).doubleValue()
			);
		}
	}

	public Object visit(ASTSubNode node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		Object j = node.jjtGetChild(1).jjtAccept(this, data);
		if(i instanceof Integer && j instanceof Integer){
			return new Integer(
				new Integer(i.toString()).intValue()
				- new Integer(j.toString()).intValue()
			);
		} else {
			return new Double(
				new Double(i.toString()).doubleValue()
				- new Double(j.toString()).doubleValue()
			);
		}
	}

												// --> <1>
	public Object visit(ASTMulNode node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		Object j = node.jjtGetChild(1).jjtAccept(this, data);
		if(i instanceof Integer && j instanceof Integer){
			return new Integer(
				new Integer(i.toString()).intValue()
				* new Integer(j.toString()).intValue()
			);
		} else {
			return new Double(
				new Double(i.toString()).doubleValue()
				* new Double(j.toString()).doubleValue()
			);
		}
	}

	public Object visit(ASTDivNode node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		Object j = node.jjtGetChild(1).jjtAccept(this, data);
		if(i instanceof Integer && j instanceof Integer){
			return new Integer(
				new Integer(i.toString()).intValue()
				/ new Integer(j.toString()).intValue()
			);
		} else {
			return new Double(
				new Double(i.toString()).doubleValue()
				/ new Double(j.toString()).doubleValue()
			);
		}
	}
	
	public Object visit(ASTSurNode node, Object data) {
		Integer i = (Integer)
				node.jjtGetChild(0).jjtAccept(this, data);
		Integer j = (Integer)
				node.jjtGetChild(1).jjtAccept(this, data);

		return new Integer(i.intValue() % j.intValue());
	}

	public Object visit(ASTMinNode node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		if(i instanceof Integer){
			return new Integer(
				- new Integer(i.toString()).intValue()
			);
		} else {
			return new Double(
				- new Double(i.toString()).doubleValue()
			);
		}
	}

						// --> <8>
	public Object visit(ASTIdent node, Object data) {
		if( node.flag ) {
			int k = node.jjtGetNumChildren();
			if(k > 0){
				Object i = node.jjtGetChild(0).jjtAccept(this, data);
				node.varName = node.varName2 + "[" + i + "]";
			}
	
			if( null == symTable.get(node.varName)){
				new ConsoleAppend("### "  + MainGUI.run_point.getLineCount() + "行目 : "+ node.varName + "\n");
			}
			return symTable.get(node.varName);
		} else {
			return Function(node, data, node.varName);
		}
	}

	public Object visit(ASTArrayNum node, Object data) {
		int k = node.jjtGetNumChildren();
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		if(k > 1){
			i = i + "," + node.jjtGetChild(1).jjtAccept(this, data);
		}
		return (i);
	}

	public Object visit(ASTFunctionCall node, Object data) {
		return Function(node, data, node.varName);
	}

	public Object visit(ASTReturn node, Object data) {
		run_flag(node.line_num1, true);
		return node.jjtGetChild(0).jjtAccept(this, data);
	}

	public Object visit(ASTGet node, Object data) {
		String input_data = input_wait();
		return input_data;
	}

	public Object visit(ASTRandom node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		Double j = new Double(i.toString());
		double random = Math.random() * (j.intValue() + 1);
		return new Integer((int)random);
	}
	
	public Object visit(ASTSine node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		Double j = new Double(i.toString());
		double sine = Math.sin(j.doubleValue());
		return new Double(sine);
	}

	public Object visit(ASTCosine node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		Double j = new Double(i.toString());
		double cos = Math.cos(j.doubleValue());
		return new Double(cos);
	}

	public Object visit(ASTTangent node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		Double j = new Double(i.toString());
		double tan = Math.tan(j.doubleValue());
		return new Double(tan);
	}

	public Object visit(ASTSqrt node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		Double j = new Double(i.toString());
		double sqrt = Math.sqrt(j.doubleValue());
		return new Double(sqrt);
	}
	
	public Object visit(ASTLog node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		Double j = new Double(i.toString());
		double log = Math.log(j.doubleValue());
		return new Double(log);
	}
	
	public Object visit(ASTAbs node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		if(i instanceof Integer){
			Double j = new Double(i.toString());
			int abs = Math.abs(j.intValue());
			return new Integer(abs);
		} else {
			Double j = new Double(i.toString());
			double abs = Math.abs(j.doubleValue());
			return new Double(abs);
		}
	}

	public Object visit(ASTCeil node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		Double j = new Double(i.toString());
		double ceil = Math.ceil(j.doubleValue());
		return new Double(ceil);
	}

	public Object visit(ASTFloor node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		Double j = new Double(i.toString());
		double floor = Math.floor(j.doubleValue());
		return new Double(floor);
	}

	public Object visit(ASTRound node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		Double j = new Double(i.toString());
		int round = (int) Math.round(j.doubleValue());
		return new Integer(round);
	}

	public Object visit(ASTInt node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		Double j = new Double(i.toString());
		return new Integer(j.intValue());
	}

	public Object visit(ASTLength node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		return new Integer(i.toString().length());
	}

	public Object visit(ASTSubstring node, Object data) {
		int k = node.jjtGetNumChildren();
		String substring;
		
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		int x = new Double(node.jjtGetChild(1).jjtAccept(this, data).toString()).intValue();
		if( k >= 3){
			int y = new Double(node.jjtGetChild(2).jjtAccept(this, data).toString()).intValue();
			substring = i.toString().substring(x,y);
		} else {
			substring = i.toString().substring(x);
		}
		return new String(substring);
	}

	public Object visit(ASTInsert node, Object data) {
		String str1	= (String) node.jjtGetChild(0).jjtAccept(this, data);
		Integer n	= (Integer) node.jjtGetChild(1).jjtAccept(this, data);
		String str2	= (String) node.jjtGetChild(2).jjtAccept(this, data);
		
		String in = str1.substring(0,n.intValue()) + str2 + str1.substring(n.intValue());
		return new String(in);
	}
	
	public Object visit(ASTReplace node, Object data) {
		String str1	= (String) node.jjtGetChild(0).jjtAccept(this, data);
		Integer n	= (Integer) node.jjtGetChild(1).jjtAccept(this, data);
		Integer m	= (Integer) node.jjtGetChild(2).jjtAccept(this, data);
		String str2	= (String) node.jjtGetChild(3).jjtAccept(this, data);
		
		if( n.intValue() <= m.intValue() ){
			String in = str1.substring(0,n.intValue()) + str2 + str1.substring(m.intValue());
			return new String(in);
		} else {
			String in = str1.substring(0,m.intValue()) + str2 + str1.substring(n.intValue());
			return new String(in);
		}
	}

	public Object visit(ASTExtract node, Object data) {
		String[] ex;
		String str1	= (String) node.jjtGetChild(0).jjtAccept(this, data);
		String str2	= (String) node.jjtGetChild(1).jjtAccept(this, data);
		Integer n	= (Integer) node.jjtGetChild(2).jjtAccept(this, data);
		
		ex = str1.split(str2);

		if( ex.length <= n.intValue() ){
			int c = -2;
			return new String(new Character((char) c).toString());
		} else {
			return new String(ex[n.intValue()]);
		}
	}

	public Object visit(ASTgWindowOpen node, Object data) {
		int w = new Integer(node.jjtGetChild(0).jjtAccept(this, data).toString()).intValue();
		int h = new Integer(node.jjtGetChild(1).jjtAccept(this, data).toString()).intValue();

		run_flag(node.line_num1, true);
		MainGUI.gDrowWindow.gWindowOpen(w, h);
		return null;
	}

	public Object visit(ASTgWindowClose node, Object data) {
		run_flag(node.line_num1, true);
		MainGUI.gDrowWindow.gWindowClose();
		return null;
	}

	public Object visit(ASTgColor node, Object data) {
		int r = new Integer(node.jjtGetChild(0).jjtAccept(this, data).toString()).intValue();
		int g = new Integer(node.jjtGetChild(1).jjtAccept(this, data).toString()).intValue();
		int b = new Integer(node.jjtGetChild(2).jjtAccept(this, data).toString()).intValue();

		run_flag(node.line_num1, true);
		MainGUI.gDrowWindow.gColor(r, g, b);
		return null;
	}

	public Object visit(ASTgDrawOval node, Object data) {
		int x = new Integer(node.jjtGetChild(0).jjtAccept(this, data).toString()).intValue();
		int y = new Integer(node.jjtGetChild(1).jjtAccept(this, data).toString()).intValue();
		int w = new Integer(node.jjtGetChild(2).jjtAccept(this, data).toString()).intValue();
		int h = new Integer(node.jjtGetChild(3).jjtAccept(this, data).toString()).intValue();

		run_flag(node.line_num1, true);
		MainGUI.gDrowWindow.gDrawOval(x, y, w, h);
		return null;
	}

	public Object visit(ASTgDrawPoint  node, Object data) {
		int x = new Integer(node.jjtGetChild(0).jjtAccept(this, data).toString()).intValue();
		int y = new Integer(node.jjtGetChild(1).jjtAccept(this, data).toString()).intValue();

		run_flag(node.line_num1, true);
		MainGUI.gDrowWindow.gDrawPoint(x, y);
		return null;
	}

	public Object visit(ASTgFillOval node, Object data) {
		int x = new Integer(node.jjtGetChild(0).jjtAccept(this, data).toString()).intValue();
		int y = new Integer(node.jjtGetChild(1).jjtAccept(this, data).toString()).intValue();
		int w = new Integer(node.jjtGetChild(2).jjtAccept(this, data).toString()).intValue();
		int h = new Integer(node.jjtGetChild(3).jjtAccept(this, data).toString()).intValue();

		run_flag(node.line_num1, true);
		MainGUI.gDrowWindow.gFillOval(x, y, w, h);
		return null;
	}

	public Object visit(ASTgFillPoint  node, Object data) {
		int x = new Integer(node.jjtGetChild(0).jjtAccept(this, data).toString()).intValue();
		int y = new Integer(node.jjtGetChild(1).jjtAccept(this, data).toString()).intValue();

		run_flag(node.line_num1, true);
		MainGUI.gDrowWindow.gFillPoint(x, y);
		return null;
	}

	public Object visit(ASTgDrawLine node, Object data) {
		int x1 = new Integer(node.jjtGetChild(0).jjtAccept(this, data).toString()).intValue();
		int y1 = new Integer(node.jjtGetChild(1).jjtAccept(this, data).toString()).intValue();
		int x2 = new Integer(node.jjtGetChild(2).jjtAccept(this, data).toString()).intValue();
		int y2 = new Integer(node.jjtGetChild(3).jjtAccept(this, data).toString()).intValue();

		run_flag(node.line_num1, true);
		MainGUI.gDrowWindow.gDrawLine(x1, y1 ,x2 ,y2);
		return null;
	}

	public Object visit(ASTgDrawBox node, Object data) {
		int x = new Integer(node.jjtGetChild(0).jjtAccept(this, data).toString()).intValue();
		int y = new Integer(node.jjtGetChild(1).jjtAccept(this, data).toString()).intValue();
		int w = new Integer(node.jjtGetChild(2).jjtAccept(this, data).toString()).intValue();
		int h = new Integer(node.jjtGetChild(3).jjtAccept(this, data).toString()).intValue();

		run_flag(node.line_num1, true);
		MainGUI.gDrowWindow.gDrawBox(x, y, w, h);
		return null;
	}

	public Object visit(ASTgFillBox node, Object data) {
		int x = new Integer(node.jjtGetChild(0).jjtAccept(this, data).toString()).intValue();
		int y = new Integer(node.jjtGetChild(1).jjtAccept(this, data).toString()).intValue();
		int w = new Integer(node.jjtGetChild(2).jjtAccept(this, data).toString()).intValue();
		int h = new Integer(node.jjtGetChild(3).jjtAccept(this, data).toString()).intValue();

		run_flag(node.line_num1, true);
		MainGUI.gDrowWindow.gFillBox(x, y, w, h);
		return null;
	}

	public Object visit(ASTgDrawArc node, Object data) {
		int x = new Integer(node.jjtGetChild(0).jjtAccept(this, data).toString()).intValue();
		int y = new Integer(node.jjtGetChild(1).jjtAccept(this, data).toString()).intValue();
		int w = new Integer(node.jjtGetChild(2).jjtAccept(this, data).toString()).intValue();
		int h = new Integer(node.jjtGetChild(3).jjtAccept(this, data).toString()).intValue();
		int start  = new Integer(node.jjtGetChild(4).jjtAccept(this, data).toString()).intValue();
		int extent = new Integer(node.jjtGetChild(5).jjtAccept(this, data).toString()).intValue();
		int type   = new Integer(node.jjtGetChild(6).jjtAccept(this, data).toString()).intValue();
		
		run_flag(node.line_num1, true);
		MainGUI.gDrowWindow.gDrawArc(x, y, w, h, start, extent, type);
		return null;
	}

	public Object visit(ASTgFillArc node, Object data) {
		int x = new Integer(node.jjtGetChild(0).jjtAccept(this, data).toString()).intValue();
		int y = new Integer(node.jjtGetChild(1).jjtAccept(this, data).toString()).intValue();
		int w = new Integer(node.jjtGetChild(2).jjtAccept(this, data).toString()).intValue();
		int h = new Integer(node.jjtGetChild(3).jjtAccept(this, data).toString()).intValue();
		int start  = new Integer(node.jjtGetChild(4).jjtAccept(this, data).toString()).intValue();
		int extent = new Integer(node.jjtGetChild(5).jjtAccept(this, data).toString()).intValue();
		int type   = new Integer(node.jjtGetChild(6).jjtAccept(this, data).toString()).intValue();
		
		run_flag(node.line_num1, true);
		MainGUI.gDrowWindow.gFillArc(x, y, w, h, start, extent, type);
		return null;
	}

	public Object visit(ASTFile_openr node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		Integer ID = IO.FileOpenRead(i.toString());
		
		return ID;
	}

	public Object visit(ASTFile_openw node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		Integer ID = IO.FileOpenWrite(i.toString());
		
		return ID;
	}

	public Object visit(ASTFile_opena node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		Integer ID = IO.FileOpenAppend(i.toString());
		
		return ID;
	}

	public Object visit(ASTFile_close node, Object data) {
		Integer ID = (Integer) node.jjtGetChild(0).jjtAccept(this, data);
		run_flag(node.line_num1, true);
		IO.FileClose(ID);
		
		return null;
	}

	public Object visit(ASTFile_getstr node, Object data) {
		Integer ID = (Integer) node.jjtGetChild(0).jjtAccept(this, data);
		Integer n = (Integer) node.jjtGetChild(1).jjtAccept(this, data);
		String str = IO.FileGetStr(ID, n);

		return str;
	}

	public Object visit(ASTFile_getline node, Object data) {
		Integer ID = (Integer) node.jjtGetChild(0).jjtAccept(this, data);
		String str = IO.FileGetLine(ID);

		return str;
	}

	public Object visit(ASTFile_putstr node, Object data) {
		Integer ID = (Integer) node.jjtGetChild(0).jjtAccept(this, data);
		Object str = node.jjtGetChild(1).jjtAccept(this, data);
		run_flag(node.line_num1, true);
		IO.FilePutString(ID, str.toString());
		
		return null;
	}

	public Object visit(ASTFile_putline node, Object data) {
		Integer ID = (Integer) node.jjtGetChild(0).jjtAccept(this, data);
		Object str = node.jjtGetChild(1).jjtAccept(this, data);
		run_flag(node.line_num1, true);
		IO.FilePutLine(ID, str.toString());
		
		return null;
	}
	
	public Object visit(ASTFile_flush node, Object data) {
		Integer ID = (Integer) node.jjtGetChild(0).jjtAccept(this, data);
		run_flag(node.line_num1, true);
		IO.FileFlush(ID);
		
		return null;
	}

	public Object visit(ASTFile_isfile node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		String flag = IO.FileIsFile(i.toString());
		
		return flag;
	}

	public Object visit(ASTFile_rename node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		Object j = node.jjtGetChild(1).jjtAccept(this, data);
		run_flag(node.line_num1, true);
		IO.FileReName(i.toString(), j.toString());
		
		return null;
	}

	public Object visit(ASTFile_remove node, Object data) {
		Object i = node.jjtGetChild(0).jjtAccept(this, data);
		run_flag(node.line_num1, true);
		IO.FileRemove(i.toString());
		
		return null;
	}
	
	public Object visit(ASTLiteral node, Object data) {
		return new Integer(node.litValue);
	}

	public Object visit(ASTFloatLiteral node, Object data) {
		return new Double(node.litValue);
	}

	public Object visit(ASTStrlit node, Object data) {
		return node.litString;
	}

	public Object visit(ASTEOF_STR node, Object data) {
		return new String(new Character((char) node.char_code).toString());
	}
	
	public Object Function(SimpleNode node, Object data, String varName){
		Object[] obj = (Object[]) FunctionTable.get(varName);
		ASTFunction fc	= (ASTFunction) obj[0];
		Object fc_data	= obj[1];
		int x, y, k, i;

		run_flag(node.line_num1, true);
		node.line_num1 = MainGUI.run_point.getLineCount();
		
		k = node.jjtGetNumChildren();
		for(i = 0; i < k; i++){
			call_var.push(node.jjtGetChild(i).jjtAccept(this, data));
		}
		
		Object[] stack = { symTable.clone(), TableNoTable.clone()};
		
		symTable.clear();
		TableNoTable.clear();

//		x = MainGUI.vt_model.getRowCount();
//		Object[][] table = new Object[x][3];
//		for(i = 0; i < x; i++){
//			for( int j = 0; j < 3; j++){
//				table[i][j] = MainGUI.vt_model.getValueAt(i, j);
//				mysleep(3);
//			}
//		}
		
		Object[] temp_table = MainGUI.vt_model.getDataVector().toArray();
		mysleep(15);
		
	//	x = MainGUI.vt_model.getRowCount();
	//	for(i = 0; i < x; i++){
	//		MainGUI.vt_model.removeRow(0);
	//	}
		MainGUI.vt_model.setRowCount(0);
		
		run_flag(fc.line_num1, true);
		
		Object return_var =null;
		k = fc.jjtGetNumChildren();
		if( k > 1) {
			fc.jjtGetChild(0).jjtAccept(this, fc_data);
			call_var.clear();
			return_var = fc.jjtGetChild(1).jjtAccept(this, fc_data);
		} else {
			return_var = fc.jjtGetChild(0).jjtAccept(this, fc_data);
		}
		
		if(return_var == null) {
			run_flag(fc.line_num2, true);
		}

		symTable		= (Hashtable) stack[0];
		TableNoTable		= (Hashtable) stack[1];
		
	//	x = MainGUI.vt_model.getRowCount();
	//	for(i = 0; i < x; i++){
	//		MainGUI.vt_model.removeRow(0);
	//	}

		MainGUI.vt_model.setRowCount(0);

//		MainGUI.vt_model.setDataVector(table,var_title);

		for(i = 0; i < temp_table.length; i++){
			MainGUI.vt_model.addRow((Vector)temp_table[i]);
		}

		run_flag(node.line_num1, true);
		
		if(fc.decl == 1){
			return (Integer) return_var;
		} else if(fc.decl == 2){
			return (Double) return_var;
		} else if(fc.decl == 3){
			return (String) return_var;
		} else {
			return null;
		}
	}

	public synchronized void run_flag(int num, boolean mysleep_flag) {
		if(!MainGUI.Run_flag){ RunBreak(); }
		if( num > 0 ){ run_num(num); }
		if(mysleep_flag){
			long msec = MainGUI.run_time.getValue();
			if(!MainGUI.Step_flag && msec > 0) { mysleep(msec); }
			if(MainGUI.Step_flag && flag) { MainGUI.RunButton.MySuspend(); }
			while(MainGUI.Suspend_flag){ mysleep(100); }
			flag = true;
		}
		if(!MainGUI.Run_flag){ RunBreak(); }
	}

	public synchronized void mysleep(long sleep_msec) {
		try{
			wait(sleep_msec);
		}catch(InterruptedException e){ }
	}

	public void run_num(int run) {
		int num = MainGUI.run_point.getLineCount();
		
		if(run > num){
			for(int i = num; i < run; i++){
				MainGUI.run_point.insert("\n", 0);
			}
		}else if(run < num){
			for(int i = run; i < num; i++){
				MainGUI.run_point.replaceRange("",0,1);
			}
		}
	}

	public String input_wait(){
		MainGUI.run_print.input();
		MainGUI.console_tab.setSelectedIndex(0);
		MainGUI.console.setCaretPosition(MainGUI.console.getText().length());
		MainGUI.console.requestFocus();
		
		MainGUI.Input_flag = true;

		try {
			int get_line	= MainGUI.console.getLineCount();
			ConsoleKeyListener.start_offs = MainGUI.console.getLineEndOffset(get_line - 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		MainGUI.console.setEditable(true);
		MainGUI.console.setBackground(new Color(255,255,255));
		while(MainGUI.Input_flag){
			mysleep(250);
		}
		MainGUI.console.setEditable(false);
		MainGUI.console.setBackground(new Color(240, 240, 240));
		if(!MainGUI.Run_flag){ RunBreak(); }

		MainGUI.console_log.append(ConsoleKeyListener.input_line + "\n");
		
		if(MainGUI.Step_flag || MainGUI.Suspend_flag){
			MainGUI.run_print.stop();
		} else {
			MainGUI.run_print.run();
		}
		
		return (ConsoleKeyListener.input_line);
	}
	
	public void RunBreak(){
		MainGUI.Run_flag = false;
		IO.FileAllClose();

		symTable	= null;
		FunctionTable	= null;
		TableNoTable	= null;
		IO		= null;
		
		throw new ThreadRunStop();
	}
}

class Queue {
	Vector store = new Vector();

	public void push(Object obj) {
		store.addElement(obj);
	}

	public Object pop() {
		try {
			Object obj = store.elementAt(0);
			store.removeElementAt(0);
			return(obj);
		} catch(Exception exception) {
			return(null);
		}
	}
	
	public void clear() {
		store.clear();
	}
}