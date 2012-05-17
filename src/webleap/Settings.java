package webleap;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.io.File;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import language.LispAndFile;
import language.LispObject;
import language.MyString;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class Settings extends javax.swing.JFrame 
{
	static public String passWord="tenmonkan";
	static public String port="8089";
	
	boolean thisIsApplet;
	
	public void setIsApplet(boolean x){
		this.thisIsApplet=x;
	}
	public boolean isApplet(){
		return thisIsApplet;
	}
	
    void saveSettings()
    {
        LispAndFile lisp= new LispAndFile();
        String userPath=System.getProperty("user.dir");
        File f=new File(userPath, "webleap.cfg");
        this.settingsToEnv(lisp);
        lisp.plist("",lisp.environment);
        lisp.saveEnv(f.toString());
    }

    String baseUrl="";
    public void setBaseUrl(String x){
    	this.baseUrl=x;
    }
    
    void readSettings()
    {
        LispAndFile lisp= new LispAndFile();
   	
    	if(this.isApplet()){
           if(this.baseUrl!=null){
        	   lisp.readEnvFromUrl(baseUrl+"webleap.cfg");
        	   this.envToSettings(lisp);
           }
    	}
    	else{
           String userPath=System.getProperty("user.dir");
           File f=new File(userPath, "webleap.cfg");
           if(f.exists()){
              lisp.readEnv(f.toString());
              this.envToSettings(lisp);
           }
    	}
    }
    
    public String getProxyKind(){
    	String x="no";
    	if(this.generalProxyButton.isSelected()){
    		x="general";
    	}
    	else if(this.webLeapProxyButton.isSelected()){
    		x="webleap";
    	}
    	else if(this.noProxyButton.isSelected()){
    		x="no";
    	}
    	return x;
    }
    public String getPassWord(){
    	return this.passwordField.getText();
    }

    public Object tlabel[]={"search-engine","driver","url",
		                  "query-head","word-separator", "query-tail",
		                  "frequency-format",
		                  "answer-list-format",
		                  "info-position",
		                  "url-pattern",
		                  "next-list",
		                  "previous-list",
		                  "comments"};

    public void settingsToEnv(LispAndFile lisp)
    {
        /*
           config file format
           
           file name: webleap.cfg
           
           (contents)
           
           ' comments
           (
           (search-engine  ("Altavista" "Google"))
           (initial-engine "Altavista")
           (config (
             (
                (search-engine "Altavista") 
                (driver "")
                (url "")
                (qery-head "web/results?q=%22")
                (word-separator "+")
                (query-tail "%22&kgs=0&kls=0&avkw=qtrp")
                (frequency-format "found * results")
                (answer-list-format "</b> * <br> * * * * </span>")
                (info-position "5,3,3,0")
                (url-pattern "URL: *")
                (next-list "")
                (previous-list "")
                (comments "")
              ) 
              (
                (search-engine "Google")
                (driver "GoogleDriver")
                (licence-key "")
                (ajax-licence-key "")
                (search-option "none"
                             "free"
                             "site:us"
                             "site:uk"
                             "site:au"
                             "site:ca"
                             "site:nz"
                             "site:de"
                             "site:fr"
                             "site:cn"
                             "site:kr"
                             "site:sg"
                             "site:hk"
                             "site:jp"
                )
              )
            ))
            
        */
        int i=0;
        
        // new search engine list
//        int sosize=this.searchEngineComboBox.getSize();
//        for(i=0;i<sosize;i++){
            this.searchEngineComboBox.removeAll();
//        }
//        this.searchEngineComboBox.removeAllItems();
        for( i=0;i<10;i++){
        	String sname="";
        	try{
                sname=(String)(this.searchEngineSettingTab.getValueAt(i,0));
        	}
        	catch(Exception e){
        		break;
        	}
            if(sname==null) break;
            if(sname.equals("")) break;
            this.searchEngineComboBox.addItem(sname);
        }
        
//      lisp.environment=lisp.nilSymbol;
        
        // bind the search-engine list
        LispObject l=lisp.nilSymbol;
//        int numberOfEngine=searchEngineComboBox.getItemCount();
        int numberOfEngine=i;
        for(i=1;i<=numberOfEngine;i++){
            String e=(String)(searchEngineComboBox.getItemAt(
                           numberOfEngine-i));
            MyString en= new MyString(e);
            l=lisp.cons(en,l);
        }
//        l=lisp.cons(l,lisp.nilSymbol);
//        l=lisp.cons(lisp.recSymbol("quote"),l);
        try{
           lisp.setf(lisp.recSymbol("search-engine"),l);
        }
        catch(Exception e){}
        // bind the initial engine 
        String initEngine=
          (String)(searchEngineComboBox.getSelectedItem());
        l=new MyString(initEngine);
//        l=lisp.cons(new MyString(initEngine),lisp.nilSymbol);
//        l=lisp.cons(lisp.recSymbol("quote"),l);
        try{
           lisp.setf(lisp.recSymbol("initial-engine"),l );
        }
        catch(Exception e){}
        // make google search-option list
        LispObject so=lisp.nilSymbol;
        int numberOfOption=this.searchOptionComboBox.getItemCount();
        for(i=1;i<=numberOfOption;i++){
            String oname=((String)(this.searchOptionComboBox.getItemAt(
                numberOfOption-i)));
            so=lisp.cons(new MyString(oname),so);
        }
        so=lisp.cons(lisp.recSymbol("search-option"),so);
        LispObject licenceKey=lisp.nilSymbol;
        licenceKey=lisp.cons(new MyString(this.googleLicenceKeyField.getText()),
                              licenceKey);
        licenceKey=lisp.cons(lisp.recSymbol("licence-key"),licenceKey); 
        
        LispObject licenceKey2=lisp.nilSymbol;
        licenceKey2=lisp.cons(new MyString(this.googleAjaxSearchLicenceKeyField.getText()),
        		              licenceKey2);
        licenceKey2=lisp.cons(lisp.recSymbol("ajax-licence-key"),licenceKey2);

        // make the each engine's config list
        LispObject cfglist=lisp.nilSymbol;
        for(i=1;i<=numberOfEngine; i++){
            LispObject cfg=lisp.nilSymbol;
            int numberOfParm=this.tlabel.length;
            String sn=(String)(this.searchEngineSettingTab.getValueAt(
                numberOfEngine-i,0));
            if(sn.equals("Google")){
                cfg=lisp.cons(so,cfg);
                cfg=lisp.cons(licenceKey,cfg);
                cfg=lisp.cons(licenceKey2,cfg);
            }
            int j;
            for(j=1;j<=numberOfParm;j++){
                String label=(String)(this.tlabel[numberOfParm-j]);
                String val=(String)(this.searchEngineSettingTab.getValueAt(
                   numberOfEngine-i,numberOfParm-j));
                LispObject w=lisp.nilSymbol;
                w=lisp.cons(new MyString(val),w);
                w=lisp.cons(lisp.recSymbol(label),w);
                cfg=lisp.cons(w,cfg);
            }
//            cfg=lisp.cons(new MyString(sn),cfg);            
            cfglist=lisp.cons(cfg,cfglist);           
         }
//        l=lisp.cons(cfglist,lisp.nilSymbol);
//        l=lisp.cons(lisp.recSymbol("quote"),l);
         try{
           lisp.setf(lisp.recSymbol("config"),cfglist);
         }
         catch(Exception e){}
         
         // make the proxy config
         LispObject proxy=lisp.nilSymbol;
         proxy=lisp.cons(new MyString(this.proxyPortField.getText()),proxy);
         proxy=lisp.cons(new MyString(this.proxyNameField.getText()),proxy);
         String tf="false";
//         if(this.useProxyCheckBox.isSelected()){
         if(this.generalProxyButton.isSelected()){
            tf="general";
         }
         else
         if(this.webLeapProxyButton.isSelected()){
        	 tf="webleap";
         }
         else
         if(this.noProxyButton.isSelected()){
        	 tf="no";
         }
         proxy=lisp.cons(new MyString(tf),proxy);
         try{
           lisp.setf(lisp.recSymbol("proxy"),proxy);
         }
         catch(Exception e){}
     }
     
    public void envToSettings(LispAndFile lisp)
    {
        /*
           config file format
           
           file name: webleap.cfg
           
           (contents)
           ' comments
           (
           (search-engine  ("Altavista" "Google"))
           (initial-engine "Altavista")
           (config (
             (
                (search-engine "Altavista") 
                (driver "")
                (url "")
                (qery-head "web/results?q=%22")
                (word-separator "+")
                (query-tail "%22&kgs=0&kls=0&avkw=qtrp")
                (frequency-format "found * results")
                (answer-list-format "</b> * <br> * * * * </span>")
                (info-position "5,3,3,0")
                (url-pattern "URL: *")
                (next-list "")
                (previous-list "")
                (comments "")
              ) 
              (
                (search-engine "Google")
                (driver "GoogleDriver")
                (license-key "")
                (search-option "none"
                             "free"
                             "site:us"
                             "site:uk"
                             "site:au"
                             "site:ca"
                             "site:nz"
                             "site:de"
                             "site:fr"
                             "site:cn"
                             "site:kr"
                             "site:sg"
                             "site:hk"
                             "site:jp"
                )
              )
            ))
           
            
        */
        LispObject searchEngine=lisp.nilSymbol;
        LispObject initEngine=lisp.nilSymbol;
        LispObject config=lisp.nilSymbol;
        LispObject proxy=lisp.nilSymbol;
        LispObject w=lisp.nilSymbol;
        if(lisp.environment==null) return;

        w=   lisp.assoc(lisp.recSymbol("search-engine"),lisp.car(lisp.environment));
        searchEngine=lisp.car(lisp.cdr(w));
        
        w=lisp.assoc(lisp.recSymbol("initial-engine"),lisp.car(lisp.environment));
        initEngine=lisp.car(lisp.cdr(w ));
  
        w=lisp.assoc(lisp.recSymbol("config"),lisp.car(lisp.environment));
        config= lisp.car(lisp.cdr(w));
        
       // set proxy
        w=lisp.assoc(lisp.recSymbol("proxy"),lisp.car(lisp.environment));
        if(!lisp.Null(w)){
           proxy=lisp.car(lisp.cdr(w));
       
           String tf=((MyString)(lisp.car(proxy))).val;
           if(tf.equals("general"))
//              this.useProxyCheckBox.setSelected(true);
        	   this.generalProxyButton.setSelected(true);
           else 
           if(tf.equals("webleap"))
//              this.useProxyCheckBox.setSelected(false);
        	   this.webLeapProxyButton.setSelected(true);
           else
           if(tf.equals("no"))
        	   this.noProxyButton.setSelected(true);
           String paddress=((MyString)(lisp.second(proxy))).val;
           this.proxyNameField.setText(paddress);
           String pport=((MyString)(lisp.third(proxy))).val;
           this.proxyPortField.setText(pport);
        }       
        
       // set search engine names
       this.searchEngineComboBox.removeAllItems();
       LispObject p=searchEngine;
       while(!lisp.Null(p)){
            String seName=((MyString)(lisp.car(p))).val;
            searchEngineComboBox.addItem(seName);
            p=lisp.cdr(p);
       }
       this.searchEngineComboBox.setVisible(true);
       
       // set initial search engine
       String sitem=((MyString)initEngine).val;
       searchEngineComboBox.setSelectedItem(sitem);
       
       p=config;
       int numberOfParm=this.tlabel.length;
       int i=0;
       while(!lisp.Null(p)){
           LispObject aconfig=lisp.car(p);
                
           //set each engines's parameters
           for(int j=0;j< numberOfParm;j++){
              String key=(String)(this.tlabel[j]);
              LispObject x=lisp.assoc(lisp.recSymbol(key),aconfig);
              String val=((MyString)(lisp.car(lisp.cdr(x)))).val;
              this.searchEngineSettingTab.setValueAt(val,i,j);
           }
           
           // set google api license key
           LispObject lk=lisp.assoc(lisp.recSymbol("licence-key"),aconfig);
           if(!lisp.Null(lk)){
              lk=lisp.car(lisp.cdr(lk) );
              this.googleLicenceKeyField.setText(((MyString)lk).val);
           }
           LispObject lk2=lisp.assoc(lisp.recSymbol("ajax-licence-key"),aconfig);
           if(!lisp.Null(lk2)){
               lk2=lisp.car(lisp.cdr(lk2) );
               this.googleAjaxSearchLicenceKeyField.setText(((MyString)lk2).val);
            }
          
           // set google search option
           LispObject opt=lisp.assoc(lisp.recSymbol("search-option"),aconfig);
           if(!lisp.Null(opt)){
              opt=lisp.cdr(opt);
              this.searchOptionComboBox.removeAllItems();               
           }
           while(!lisp.Null(opt)){
               String optx=((MyString)(lisp.car(opt))).val;
               this.searchOptionComboBox.addItem(optx);
               opt=lisp.cdr(opt);
           }
           this.searchOptionComboBox.repaint();
           
           // next config
           p=lisp.cdr(p);
           i++;
       }
    }
    public KwicTable kwicTable;

    public void setKwicTable(KwicTable t)
    {
        this.kwicTable=t;
    }

    public SearchEngineDriver getSearchEngineDriver()
    {
        this.setSearchEngineDriver();
        return this.searchEngineDriver;
    }

    public void setSearchEngineDriver()
    {
        this.searchEngineComboBox_itemStateChanged_Interaction1(null);
    }

    public void setSearchEngineDriver(String driverName)
    {
        Class theClass;
        if(driverName.equals("")){
            this.searchEngineDriver=new GeneralDriver();
            return;
        }
        if(this.isApplet()){
        	this.searchEngineDriver=new GeneralDriver();
        	return;
        }
        else{
           try{
              theClass=Class.forName("webleap."+driverName);
           }
           catch(ClassNotFoundException e){
               System.out.print("class not found exception occurd while loading "
               +driverName+".\n");
               return;
           }
        
           try{
                 searchEngineDriver=(SearchEngineDriver)(theClass.newInstance());
           }
           catch(InstantiationException e)
           {
               System.out.print("Instatiation Exception occurd while spawning "+driverName+".\n");
               return;
           }
           catch(IllegalAccessException e)
           {
               System.out.print("illegal access exception occurd while spawning "+driverName+".\n");
               return;
           }
           catch(Exception e)
           {
               System.out.print("exception " + e + " is occured while spawning " + driverName + ".\n");
               Thread.dumpStack();
               return ;
           }
        }
    }

    public SearchEngineDriver searchEngineDriver;

    public DefaultTableModel tModel;


    public void initTab()
    {
    	/*
    public Object tlabel[]={"search-engine","driver","url",
		                  "query-head","word-separator", "query-tail",
		                  "frequency-format",
		                  "answer-list-format",
		                  "info-position",  // <url><><><>
		                  "url-pattern", 
		                  "next-list",
		                  "previous-list",
		                  "comments"};
    	 * 
    	 */
		Object tdata[][]={ {"Altavista","","http://www.altavista.com/",
		                    "web/results?q=%22","+",
		                     "%22&kgs=0&kls=0&avkw=qtrp",
		                     "found * results",
		                     "</b> * <br> * * * * </span>",
		                     "5,3,3,0","URL: *","","",""},
		                     
		                     {"Google","GoogleDriver","","","","","","","","","","",""},
		                     
		                     {"GoogleAjax","GoogleAjaxSearchDriver","","","","","","","","","","",""},
		                     {"GoogleWeb","","http://www.google.com/",
		                      "search?hl=en&q=%22","+",
		                      "%22&btnG=Google+%E6%A4%9C%E7%B4%A2&lr=",
		                      " of about <b> * </b> for ",
		                      "<div class=g> * * <b> * </b> * * * </div>",
		                      "5,3,3,0","URL: *","","",""
		                     },
		                     {"GooWeb","","http://search.goo.ne.jp/",
		                      "ml/web_en.jsp?MT=%22","+",
		                      "%22&IE=EUC-JP&PT=webtu&from=webtu",
		                      "of <strong>about * </strong> for",
		                      "<div style=\"margin-left:5px\"> * <font size=\"-1\"> * <b> * </b> * <br>",
		                      //0. <div style=\"margin-left:5px\">
		                      //1. 1&nbsp;   
		                      //2. <a href="" >
		                      //3. *
		                      //4. <b>
		                      //5. "keywords"
		                      //6. </b>
		                      //7. *
		                      //8. </a>
		                      //9. <br>
		                      //10. 
		                      //11. <div class="r">
		                      //12.  
		                      //13. <font size=\"-1\">
		                      //14. *
		                      //15. <b>
		                      //16. *
		                      //17. </b>
		                      //18. *
		                      "0,0,1,2,3","URL: *","","",""
		                      // <url>
		                      // <title>
		                      // <snippet>
		                     }
		};
		                     
        tModel = new DefaultTableModel(tdata,tlabel);
		searchEngineSettingTab.setModel(tModel);
		
        TableColumn column = null;
        column=searchEngineSettingTab.getColumnModel().getColumn(0); //name
        column.setMinWidth(40); //sport column is smaller
        column.setMaxWidth(150);
        column=searchEngineSettingTab.getColumnModel().getColumn(1); //url
        column.setMinWidth(60); //sport column is middle
        column.setMaxWidth(200);
        column=searchEngineSettingTab.getColumnModel().getColumn(2); //query head
        column.setMinWidth(60); //sport column is middle
        column.setMaxWidth(250);
        column=searchEngineSettingTab.getColumnModel().getColumn(3); //separator
        column.setMinWidth(20); //sport column is middle
        column.setMaxWidth(40);
        column=searchEngineSettingTab.getColumnModel().getColumn(4); //query tail
        column.setMinWidth(60); //sport column is middle
        column.setMaxWidth(250);
        column=searchEngineSettingTab.getColumnModel().getColumn(5); //frequency format
        column.setMinWidth(30); //sport column is middle
        column.setMaxWidth(150);
        column=searchEngineSettingTab.getColumnModel().getColumn(6); //answer
        column.setMinWidth(60); //sport column is middle
        column.setMaxWidth(250);
        column=searchEngineSettingTab.getColumnModel().getColumn(7); //positions
        column.setMinWidth(60); //sport column is middle
        column.setMaxWidth(250);
        column=searchEngineSettingTab.getColumnModel().getColumn(8); //comment
        column.setMinWidth(60); //sport column is middle
        column.setMaxWidth(250);
        
        int rowCount=searchEngineSettingTab.getRowCount();
        for(int i=0;i<rowCount;i++){
            String seName=(String)(searchEngineSettingTab.getValueAt(i,0));
            searchEngineComboBox.addItem(seName);
        }
        this.searchEngineComboBox.setSelectedIndex(1);
        this.searchOptionComboBox.addItem("none");
        this.searchOptionComboBox.addItem("free");
        this.searchOptionComboBox.addItem("site:.uk");
        this.searchOptionComboBox.addItem("site:.us");
        this.searchOptionComboBox.addItem("site:.au");
        this.searchOptionComboBox.addItem("site:.ca");
        this.searchOptionComboBox.addItem("site:.nz");
        this.searchOptionComboBox.addItem("site:.de");
        this.searchOptionComboBox.addItem("site:.fr");
        this.searchOptionComboBox.addItem("site:.cn");
        this.searchOptionComboBox.addItem("site:.sg");
        this.searchOptionComboBox.addItem("site:.kr");
        this.searchOptionComboBox.addItem("site:.jp");
        
        this.googleLicenceKeyField.setText(
//          "****************************"
        		""
          ); // t.yamanoue, google api access key, 15 May 2003
          
        this.googleAjaxSearchLicenceKeyField.setText(
//       		"*******************");
        		"");
        // set initial search engine
        String sitem="GooWeb";
        searchEngineComboBox.setSelectedItem(sitem);
        this.proxyNameField.setText("yama-linux.cc.kagoshima-u.ac.jp");
        this.proxyPortField.setText(this.port);
        this.passwordField.setText(this.passWord);

        try{
           this.readSettings();
        }
        catch(Exception e){
        	
        }
        this.setSearchEngineDriver();
    }

	public Settings()
	{
		//{{INIT_CONTROLS
		setTitle("Search Enginge Settings");
		getContentPane().setLayout(null);
		this.setSize(736, 426);
		setVisible(false);
		tablePane.setOpaque(true);
		getContentPane().add(tablePane);
		tablePane.setBounds(21, 231, 693, 126);
		tablePane.getViewport().add(searchEngineSettingTab);
		searchEngineSettingTab.setBounds(0,0,692,0);
		JLabel1.setText("WebLEAP Settings");
		getContentPane().add(JLabel1);
		JLabel1.setBounds(24,12,240,36);
		proxyNameField.setText("www.tobata.isc.kyutech.ac.jp");
		getContentPane().add(proxyNameField);
		proxyNameField.setBounds(119, 182, 350, 21);
		JLabel5.setText("proxy");
		getContentPane().add(JLabel5);
		JLabel5.setBounds(21, 161, 42, 21);
		proxyPortField.setText("8080");
		getContentPane().add(proxyPortField);
		proxyPortField.setBounds(469, 182, 77, 21);
		searchEngineComboBox.setToolTipText("select a search engine for webleap");
		getContentPane().add(searchEngineComboBox);
		searchEngineComboBox.setBounds(252,60,240,24);
		JLabel2.setText("Search Engine :");
		getContentPane().add(JLabel2);
		JLabel2.setBounds(24,60,120,24);
		JLabel3.setText("Setting of search engines");
		getContentPane().add(JLabel3);
		JLabel3.setBounds(21, 203, 203, 21);
		JLabel4.setText("search options (for google)");
		getContentPane().add(JLabel4);
		JLabel4.setBounds(24,84,216,24);
		getContentPane().add(searchOptionComboBox);
		searchOptionComboBox.setBounds(252,84,240,24);
		getContentPane().add(searchOptionField);
		searchOptionField.setBounds(492,84,228,24);
		getContentPane().add(googleLicenceKeyField);
		googleLicenceKeyField.setBounds(252,108,468,24);
		getContentPane().add(googleAjaxSearchLicenceKeyField);
		googleAjaxSearchLicenceKeyField.setBounds(252,132,468,24);
		JLabel6.setText("Google API Licence Key(for google)");
		JLabel6.setBounds(0, -21, 217, 21);
		JLabel7.setText("Google Ajax API Licence Key");
		getContentPane().add(JLabel7);
		JLabel7.setBounds(24,132,216,24);
		JLabel7.add(JLabel6);
		exitButton.setText("exit");
		exitButton.setActionCommand("exit");
		getContentPane().add(exitButton);
		exitButton.setBounds(636,12,84,24);
		saveButton.setText("save");
		saveButton.setActionCommand("save");
		getContentPane().add(saveButton);
		saveButton.setBounds(636,36,84,24);
		//}}
		{
			generalProxyButton = new JRadioButton();
			getContentPane().add(generalProxyButton);
			generalProxyButton.setText("general proxy");
			generalProxyButton.setBounds(280, 161, 119, 21);
		}
		{
			webLeapProxyButton = new JRadioButton();
			getContentPane().add(webLeapProxyButton);
			webLeapProxyButton.setText("webleap proxy");
			webLeapProxyButton.setBounds(154, 161, 119, 21);
		}
		{
			noProxyButton = new JRadioButton();
			getContentPane().add(noProxyButton);
			noProxyButton.setText("no proxy");
			noProxyButton.setBounds(63, 161, 77, 21);
		}
		{
			proxyGroup = new ButtonGroup();
			proxyGroup.add(generalProxyButton);
			proxyGroup.add(webLeapProxyButton);
			proxyGroup.add(noProxyButton);
		}
		this.webLeapProxyButton.setSelected(true);
		{
			proxyLabel = new JLabel();
			getContentPane().add(proxyLabel);
			proxyLabel.setText("addrress/port");
			proxyLabel.setBounds(35, 182, 84, 21);
		}
		{
		    passLabel = new JLabel();
			getContentPane().add(passLabel);
			passLabel.setText("webleap proxy password");
			passLabel.setBounds(567, 161, 133, 21);
		}
		{
			passwordField = new JTextField();
			getContentPane().add(passwordField);
			passwordField.setBounds(567, 182, 112, 21);
		}
//        initTab();

		//{{REGISTER_LISTENERS
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymItem lSymItem = new SymItem();
		searchEngineComboBox.addItemListener(lSymItem);
		SymComponent aSymComponent = new SymComponent();
		searchEngineSettingTab.addComponentListener(aSymComponent);
		searchOptionComboBox.addItemListener(lSymItem);
		SymAction lSymAction = new SymAction();
		exitButton.addActionListener(lSymAction);
		saveButton.addActionListener(lSymAction);
		//}}

		//{{INIT_MENUS
		//}}

	}

	public Settings(String title)
	{
		this();
		setTitle(title);
	}
	public void setVisible(boolean b)
	{
		if(b)
		{
			setLocation(50, 50);
		}
	super.setVisible(b);
	}

	public void addNotify()
	{
		// Record the size of the window prior to calling parents addNotify.
		Dimension d = getSize();

		super.addNotify();

		if (fComponentsAdjusted)
			return;

		// Adjust components according to the insets
		Insets ins = getInsets();
		setSize(ins.left + ins.right + d.width, ins.top + ins.bottom + d.height);
		Component components[] = getContentPane().getComponents();

//		components.setBounds(50, 200, 50, 30);

		for (int i = 0; i < components.length; i++)
			{
			Point p = components[i].getLocation();
			p.translate(ins.left, ins.top);
			components[i].setLocation(p);
		}
		fComponentsAdjusted = true;
	}

	// Used for addNotify check.
	boolean fComponentsAdjusted = false;

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
		Object object = event.getSource();
		if (object == Settings.this)
			setting_WindowClosing(event);
		}
	}

	void setting_WindowClosing(java.awt.event.WindowEvent event)
	{
//		dispose();		 // dispose of the Frame.
        this.hide();
	}
	//{{DECLARE_CONTROLS
	javax.swing.JScrollPane tablePane = new javax.swing.JScrollPane();
	javax.swing.JTable searchEngineSettingTab = new javax.swing.JTable();
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	public javax.swing.JTextField proxyNameField = new javax.swing.JTextField();
	javax.swing.JLabel JLabel5 = new javax.swing.JLabel();
	public javax.swing.JTextField proxyPortField = new javax.swing.JTextField();
	javax.swing.JComboBox searchEngineComboBox = new javax.swing.JComboBox();
	javax.swing.JLabel JLabel2 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel3 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel4 = new javax.swing.JLabel();
	javax.swing.JComboBox searchOptionComboBox = new javax.swing.JComboBox();
	public javax.swing.JTextField searchOptionField = new javax.swing.JTextField();
	javax.swing.JTextField googleLicenceKeyField = new javax.swing.JTextField();
	javax.swing.JTextField googleAjaxSearchLicenceKeyField = new javax.swing.JTextField();
	javax.swing.JLabel JLabel6 = new javax.swing.JLabel();
	javax.swing.JLabel JLabel7 = new javax.swing.JLabel();
	javax.swing.JButton exitButton = new javax.swing.JButton();
	private JLabel proxyLabel;
	private JLabel passLabel;
	private JTextField passwordField;
	public JRadioButton noProxyButton;
	private ButtonGroup proxyGroup;
	public JRadioButton webLeapProxyButton;
	public JRadioButton generalProxyButton;
	javax.swing.JButton saveButton = new javax.swing.JButton();
	//}}

	//{{DECLARE_MENUS
	//}}


	class SymItem implements java.awt.event.ItemListener
	{
		public void itemStateChanged(java.awt.event.ItemEvent event)
		{
			Object object = event.getSource();
			if (object == searchEngineComboBox)
				searchEngineComboBox_itemStateChanged(event);
			else if (object == searchOptionComboBox)
				searchOptionComboBox_itemStateChanged(event);
		}
	}

	void searchEngineComboBox_itemStateChanged(java.awt.event.ItemEvent event)
	{
		// to do: code goes here.
			 
		searchEngineComboBox_itemStateChanged_Interaction1(event);
	}

	void searchEngineComboBox_itemStateChanged_Interaction1(java.awt.event.ItemEvent event)
	{
		try {
			// searchEngineSettingTab Show the JTable
			searchEngineSettingTab.setVisible(true);
		} catch (Exception e) {
		}
        int selectedEngine=this.searchEngineComboBox.getSelectedIndex();
        try{
           String driver=(String)(this.searchEngineSettingTab.getValueAt(selectedEngine,1));
           this.setSearchEngineDriver(driver);
        }
        catch(Exception e){
        	
        }
	}

	class SymComponent extends java.awt.event.ComponentAdapter
	{
		public void componentShown(java.awt.event.ComponentEvent event)
		{
			Object object = event.getSource();
			if (object == searchEngineSettingTab)
				searchEngineSettingTab_componentShown(event);
		}
	}

	void searchEngineSettingTab_componentShown(java.awt.event.ComponentEvent event)
	{
		// to do: code goes here.
			 
		searchEngineSettingTab_componentShown_Interaction1(event);
	}

	void searchEngineSettingTab_componentShown_Interaction1(java.awt.event.ComponentEvent event)
	{
		try {
			// searchEngineComboBox Set selected item
//			searchEngineComboBox.setSelectedItem("");
		} catch (Exception e) {
		}
	}

	void searchOptionComboBox_itemStateChanged(java.awt.event.ItemEvent event)
	{
		// to do: code goes here.
			 
		searchOptionComboBox_itemStateChanged_Interaction1(event);
	}

	void searchOptionComboBox_itemStateChanged_Interaction1(java.awt.event.ItemEvent event)
	{
		try {
			// convert int->class java.lang.String; searchOptionComboBox Get the selected index
			String opt=(String)(searchOptionComboBox.getSelectedItem());
			if(opt.equals("none")){
		    	this.searchOptionField.setText("");
			}
			else
			if(opt.equals("free")){
			    int dmy=1;
			}
			else {
			    this.searchOptionField.setText(opt);
			}
		} catch (java.lang.Exception e) {
		}
	}
    

	class SymAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == exitButton)
				exitButton_actionPerformed(event);
			else if (object == saveButton)
				saveButton_actionPerformed(event);
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
//			exitButton.show();
		} catch (java.lang.Exception e) {
		}
		this.show(false);
	}

	void saveButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		saveButton_actionPerformed_Interaction1(event);
	}

	void saveButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
//			saveButton.show();
		} catch (java.lang.Exception e) {
		}
		this.saveSettings();
	}
}