package listener;

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
public class ListenerForTrafficLightReSetAll {
	private GigaSpace gigaspace;

	/**
	 * Listener for active traffic lights 
	 */
	public ListenerForTrafficLightReSetAll(GigaSpace gigaspace) {
		this.gigaspace = gigaspace;
	}

	@EventTemplate
	MapElement unprocessedData() {
		MapElement template = new MapElement();
		template.setRoad(true);
		template.setJunction(true);
		return template;
	}

	@SpaceDataEvent
	public MapElement eventListener(MapElement matchedTrafficLight) throws InterruptedException {
		//Displaying "Change in Process" (orange traffic light)
		gigaspace.write(new NotifyGuiAboutTrafficLightChange(matchedTrafficLight.getX(), matchedTrafficLight.getY(), false, false));
		
		//duration of orange traffic light
		Thread.sleep(1000);
		
		//change direction (green/red combo)
		if(matchedTrafficLight.isSouthAllowed()){
			matchedTrafficLight.setSouthAllowed(false);
			matchedTrafficLight.setEastAllowed(true);
		}else if (matchedTrafficLight.isEastAllowed()){
			matchedTrafficLight.setSouthAllowed(true);
			matchedTrafficLight.setEastAllowed(false);
		}else{
			//error handling if traffic light was "off" -> set west to east
			matchedTrafficLight.setSouthAllowed(false);
			matchedTrafficLight.setEastAllowed(true);
		}

		//Displaying new traffic light colours
		gigaspace.write(new NotifyGuiAboutTrafficLightChange(matchedTrafficLight.getX(), matchedTrafficLight.getY(), matchedTrafficLight.isEastAllowed(), matchedTrafficLight.isSouthAllowed()));
		
		return matchedTrafficLight;

	}
}