package listener;

import java.util.Random;

import org.openspaces.core.GigaSpace;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.notify.NotifyType;
import org.openspaces.events.polling.Polling;

import roxel.MapElement;
import updates.NotifyGuiAboutTrafficLightChange;

@EventDriven
@Polling
@NotifyType(write = true, update = true)
public class ListenerForTrafficLight {
	private GigaSpace gigaspace;

	/**
	 * Listen for unsetted traffic lights
	 */
	public ListenerForTrafficLight(GigaSpace gigaspace) {
		this.gigaspace = gigaspace;
	}

	@EventTemplate
	MapElement unprocessedData() {
		MapElement template = new MapElement();
		template.setRoad(true);
		template.setJunction(true);
		template.setSouthAllowed(false);
		template.setEastAllowed(false);
		return template;
	}

	@SpaceDataEvent
	public MapElement eventListener(MapElement event) throws InterruptedException {

		//Thread.sleep(1000);

		if (new Random().nextInt(2) == 0) {
			event.setSouthAllowed(true);
			event.setEastAllowed(false);
		} else {
			event.setEastAllowed(true);
			event.setSouthAllowed(false);
		}
		System.out.println("reseting traffic light: " + event.toString()+"\n");

		NotifyGuiAboutTrafficLightChange utl = new NotifyGuiAboutTrafficLightChange(event.getX(), event.getY(), event.isEastAllowed(), event.isSouthAllowed());
		gigaspace.write(utl);
		
		return event;

	}
}