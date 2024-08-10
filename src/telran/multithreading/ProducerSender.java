package telran.multithreading;

import java.util.concurrent.BlockingQueue;

public class ProducerSender extends Thread {
	private final BlockingQueue<String> evenMessageBox;
	private final BlockingQueue<String> oddMessageBox;
	private final int nMessages;

	public ProducerSender(BlockingQueue<String> oddMessageBox, BlockingQueue<String> evenMessageBox,  int nMessages) {
		this.evenMessageBox = evenMessageBox;
		this.oddMessageBox = oddMessageBox;
		this.nMessages = nMessages;
	}

	@Override
	public void run() {
		for (int i = 1; i <= nMessages; i++) {
			try {
				String message = "Message " + i;
				if (i % 2 == 0) {
					evenMessageBox.put(message);
				} else {
					oddMessageBox.put(message);
				}
			} catch (InterruptedException e) {
				//no interrupt logics
			}
		}
	}
}
