package haw.common;

import java.util.HashMap;

import com.gigaspaces.annotation.pojo.SpaceId;

public class CarPositions {
	private Integer id;
	private HashMap<Integer, Integer[]> list;

	public CarPositions() {

		list = new HashMap<Integer, Integer[]>();
	}
	
	public CarPositions(Integer id) {
	this.id=id;
	list = new HashMap<Integer, Integer[]>();
	}


	@SpaceId
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public HashMap<Integer, Integer[]> getList() {
		return list;
	}

	public void addToList(Integer CarId, Integer PositionX, Integer PositionY) {
		Integer[] position = new Integer[2];
		position[0] = PositionX;
		position[1] = PositionY;
		this.list.put(CarId, position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CarPositions("+id+") [list=" + list + "]";
	}

	public void setList(HashMap<Integer, Integer[]> list) {
		this.list = list;
	}

}
