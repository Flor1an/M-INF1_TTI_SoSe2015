package updates;

import java.util.ArrayList;
import java.util.List;


public class SingleNotificationAboutEntireCarMovement {
	List<NotifyGuiAboutCarMovement> lst;
	
	public SingleNotificationAboutEntireCarMovement() {
		lst = new ArrayList<NotifyGuiAboutCarMovement>();
	}


	public List<NotifyGuiAboutCarMovement> getLst() {
		return lst;
	}

	public void addNotification(NotifyGuiAboutCarMovement item){
		lst.add(item);
	}
	
	public void setLst(List<NotifyGuiAboutCarMovement> lst) {
		this.lst = lst;
	}


	@Override
	public String toString() {
		return "SingleNotificationAboutEntireCarMovement [lst=" + lst + "]";
	}
	
	
}
