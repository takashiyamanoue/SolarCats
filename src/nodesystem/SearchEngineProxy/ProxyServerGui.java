package nodesystem.SearchEngineProxy;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import nodesystem.MessageReceiver;
import webleap.*;


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
public class ProxyServerGui extends JFrame
implements MessageReceiver
{
	private JScrollPane messagePane;
	private JLabel jLabel1;
	private JTextField inputField;
	private JButton inputButton;
	private JScrollPane inputPane;
	public JTextArea messageArea;
	private ProxyServer proxyServer;

	public ProxyServerGui()
	{}
	
	public void setProxyServer(ProxyServer s){
		this.proxyServer=s;
	}
	
	public void initGui(){
		{
		    this.setLayout(null);	
			this.setPreferredSize(new java.awt.Dimension(442, 337));
			this.setSize(442, 337);
			{
				messagePane = new JScrollPane();
				getContentPane().add(messagePane, BorderLayout.CENTER);
				messagePane.setBounds(21, 14, 364, 203);
				{
					messageArea = new JTextArea();
					messagePane.setViewportView(messageArea);
				}
			}
			{
				inputPane = new JScrollPane();
				getContentPane().add(inputPane);
				inputPane.setBounds(21, 217, 301, 28);
				{
					inputField = new JTextField();
					inputPane.setViewportView(inputField);
				}
			}
			{
				inputButton = new JButton();
				getContentPane().add(inputButton);
				inputButton.setText("enter");
				inputButton.setBounds(322, 217, 63, 28);
				inputButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						inputButtonActionPerformed(evt);
					}
				});
			}
			{
				jLabel1 = new JLabel();
				getContentPane().add(jLabel1);
				jLabel1.setText("ex: http://www.yama-linux.cc.kagoshima-u.ac.jp/~dsr");
				jLabel1.setBounds(21, 257, 393, 31);
			}
		}
	}
	
	boolean noWindow;
	public void setNoWindow(boolean x){
		this.noWindow=x;
	}
	/**
	 * @param args
	 */


	public void receiveMessage(String s) {
		// TODO 自動生成されたメソッド・スタブ
		if(this.noWindow){
			System.out.println(s);
		}
		else{
			if(this.messageArea!=null){
				if(messageArea.getLineCount()>500){
					String x=messageArea.getText();
					String y=x.substring(x.length()/2);
					messageArea.setText(y);
				}
        		this.messageArea.append(s+"\n");	
//        		this.messagePane.getVerticalScrollBar().setValue(0);
			}
			else
				System.out.println(s);
		}
		
	}
	
	private void inputButtonActionPerformed(ActionEvent evt) {
//		System.out.println("inputButton.actionPerformed, event=" + evt);
		//TODO add your code for inputButton.actionPerformed
		String x=this.inputField.getText();
		if(this.proxyServer!=null)
		this.proxyServer.testSearchEngineAccess(x);
	}

}
