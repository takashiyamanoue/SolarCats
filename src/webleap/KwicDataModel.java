package webleap;


public class KwicDataModel extends javax.swing.table.AbstractTableModel
{
    final String[] columnNames = {"number","URL","Title","KWIC(Snippet)"};//same as before...
    final Object[][] data = { {"","","",""}, //1
                              {"","","",""},
                              {"","","",""},
                              {"","","",""},
                              {"","","",""},
                              {"","","",""},
                              {"","","",""},
                              {"","","",""},
                              {"","","",""},
                              {"","","",""}, //10
                              {"","","",""},
                              {"","","",""},
                              {"","","",""},
                              {"","","",""},
                              {"","","",""},
                              {"","","",""},
                              {"","","",""},
                              {"","","",""},
                              {"","","",""},
                              {"","","",""}, //20
                              {"","","",""},
                              {"","","",""},
                              {"","","",""},
                              {"","","",""},
                              {"","","",""},
                              {"","","",""},
                              {"","","",""}
                              
  		                   }; //same as before...

    public int getColumnCount() {
        return columnNames.length;
    }
    
    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        Object rtn=data[row][col];
        return rtn;
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col < 2) { 
            return false;
        } else {
            return true;
        }
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        /*
        ...//debugging code not shown...
        ...//ugly special handling of Integers not shown...
        */
        data[row][col] = value;
        fireTableCellUpdated(row, col);
        /*
        ...//debugging code not shown...
        */
    }

}
