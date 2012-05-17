/*
    Ç±ÇÃ∏◊ΩÇÕ java.awt.Dialog ∏◊ΩÇägí£ÇµÇΩÇ‡ÇÃÇ≈Ç∑.
 */

package nodesystem;
import java.awt.Dialog;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Rectangle;

public class AboutDialog extends Dialog {
	void okButton_Clicked(Event event) {
		//{{CONNECTION
		// okButton ÇÃ ∏ÿØ∏ Ç…ÇÊÇ¡Çƒ Dialog ÇîÒï\é¶Ç…Ç∑ÇÈ
		hide();
		//}}
	}

	public AboutDialog(Frame parent, boolean modal) {

	    super(parent, modal);

		//{{INIT_CONTROLS
		setLayout(null);
		setSize(249,150);
		setVisible(false);
		label1.setText("JAVA äÓñ{±Ãﬂÿπ∞ºÆ›");
		add(label1);
		label1.setBounds(36,36,166,21);
		okButton.setLabel("OK");
		add(okButton);
		okButton.setBounds(96,84,66,27);
		setTitle(" ﬁ∞ºﬁÆ›èÓïÒ");
		//}}
	}

	public AboutDialog(Frame parent, String title, boolean modal) {
	    this(parent, modal);
	    setTitle(title);
	}

    public synchronized void show() {
    	Rectangle bounds = getParent().bounds();
    	Rectangle abounds = bounds();

    	move(bounds.x + (bounds.width - abounds.width)/ 2,
    	     bounds.y + (bounds.height - abounds.height)/2);

    	super.show();
    }

	public boolean handleEvent(Event event) {
	    if(event.id == Event.WINDOW_DESTROY) {
	        hide();
	        return true;
	    }
		if (event.target == okButton && event.id == Event.ACTION_EVENT) {
			okButton_Clicked(event);
		}
		return super.handleEvent(event);
	}

	//{{DECLARE_CONTROLS
	java.awt.Label label1 = new java.awt.Label();
	java.awt.Button okButton = new java.awt.Button();
	//}}
}
