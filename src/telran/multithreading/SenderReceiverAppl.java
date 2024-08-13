package telran.multithreading;

import java.util.Arrays;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class SenderReceiverAppl {
//TODO for HW #44 (ConsumerReceiver should not be updated)
	//Provide functionality of dispatching
	//Even messages must be processed by receiver threads with even id
	//Odd messages must be processed by receiver threads with odd id
	//Hints two message boxes: one for even messages and other for odd messages
	
	
	private static final int N_MESSAGES = 2000;
	private static final int N_RECEIVERS = 10;

	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<String> messageBoxEven = new LinkedBlockingQueue<String>();
		BlockingQueue<String> messageBoxOdd = new LinkedBlockingQueue<String>();
		ProducerSender sender = startSender(messageBoxEven, messageBoxOdd, N_MESSAGES);
		ConsumerReceiver[] receivers = startReceivers(messageBoxEven, messageBoxOdd, N_RECEIVERS);
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

	private static ConsumerReceiver[] startReceivers(BlockingQueue<String> messageBoxEven,
			BlockingQueue<String> messageBoxOdd, int nReceivers) {
		ConsumerReceiver[] receivers = 
		IntStream.range(0, nReceivers).mapToObj(i -> {
			ConsumerReceiver receiver = new ConsumerReceiver();
			receiver.setMessageBox(receiver.getId() % 2 == 0 ? messageBoxEven : messageBoxOdd);
			return receiver;
		}).toArray(ConsumerReceiver[]::new);
		Arrays.stream(receivers).forEach(ConsumerReceiver::start);
		return receivers;
	}

	private static ProducerSender startSender(BlockingQueue<String> messageBoxEven,
			BlockingQueue<String> messageBoxOdd, int nMessages) {
		ProducerSender sender = new ProducerSender(messageBoxEven, messageBoxOdd, nMessages);
		sender.start();
		return sender;
	}

}
