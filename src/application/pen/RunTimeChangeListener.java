package application.pen;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class RunTimeChangeListener implements ChangeListener{
	private JSlider JSlider;
	
	public RunTimeChangeListener(JSlider js){
		JSlider = js;
		JSlider.setToolTipText("0.0•b");
	}
	
	public void stateChanged(ChangeEvent e) {
		double i = JSlider.getValue();
		i /= 1000;
		JSlider.setToolTipText(i + "•b");
	}
}
