package telran.multithreading;

import java.util.Arrays;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class SenderReceiverAppl {

	
	private static final int N_MESSAGES = 2000;
	private static final int N_RECEIVERS = 10;

	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<String> oddMessageBox = new LinkedBlockingQueue<>();
		BlockingQueue<String> evenMessageBox = new LinkedBlockingQueue<>();
		
		
		ProducerSender sender = startSender(oddMessageBox, evenMessageBox,  N_MESSAGES);
		ConsumerReceiver[] receivers = startReceivers(oddMessageBox, evenMessageBox,  N_RECEIVERS);
		
		sender.join();
		stopReceivers(receivers);
		displayResult();
		
		

	}

	private static void displayResult() {
		System.out.printf("counter of processed messsages is %d\n",
				ConsumerReceiver.getMessagesCounter());
		
	}

	private static void stopReceivers(ConsumerReceiver[] receivers) throws InterruptedException {
		for(ConsumerReceiver receiver: receivers) {
			receiver.interrupt();
			receiver.join();
		}
		
	}

	private static ConsumerReceiver[] startReceivers(BlockingQueue<String> oddMessageBox, BlockingQueue<String> evenMessageBox,
			int nReceivers) {	
		ConsumerReceiver[] receivers = 
		IntStream.range(0, nReceivers).mapToObj(i -> {
			ConsumerReceiver receiver = new ConsumerReceiver();
			receiver.start(); 
			if (receiver.getId() % 2 == 0) {
				receiver.setMessageBox(oddMessageBox);
			} else {
				receiver.setMessageBox(evenMessageBox);
			}

			return receiver;
		}).toArray(ConsumerReceiver[]::new);

		return receivers;
	}

	private static ProducerSender startSender(BlockingQueue<String> oddMessageBox, BlockingQueue<String> evenMessageBox,
			int nMessages) {
		ProducerSender sender = new ProducerSender(oddMessageBox, evenMessageBox,  nMessages);
		sender.start();
		return sender;
	}

}
