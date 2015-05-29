import listener.ListenerForGUICarUpdates;

import org.openspaces.core.GigaSpace;
import org.openspaces.events.polling.SimplePollingContainerConfigurer;
import org.openspaces.events.polling.SimplePollingEventListenerContainer;


	public class _startNotificationCarMovementCollector extends Thread {
		GigaSpace gigaSpace;

		public _startNotificationCarMovementCollector(GigaSpace gigaSpace) {
			this.gigaSpace = gigaSpace;
		}

		@Override
		public void run() {
			super.run();
			System.out.println("Polling started for Car Movement Notification");
			try {
				Thread.sleep(5000);

				SimplePollingEventListenerContainer pollingEventListenerContainer = new SimplePollingContainerConfigurer(gigaSpace)
					.eventListenerAnnotation(new ListenerForGUICarUpdates(gigaSpace))
					.pollingContainer();
				pollingEventListenerContainer.start();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

