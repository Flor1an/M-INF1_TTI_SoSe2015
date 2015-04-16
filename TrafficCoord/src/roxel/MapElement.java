package roxel;

import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceRouting;

/**
 * Roxels are the grid components of the map. They have specific
 * types/functionality and may contain a car.
 */
public class MapElement {

	public enum Type {
		ROAD, NOROAD;
	}

	public enum Arrow {
		NONE, // used for "non road" elements

		// directions:
		WEST, 
		SOUTH, 
		@Deprecated
		EAST, 
		@Deprecated
		NORTH,

		// 2 exit junctions:
		TWO_WAY_JUNCTION_TO_WEST_OR_TO_SOUTH; 
		

	

		public boolean isRoad() {
			if (isStraight() || isTwoWayJunction() || isThreeWayJunction() || isFourWayJunction()) {
				return true;
			} else {
				return false;
			}
		}

		public boolean isStraight() {
			switch (this) {
			case WEST:
				return true;
			case SOUTH:
				return true;
			
			default:
				return false;
			}
		}
		
		public boolean isJunction(){
			if(isTwoWayJunction()||isThreeWayJunction()||isFourWayJunction()){
				return true;
			}else{
				return false;
			}
		}

		public boolean isTwoWayJunction() {
			switch (this) {
			case TWO_WAY_JUNCTION_TO_WEST_OR_TO_SOUTH:
				return true;
			
			default:
				return false;
			}
		}

		public boolean isThreeWayJunction() {
			switch (this) {
			
			default:
				return false;
			}
		}

		public boolean isFourWayJunction() {
			switch (this) {
			
			default:
				return false;
			}
		}
	}

	private Integer id;
	private Integer x;
	private Integer y;
	private Integer currentCarId;
	private Type type;
	private Arrow arrow;
	private Integer spaceId;


	public MapElement() {
	}

	public MapElement(Integer id, Integer x, Integer y, Type type, Arrow arrow) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.type = type;
		this.arrow = arrow;
		spaceId = y % 2;
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Arrow getArrow() {
		return arrow;
	}

	public void setArrow(Arrow arrow) {
		this.arrow = arrow;
	}

	public Integer getCurrentCarId() {
		return currentCarId;
	}

	public void setCurrentCarId(Integer currentCarId) {
		this.currentCarId = currentCarId;
	}

	public Boolean hasCar() {
		return currentCarId != null;
	}

	public void removeCar() {
		currentCarId = null;
	}

	@SpaceRouting
	Integer getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Integer spaceId) {
		this.spaceId = spaceId;
	}

	@Override
	public String toString() {
		if (type != null) {
			return String.format("MapElement(%s)[%s;%s](%s) Car=%s", id, x, y, type.toString(), currentCarId);
		} else {
			return String.format("MapElement(%s)[%s;%s](null) Car=%s", id, x, y, currentCarId);
		}
	}

}
