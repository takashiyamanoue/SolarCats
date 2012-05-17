package application.pen;
public class MyRunnable implements Runnable {
	public void run() {
		new ConsoleAppend("\n--------\n");
		new RunStop();
	}
}
