package ft;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/** displays the edge count of a digital input.
  */
public class EdgeButton extends EdgeView {
  /** initialize to zero, counting up.
      @param iface to observe.
      @param mask E1.. for sensor to be observed.
      @param text to be displayed in component; null to suppress.
    */
  public EdgeButton (final Interface iface, final int mask, String text) {
    super(iface, mask, text); delta = 1;
    count.setEditable(true);
    count.addActionListener(
      new ActionListener() {
        public void actionPerformed (ActionEvent e) {
          try {
            iface.set(mask, Integer.parseInt(count.getText().trim()), delta);
	      } catch (NumberFormatException nfe) { }
        }
      } // end of actionListener
    );
    Button how;
    add(how = new Button("+"), BorderLayout.EAST);
    how.setBackground(buttonBg); how.setForeground(buttonFg);
    how.addActionListener(new ActionListener() {
      public void actionPerformed (ActionEvent e) {
        try {
          iface.set(mask, Integer.parseInt(count.getText().trim()), -delta);
	  delta = -delta;
	  ((Button)e.getSource()).setLabel(delta > 0 ? "+" : "-");
	} catch (NumberFormatException nfe) { System.err.println(nfe); }
      }
    });
  }
  protected int delta;

// ------------------------------------------------------------------ properties

  /** button background */	private Color buttonBg;
  public Color getButtonBg () { return buttonBg; }
  public void setButtonBg (Color buttonBg) { this.buttonBg = buttonBg; }

  /** count foreground */	private Color buttonFg;
  public Color getButtonFg () { return buttonFg; }
  public void setButtonFg (Color buttonFg) { this.buttonFg = buttonFg; }

  static { Interface.class.getName(); }	// load properties
  { String prefix = getClass().getName();
    buttonBg = Color.getColor(prefix+".button.bg", Color.gray);
    buttonFg = Color.getColor(prefix+".button.fg", Color.black);
  }
}