package ft;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
/** displays the scaled value of an analog input.
  */
public class ScaledView extends AnalogView {
  /** initialize to zero, identity scale.
      @param iface to observe.
      @param mask EX.. for sensor to be observed.
      @param text to be displayed in component; null to suppress.
    */
  public ScaledView (final Interface iface, final int mask,
						final String text) {
    super(iface, mask, text);
    /** dialog to set scale of analog sensor.
        Not static, because it uses properties of ScaledView.
      */
    class Scaly extends Dialog {
      public Scaly (Frame owner) {
        super(owner, text == null ? "" : text, true);
	setLayout(new GridLayout(0, 1));

        Button button;
        Label label;

        Panel line = new Panel(new GridLayout(1, 0));
          Panel panel = new Panel(new BorderLayout());
          panel.add(label = new Label("a: ", Label.RIGHT), BorderLayout.WEST);
            label.setBackground(getLabelBg());
	    label.setForeground(getLabelFg());
          panel.add(a = new TextField(5), BorderLayout.CENTER);
            a.setBackground(Color.white); a.setForeground(Color.black);
          line.add(panel);

          panel = new Panel(new BorderLayout());
          panel.add(label = new Label("b: ", Label.RIGHT), BorderLayout.WEST);
            label.setBackground(getLabelBg());
	    label.setForeground(getLabelFg());
          panel.add(b = new TextField(5), BorderLayout.CENTER);
            b.setBackground(Color.white); b.setForeground(Color.black);
          line.add(panel);
        add(line);

        line = new Panel(new BorderLayout());
          panel = new Panel(new BorderLayout());
          panel.add(label = new Label("sample E1..8 vs EX..Y: ", Label.RIGHT),
                                                          BorderLayout.WEST);
            label.setBackground(getLabelBg());
	    label.setForeground(getLabelFg());
          panel.add(query = new TextField(3), BorderLayout.CENTER);
            query.setBackground(Color.white); query.setForeground(Color.black);
          line.add(panel, BorderLayout.CENTER);

          panel = new Panel(new GridLayout(1, 0));
          panel.add(button = new Button("no"));
          button.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
              Scaly.this.setVisible(false);
            }
          });
          panel.add(button = new Button("ok"));
          button.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
              Scaly.this.setVisible(false);
	      try {
	        float a = new Float(Scaly.this.a.getText().trim()).floatValue();
	        float b = new Float(Scaly.this.b.getText().trim()).floatValue();
// JDK 1.3.0 completely botches the next line
		iface.setScale(mask, a, b);
	      } catch (NumberFormatException nfe) { }
	      try {
	        int query = Integer.parseInt(Scaly.this.query.getText().trim());
// JDK 1.3.0 completely botches the next line
		iface.setQuery(query);
	      } catch (NumberFormatException nfe) { }
            }
	  });
          line.add(panel, BorderLayout.EAST);
        add(line);

        addWindowListener(new WindowAdapter() {
          public void windowClosing (WindowEvent e) {
            Scaly.this.setVisible(false);
          }
        });
        pack();
      }
      protected final TextField a;
      protected final TextField b;
      protected final TextField query;
      /** retrieve current parameters.
        */
      public void init () {
        float[] s = iface.getScale(mask);
	a.setText(s[0]+""); b.setText(s[1]+"");
	query.setText(iface.getQuery()+"");
      }
    }
    
    Button scale;
    add(scale = new Button("%"), BorderLayout.EAST);
    scale.setBackground(buttonBg); scale.setForeground(buttonFg);
    scale.addActionListener(new ActionListener() {
      protected Scaly scaly = new Scaly(getFrame());
      public void actionPerformed (ActionEvent e) {
	scaly.init(); scaly.show();
      }
    });
    iface.setScale(mask, 1f, 0f);
  }
  /** dialog needs frame to attach to.
      This creates a fake frame if it is run in an applet.
    */
  protected Frame getFrame () {
    if (frame != null) return frame;
    for (Container parent = getParent(); parent != null;
						parent = parent.getParent())
      if (parent instanceof Frame) return frame = (Frame)parent;
    return frame = new Frame();		// should not happen
  }
  protected Frame frame;

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