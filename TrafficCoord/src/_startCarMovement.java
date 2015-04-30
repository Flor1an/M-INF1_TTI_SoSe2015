import listener.ListenerForCarMovement;

import org.openspaces.core.GigaSpace;
import org.openspaces.events.polling.SimplePollingContainerConfigurer;
import org.openspaces.events.polling.SimplePollingEventListenerContainer;

public class _startCarMovement extends Thread {
	GigaSpace gigaSpace;
	int carId;

	public _startCarMovement(GigaSpace gigaSpace, int carId) {
		this.gigaSpace = gigaSpace;
		this.carId = carId;
	}

	@Override
	public void run() {
		super.run();
		System.out.println("Polling started for Car: " + carId);
		try {
			Thread.sleep(5000);

			SimplePollingEventListenerContainer pollingEventListenerContainer = new SimplePollingContainerConfigurer(gigaSpace)
				.eventListenerAnnotation(new ListenerForCarMovement(gigaSpace, carId))
				.pollingContainer();
			pollingEventListenerContainer.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
