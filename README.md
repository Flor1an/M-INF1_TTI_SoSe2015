# TTI_SoSe2015

Technik und Technologie verteilter Informationssysteme (Teil 4)

--
_to start tuple space:_

    gs-agent.bat / gs-agent.sh
	
_to start partitioned space:_

    gs.bat / gs.sh deploy-space -cluster total_members=4,0 space

	
_1. to compile code:_	

    mvn compile 	

_2. Start feeder (generates the world and cars):_

    mvn os:run -Dmodule=feeder

_3. Start Gui (e.g. from eclipse)_

[before carMove, due to some road elements may be missing while they are processes (taken from the space)]

_4. Start carMover_

    mvn os:run -Dmodule=carMover

_5. Start trafficLight_

    mvn os:run -Dmodule=trafficLight
	
	
	
	
###### TODO:
- collect all cars within space (reduces amount of serialization)
- cluster road
- improve startup procedure