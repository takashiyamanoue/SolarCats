package application.av;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import javax.swing.JFrame;

import util.Parameters;

public class AVSkipSettingFrame extends JFrame
{
//	AVGui avgui;
	private JButton closeButton;

	private AudioSkipParametersFrame audioSkipParametersFrame1;
	private VideoSkipParametersFrame videoSkipParametersFrame1;
	public AVSkipSettingFrame(){
//		this.avgui=g;
		audioSkipParametersFrame1=new AudioSkipParametersFrame();
		videoSkipParametersFrame1=new VideoSkipParametersFrame();
		this.initGUI();
	}
    public Parameters getAudioSkipParameters(){
    	return (Parameters)(this.audioSkipParametersFrame1);
    }
    public Parameters getVideoSkipParameters(){
    	return (Parameters)(this.videoSkipParametersFrame1);
    }
    
    private void initGUI() {
    	try {
    		{
	    		getContentPane().setLayout(null);
    		}
    		{
    			getContentPane().add(audioSkipParametersFrame1);
    			audioSkipParametersFrame1.setBounds(0, 0, 350, 286);
    			audioSkipParametersFrame1.setBackground(new java.awt.Color(255,255,181));
    		}
    		{
    			getContentPane().add(videoSkipParametersFrame1);
    			{
    				closeButton = new JButton();
    				getContentPane().add(closeButton);
    				closeButton.setText("close");
    				closeButton.setBounds(119, 548, 77, 25);
    				closeButton.addActionListener(new ActionListener() {
    					public void actionPerformed(ActionEvent evt) {
    						closeButtonActionPerformed(evt);
    					}
    				});
    			}
    			videoSkipParametersFrame1.setBounds(0, 292, 350, 250);
    			videoSkipParametersFrame1.setBackground(new java.awt.Color(255,255,181));
    		}
    		{
	    		this.setSize(400, 621);
    		}
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
	}
    
	private void closeButtonActionPerformed(ActionEvent evt) {
//		System.out.println("closeButton.actionPerformed, event="+evt);
		//TODO add your code for closeButton.actionPerformed
		this.setVisible(false);
	}
}
