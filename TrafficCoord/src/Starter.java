import org.openspaces.core.GigaSpace;


import GUI.GUI;
import roxel.MapGenerator;
import traffic.Traffic;
import tupleSpace.TupleSpace;

public class Starter {
	// gs-agent.bat gsa.gsc 2 gsa. global.gsm 2 gsa.global.lus 2
	// gs.bat deploy-space -cluster total_members=1,1 myTrafficGrid

	public static void main(String[] args) {
		try {
			
			TupleSpace ts = new TupleSpace("myTrafficGrid");
			GigaSpace gigaSpace = ts.connect();
			ts.cleanTupleSpace();

			MapGenerator mapGen = new MapGenerator(gigaSpace, 45, 20);
			mapGen.generateMap();

			Traffic traffic = new Traffic(gigaSpace, 10);
			new GUI(gigaSpace, traffic);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
