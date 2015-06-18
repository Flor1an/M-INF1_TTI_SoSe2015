package haw.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.gigaspaces.annotation.pojo.SpaceId;

public class CarPositions {
	private Integer id;
	private HashMap<Integer, Integer[]> list = new HashMap<Integer, Integer[]>();
	private String note = "";

	public CarPositions() {

	}

	public CarPositions(Integer id) {
		this.id = id;
		this.list = new HashMap<Integer, Integer[]>();
		this.note = "testentry";
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {

		String txt = "";

		Iterator it = list.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			txt += "\n{" + pair.getKey() + " = [" + ((Integer[]) pair.getValue())[0] + ";" + ((Integer[]) pair.getValue())[1] + "]}";
			it.remove(); // avoids a ConcurrentModificationException
		}
		return "CarPositions(" + id + ") [list=" + txt + "] " + note;
	}

	public void setList(HashMap<Integer, Integer[]> list) {
		this.list = list;
	}

}
