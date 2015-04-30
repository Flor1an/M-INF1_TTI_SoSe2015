package roxel;

import com.gigaspaces.annotation.pojo.SpaceId;

/**
 * Roxels are the grid components of the map. They have specific
 * types/functionality and may contain a car.
 */
public class MapElement {
	private Integer id;
	private Integer x;
	private Integer y;
	private Integer currentCarId;
	
	private Boolean road;
	private Boolean junction;
	private Boolean eastAllowed;
	private Boolean southAllowed;

	public MapElement() {
	}

	public MapElement(Integer id, Integer x, Integer y, Boolean road,Boolean junction,Boolean eastAllowed,Boolean southAllowed) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.road = road;
		this.junction = junction;
		this.eastAllowed = eastAllowed;
		this.southAllowed = southAllowed;
	}

	@SpaceId
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}



	public Integer getCurrentCarId() {
		return currentCarId;
	}

	public void setCurrentCarId(Integer currentCarId) {
		this.currentCarId = currentCarId;
	}

	public Boolean isRoad() {
		return road;
	}

	public void setRoad(Boolean road) {
		this.road = road;
	}

	public Boolean isJunction() {
		return junction;
	}

	public void setJunction(Boolean junction) {
		this.junction = junction;
	}

	public Boolean isEastAllowed() {
		return eastAllowed;
	}

	public void setEastAllowed(Boolean eastAllowed) {
		this.eastAllowed = eastAllowed;
	}

	public Boolean isSouthAllowed() {
		return southAllowed;
	}

	public void setSouthAllowed(Boolean southAllowed) {
		this.southAllowed = southAllowed;
	}

	public Boolean hasCar() {
		return currentCarId != null;
	}

	public void removeCar() {
		currentCarId = null;
	}


	@Override
	public String toString() {
		return String.format("MapElement(%s)[%s;%s] Car=%s \t(Road?:%s ; Junction?:%s)  [west?:%s ; south?:%s]", id, x, y, currentCarId, road, junction,eastAllowed,southAllowed);
	}

}
