package py.com.sodep.mobileforms.ui.list;

import py.com.sodep.mf.exchange.objects.metadata.DescribableObject;

public class Item<T extends DescribableObject> {

	private boolean active = true;

	private String inactiveMessage;

	private T object;

	public Item(T o) {
		this.object = o;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getInactiveMessage() {
		return inactiveMessage;
	}

	public void setInactiveMessage(String inactiveMessage) {
		this.inactiveMessage = inactiveMessage;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

}
