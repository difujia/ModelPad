package modelpad.model;

public class EReference implements Recycleable {

	private EClass source;
	private EClass target;
	private EReference opposite;
	private boolean containment = false;
	private boolean multiple = false;
	private String name = "";

	EReference(EClass source, EClass target) {
		this.source = source;
		this.target = target;
	}

	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	boolean isContainment() {
		return containment;
	}

	void setContainment(boolean containment) {
		this.containment = containment;
	}

	boolean isMultiple() {
		return multiple;
	}

	void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	EClass getTarget() {
		return target;
	}

	EReference getOpposite() {
		return opposite;
	}

	void setOpposite(EReference opposite) {
		this.opposite = opposite;
	}

	void removeOpposite() {
		opposite = null;
	}

	@Override
	public void recycle() {
		source.removeRef(this);
		if (opposite != null) {
			opposite.removeOpposite();
			opposite = null;
		}
	}

}
