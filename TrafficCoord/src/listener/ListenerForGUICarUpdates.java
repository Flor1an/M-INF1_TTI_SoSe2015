package listener;

import java.util.Random;

import org.openspaces.core.GigaSpace;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.notify.NotifyType;
import org.openspaces.events.polling.Polling;

import updates.NotifyGuiAboutCarMovement;
import updates.SingleNotificationAboutEntireCarMovement;

@EventDriven
@Polling
@NotifyType(write = true, update = true)
public class ListenerForGUICarUpdates {
	private GigaSpace gigaspace;

	public ListenerForGUICarUpdates(GigaSpace gigaspace) {
		this.gigaspace = gigaspace;
	}

	@EventTemplate
	NotifyGuiAboutCarMovement unprocessedData() {
		NotifyGuiAboutCarMovement template = new NotifyGuiAboutCarMovement();

		return template;
	}

	@SpaceDataEvent
	public void eventListener(NotifyGuiAboutCarMovement event) throws InterruptedException {
Thread.sleep(200);
		SingleNotificationAboutEntireCarMovement combined;
		combined = gigaspace.take(new SingleNotificationAboutEntireCarMovement());

		if (combined == null) {
			System.err.println("new");
			combined = new SingleNotificationAboutEntireCarMovement();
		}


		combined.addNotification(event);
		System.err.println(combined.getLst().size()+ "\t " + combined.getLst().toString());
		//gigaspace.write(combined);

	}
}
