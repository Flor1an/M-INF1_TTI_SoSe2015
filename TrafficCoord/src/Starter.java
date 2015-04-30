import org.openspaces.core.GigaSpace;

import GUI.GUI;
import roxel.MapGenerator;
import traffic.Traffic;
import tupleSpace.TupleSpace;

public class Starter {
	// gs-agent.bat
	// gs.bat deploy-space -cluster total_members=1,0 myTrafficGrid

	static int amoutOfCars = 10;

	public static void main(String[] args) {
		try {

			TupleSpace ts = new TupleSpace("myTrafficGrid");
			GigaSpace gigaSpace = ts.connect();
			ts.cleanTupleSpace();

			MapGenerator mapGen = new MapGenerator(gigaSpace, 19, 9);

			mapGen.generateMap();
			Thread.sleep(1000);

			new Traffic(gigaSpace, amoutOfCars);

			for (int i = 0; i < amoutOfCars; i++) {
				new _startCarMovement(gigaSpace, i).start();
			}
			new _startTrafficLight(gigaSpace).start();
			new _startTrafficLightIntervallChange(gigaSpace).start();
			
			
			new GUI(gigaSpace);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
