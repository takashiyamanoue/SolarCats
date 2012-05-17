/*
		A basic implementation of the JFrame class.
*/
package application.webleap;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import nodesystem.CommunicationNode;
import nodesystem.DialogListener;
import nodesystem.EditDialog;
import webleap.Aggregate;
import webleap.KwicTable;
import application.texteditor.TextEditFrame;
import application.webbrowser.WebFrame;
import controlledparts.ControlledButton;
import controlledparts.ControlledFrame;
import controlledparts.FrameWithControlledButton;
import controlledparts.SelectedButton;

public class UrlTable extends ControlledFrame implements DialogListener, FrameWithControlledButton,
                     KwicTable
{
    public WebFrame browser;

    public webleap.SearchEngineDriver searched;

    public void setIterator(webleap.Iterator x)
    {
        this.iterator=x;
    }

    public void setAggregate(Aggregate x)
    {
        this.aggregate=x;
    }

    public webleap.Iterator iterator;

    public webleap.Aggregate aggregate;

    public webleap.DataExtractor dataExtractor;

    public void setDataExtractor(webleap.DataExtractor de)
    {
    }

    public void showTable(String keyword)
    {
            searched=(webleap.SearchEngineDriver)(this.searchedPhrases.get(keyword));
            
//            UrlTable urlTab=getUrlTable();
            this.initTab();
//            urlTab.setSettings(this.settings);
            this.keyWordField.setText(keyword);
            this.setHtml("");
            this.setFrequency(searched.getFrequency());
            this.setTitle("WebLEAP KWIC table");
            this.show();
            this.setTable();
    }

    public Hashtable searchedPhrases;

    public void setCache(Hashtable cache)
    {
        this.searchedPhrases=cache;
    }

    public boolean isShowingRmouse()
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        return super.isShowingRmouse();
    }

    public boolean isControlledByLocalUser()
    {
        // This method is derived from interface FrameWithControlledButton
        // to do: code goes here
        return super.isControlledByLocalUser();
    }

    public void sendEvent(String x)
    {
        // This method is derived from interface FrameWithControlledButton
        // to do: code goes here
    }

    public void setTable()
    {
        webleap.Iterator it=((webleap.Aggregate)(this.searched)).iterator();
        /*
            HtmlTokenizer htk=new HtmlTokenizer(html);
            PatternMatcher pm=new PatternMatcher();
            pm.init(htk.tokens);
            Vector urlList=new Vector();
            int searchEngineIndex=settings.searchEngineComboBox.getSelectedIndex();
            String listPattern=(String)(settings.searchEngineSettingTab.getValueAt(searchEngineIndex,7));
//            pm.pattern("<dl> <dt> <b> * </b> * <b> * </b> </a> <dd>", urlList);
//            pm.pattern("<dl> <dt> * * </b> <b>  * * </a> </b>",urlList);
            String infoIndexes=(String)(settings.searchEngineSettingTab.getValueAt(searchEngineIndex,8));
            String urlPattern=(String)(settings.searchEngineSettingTab.getValueAt(searchEngineIndex,9));
            StringTokenizer stx=new StringTokenizer(infoIndexes,",");
            int numberOfValue=(new Integer(stx.nextToken())).intValue();
            int listNumberIndex=(new Integer(stx.nextToken())).intValue();
            int urlIndex=(new Integer(stx.nextToken())).intValue();
            int titleIndex=(new Integer(stx.nextToken())).intValue();

            pm.pattern(listPattern,urlList);
            
            int i=0;
            int n=0;
            int listSize=urlList.size();
            setTokens(htk.tokens);
            */
            for(int i=0;i<25;i++){
                if(it.hasNext()){
                  webleap.SearchResultElement e=(webleap.SearchResultElement)it.next();
                  tModel.setValueAt(""+i,i,0);
                  tModel.setValueAt((String)(e.getUrl()),i,1);
                  tModel.setValueAt("<html>"+(String)(e.getTitle()),i,2);
                  String qwic="<html>";
//                  tModel.setValueAt((String)(e.getSnippet()),i,3);
                  qwic=qwic+e.getSnippet();
//                  qwic=qwic+"<hr>";
                  tModel.setValueAt(qwic,i,3);
                }
                else break;
            }
//            qwic="</body></html>";
//            this.browser.pageArea.setText(qwic);
        
           /*
            while(i<listSize){
                String urlNo=(String)(urlList.elementAt(i+listNumberIndex));
                String itsUrl=(String)(urlList.elementAt(i+urlIndex));  // i+1
                String itsTitle=(String)(urlList.elementAt(i+titleIndex)); // i+2
//                ra.append(urlNo+":"+itsUrl+"-"+itsTitle+ "\n");
                PatternMatcher pm2=new PatternMatcher();
                Vector urlField=new Vector();
                pm2.init(itsUrl," ");
                pm2.pattern(urlPattern,urlField);
                tModel.setValueAt(urlNo,n,0);
                tModel.setValueAt((String)(urlField.elementAt(0)),n,1);
                tModel.setValueAt(itsTitle,n,2);
                i+=numberOfValue; // i+=3
                n++;
            }
            */
            /*
            gui.repaint();
            gui.eraseTheHtml();
//            gui.removeUrlExtractor();
    		try {
	    		// UrlTable Create and show the UrlTable with a title
  //			(new UrlTable("url list")).setVisible(true);
                urlTab.setVisible(true);
                urlTab.show();
		    } catch (Exception e) {
		    }
		    */
    }

    public File getDefaultPath()
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        return null;
    }

    public Vector getDialogs()
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
        return null;
    }

    public void sendFileDialogMessage(String m)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
    }

    public void whenActionButtonPressed(EditDialog d)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
    }

    public void whenCancelButtonPressed(EditDialog d)
    {
        // This method is derived from interface DialogListener
        // to do: code goes here
    }

    public TextEditFrame textEditFrame;

    public void openTokenizedPage()
    {
        textEditFrame.setListener(this);
        textEditFrame.setVisible(true);
        textEditFrame.setControlledFrame(this);
        textEditFrame.getTextArea().setText("");
        if(tokens==null) return;
        int lineNumber=this.tokens.size();
        for(int i=0;i<lineNumber;i++){
            textEditFrame.getTextArea().append(""+i+":"+(String)(tokens.elementAt(i))+"\n");
        }
    }

    public String html;

    public void showPage(String h)
    {
        /*
         WebFrame browser=(WebFrame)
            (this.communicationNode.applicationManager.spawnApplication2("WebFrame",
          "local"));
          */
         browser.pageArea.setText(h);
    }

    Vector tokens;

    void setTokens(Vector t)
    {
        this.tokens=t;
    }

    public void setFrequency(int f)
    {
        this.frequencyField.setText(""+f);
    }

    public void setHtml(String h)
    {
        html=h;
    }

    public Vector buttons;

    public void clickButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        ControlledButton b=(ControlledButton)(buttons.elementAt(i));
        b.click();
        this.mouseClickedAtButton(i);
    }

    public void focusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
    }

    public void mouseClickedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
        /*
		buttons.addElement(this.showThePageButton);
		buttons.addElement(this.previousButton);
		buttons.addElement(this.nextButton);
		buttons.addElement(this.tokenizedPageButton);
		*/
 		if(i==0){
		    this.showPage(this.html);
		}
		else
		if(i==1){
		}
		else
		if(i==2){
		}
		else
		if(i==3){
		    // tokenizedPageButton
		    this.openTokenizedPage();
		}
		if(i==this.exitButton.getID()){
		    this.hide();
		}
		
    }

    public void mouseEnteredAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
    }

    public void mouseExitedAtButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
    }

    public void unfocusButton(int i)
    {
        // This method is derived from interface SelectButtonsFrame
        // to do: code goes here
            SelectedButton button=(SelectedButton)(buttons.elementAt(i));
            button.controlledButton_mouseExited(null);
    }

    public void setSettings(webleap.Settings s)
    {
        settings=s;
    }

    public webleap.Settings settings;

    public void initTab()
    {
        /*
		Object tdata[][]={ {"","","",""}, {"","","",""}, {"","","",""}, {"","","",""}, {"","","",""}, 
   		                   {"","","",""}, {"","","",""}, {"","","",""}, {"","","",""}, {"","","",""},
   		                   {"","","",""}, {"","","",""}, {"","","",""}, {"","","",""}, {"","","",""}, 
   		                   {"","","",""}, {"","","",""}, {"","","",""}, {"","","",""}, {"","","",""}
   		                   };
		Object tlabel[]={"number","URL","Title","Snippet"};
		*/
        tModel = new webleap.KwicDataModel();
		theTable.setModel(tModel);
		theTable.setRowHeight(40);
        TableColumn column = null;
        column=theTable.getColumnModel().getColumn(0);
        column.setMinWidth(15); //sport column is smaller
        column.setMaxWidth(30);
        column=theTable.getColumnModel().getColumn(1);
        column.setMinWidth(80); //sport column is middle
        column.setMaxWidth(250);
        column=theTable.getColumnModel().getColumn(2);
        column.setMinWidth(80);
        column.setMaxWidth(250);
        
    }


    public webleap.KwicDataModel tModel;

	public UrlTable()
	{
		// This code is automatically generated by Visual Cafe when you add
		// components to the visual environment. It instantiates and initializes
		// the components. To modify the code, only use code syntax that matches
		// what Visual Cafe can generate, or Visual Cafe may be unable to back
		// parse your Java file into its visual environment.
		//{{INIT_CONTROLS
		getContentPane().setLayout(null);
		setSize(825,447);
		setVisible(false);
		getContentPane().add(keyWordField);
		keyWordField.setBounds(36,24,312,24);
		TableScrollPane.setAutoscrolls(true);
		TableScrollPane.setOpaque(true);
		getContentPane().add(TableScrollPane);
		TableScrollPane.setBounds(36,72,780,324);
		theTable.setCellSelectionEnabled(true);
		theTable.setColumnSelectionAllowed(true);
		theTable.setEditingRow(10);
		theTable.setEditingColumn(10);
		TableScrollPane.getViewport().add(theTable);
		theTable.setBackground(java.awt.Color.lightGray);
		theTable.setBounds(0,0,776,0);
		showThePageButton.setText("page");
		showThePageButton.setActionCommand("page");
		showThePageButton.setToolTipText("show the page");
		getContentPane().add(showThePageButton);
		showThePageButton.setBackground(new java.awt.Color(204,204,204));
		showThePageButton.setForeground(java.awt.Color.black);
		showThePageButton.setFont(new Font("Dialog", Font.BOLD, 12));
		showThePageButton.setBounds(36,48,84,24);
		tokenizedPageButton.setText("tokens");
		tokenizedPageButton.setActionCommand("tokens");
		tokenizedPageButton.setToolTipText("show the tokenized page");
		getContentPane().add(tokenizedPageButton);
		tokenizedPageButton.setBackground(new java.awt.Color(204,204,204));
		tokenizedPageButton.setForeground(java.awt.Color.black);
		tokenizedPageButton.setFont(new Font("Dialog", Font.BOLD, 12));
		tokenizedPageButton.setBounds(312,48,84,24);
		nextButton.setText("next");
		nextButton.setActionCommand("next");
		getContentPane().add(nextButton);
		nextButton.setBackground(new java.awt.Color(204,204,204));
		nextButton.setForeground(java.awt.Color.black);
		nextButton.setFont(new Font("Dialog", Font.BOLD, 12));
		nextButton.setBounds(216,48,96,24);
		previousButton.setText("previous");
		previousButton.setActionCommand("previous");
		getContentPane().add(previousButton);
		previousButton.setBackground(new java.awt.Color(204,204,204));
		previousButton.setForeground(java.awt.Color.black);
		previousButton.setFont(new Font("Dialog", Font.BOLD, 12));
		previousButton.setBounds(120,48,96,24);
		frequencyLabel.setText("frequency");
		getContentPane().add(frequencyLabel);
		frequencyLabel.setBounds(372,24,60,24);
		getContentPane().add(frequencyField);
		frequencyField.setBounds(456,24,96,24);
		exitButton.setText("exit");
		exitButton.setActionCommand("exit");
		getContentPane().add(exitButton);
		exitButton.setBackground(new java.awt.Color(204,204,204));
		exitButton.setForeground(java.awt.Color.black);
		exitButton.setFont(new Font("Dialog", Font.BOLD, 12));
		exitButton.setBounds(396,48,96,24);
		JLabel1.setText("The corresponding page is shown by clicking the corresponding low\'s URL column.");
		getContentPane().add(JLabel1);
		JLabel1.setBounds(36,396,492,24);
		//}}
		
		initTab();
		
//		tModel = new DefaultTableModel(10,3);
		//addMouseListener(new Selection());

		//{{INIT_MENUS
		//}}
	
		//{{REGISTER_LISTENERS
		SymMouse aSymMouse = new SymMouse();
		theTable.addMouseListener(aSymMouse);
		SymAction lSymAction = new SymAction();
		showThePageButton.addActionListener(lSymAction);
		previousButton.addActionListener(lSymAction);
		nextButton.addActionListener(lSymAction);
		tokenizedPageButton.addActionListener(lSymAction);
		exitButton.addActionListener(lSymAction);
		//}}
		
		buttons=new Vector();
		buttons.addElement(this.showThePageButton);
		buttons.addElement(this.previousButton);
		buttons.addElement(this.nextButton);
		buttons.addElement(this.tokenizedPageButton);
		buttons.addElement(this.exitButton);
		int numberOfButtons=buttons.size();
		for(int i=0;i<numberOfButtons;i++){
		    SelectedButton b=(SelectedButton)(buttons.elementAt(i));
		    b.setFrame(this);
		    b.setID(i);
		}
		this.textEditFrame=new TextEditFrame();
	}

	public UrlTable(String sTitle,CommunicationNode cn)
	{
		this();
		this.communicationNode=cn;		
		if(this.communicationNode!=null)
    		this.setIsApplet(cn.isApplet());
		setTitle(sTitle);
//		if(communicationNode==null){ // applet?
		if(this.isApplet()){
			this.browser=new WebFrame();
			if(communicationNode!=null){
    			 this.browser.setIcons(this.communicationNode.getIconPlace());
			}
			return;
		}
		else{
           browser=(WebFrame)
              (this.communicationNode.applicationManager.spawnApplication2("WebFrame",
              "local"));
		}
          /*
         browser.pageArea.setContentType("text/html");
         browser.pageArea.setEditable(false);
         */
	}

	public void setVisible(boolean b)
	{
		if (b)
			setLocation(50, 50);
		super.setVisible(b);
	}

	static public void main(String args[])
	{
		(new UrlTable()).setVisible(true);
	}

	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
		Dimension size = getSize();

		super.addNotify();

		if (frameSizeAdjusted)
			return;
		frameSizeAdjusted = true;

		// Adjust size of frame according to the insets and menu bar
		Insets insets = getInsets();
		javax.swing.JMenuBar menuBar = getRootPane().getJMenuBar();
		int menuBarHeight = 0;
		if (menuBar != null)
			menuBarHeight = menuBar.getPreferredSize().height;
		setSize(insets.left + insets.right + size.width, insets.top + insets.bottom + size.height + menuBarHeight);
	}

	// Used by addNotify
	boolean frameSizeAdjusted = false;

	//{{DECLARE_CONTROLS
	javax.swing.JTextField keyWordField = new javax.swing.JTextField();
	javax.swing.JScrollPane TableScrollPane = new javax.swing.JScrollPane();
	javax.swing.JTable theTable = new javax.swing.JTable();
	controlledparts.ControlledButton showThePageButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton tokenizedPageButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton nextButton = new controlledparts.ControlledButton();
	controlledparts.ControlledButton previousButton = new controlledparts.ControlledButton();
	javax.swing.JLabel frequencyLabel = new javax.swing.JLabel();
	javax.swing.JTextField frequencyField = new javax.swing.JTextField();
	controlledparts.ControlledButton exitButton = new controlledparts.ControlledButton();
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	//}}

	//{{DECLARE_MENUS
	//}}


	class SymMouse extends java.awt.event.MouseAdapter
	{
		public void mousePressed(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == theTable)
				theTable_mousePressed(event);
		}
	}

	void theTable_mousePressed(java.awt.event.MouseEvent event)
	{
		// to do: code goes here.
		JTable table =(JTable)event.getSource();
		int x=table.getSelectedRow();
		int y=table.getSelectedColumn();
//			 System.out.println("mouse pressed("+x+","+y+")");
	    if(y!=1) return;
	    String url=(String)(tModel.getValueAt(x,y));
		theTable_mousePressed_Interaction1(event,url);
	}

	void theTable_mousePressed_Interaction1(java.awt.event.MouseEvent event,String url)
	{
		this.browser.setVisible(true);
		this.browser.gainFocus();
		this.browser.setKeywordsLabel(this.keyWordField.getText());
		this.browser.urlArea.setText(url);
		this.browser.mouseClickedAtButton(3); // load
	}

	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == showThePageButton)
				showThePageButton_actionPerformed(event);
			else if (object == previousButton)
				previousButton_actionPerformed(event);
			else if (object == nextButton)
				nextButton_actionPerformed(event);
			else if (object == tokenizedPageButton)
				tokenizedPageButton_actionPerformed(event);
			else if (object == exitButton)
				exitButton_actionPerformed(event);
		}
	}

	void showThePageButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		showThePageButton_actionPerformed_Interaction1(event);
	}

	void showThePageButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			showThePageButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void previousButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		previousButton_actionPerformed_Interaction1(event);
	}

	void previousButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			previousButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void nextButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		nextButton_actionPerformed_Interaction1(event);
	}

	void nextButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			nextButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void tokenizedPageButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		tokenizedPageButton_actionPerformed_Interaction1(event);
	}

	void tokenizedPageButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			tokenizedPageButton.show();
		} catch (java.lang.Exception e) {
		}
	}

	void exitButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		exitButton_actionPerformed_Interaction1(event);
	}

	void exitButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			exitButton.show();
		} catch (java.lang.Exception e) {
		}
	}
}