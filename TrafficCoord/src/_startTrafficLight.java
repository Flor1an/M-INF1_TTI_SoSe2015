import listener.ListenerForCarMovement;
import listener.ListenerForTrafficLight;

import org.openspaces.core.GigaSpace;
import org.openspaces.events.polling.SimplePollingContainerConfigurer;
import org.openspaces.events.polling.SimplePollingEventListenerContainer;

public class _startTrafficLight extends Thread {
	GigaSpace gigaSpace;

	public _startTrafficLight(GigaSpace gigaSpace) {
		this.gigaSpace = gigaSpace;
	}

	@Override
	public void run() {
		super.run();
		System.out.println("Polling started for Traffic Light");
		try {
			Thread.sleep(5000);

			SimplePollingEventListenerContainer pollingEventListenerContainer = new SimplePollingContainerConfigurer(gigaSpace)
				.eventListenerAnnotation(new ListenerForTrafficLight(gigaSpace))
				.pollingContainer();
			pollingEventListenerContainer.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
