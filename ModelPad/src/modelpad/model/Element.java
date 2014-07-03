package modelpad.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class Element implements Recycleable {

	protected String name;
	private boolean consumed = false;

	protected Element(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isConsumed() {
		return consumed;
	}

	public void consume() {
		consumed = true;
	}

	@Override
	public void recycle() {
		consumed = false;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Element)) {
			return false;
		}
		Element that = (Element) o;
		return new EqualsBuilder().append(this.name, that.name).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name).hashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(name).toString();
	}

}
