package modelpad.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class NamedElement implements Destroyable {

	protected NameSet names;
	protected String nameKey;
	protected boolean isDestroyed;

	protected NamedElement(NameSet names, String nameKey) {
		this.names = names;
		this.nameKey = nameKey;
		this.isDestroyed = false;
	}

	protected String getNameByKey(String key) {
		return names.getNameByKey(key);
	}

	public String getName() {
		return getNameByKey(nameKey);
	}

	@Override
	public void destroy() {
		isDestroyed = true;
		names = null;
		nameKey = null;
	}

	@Override
	public boolean isDestroyed() {
		return isDestroyed;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof NamedElement)) {
			return false;
		}
		NamedElement that = (NamedElement) o;
		return new EqualsBuilder().append(this.names, that.names).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(names).hashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(names).append(nameKey).toString();
	}

}
