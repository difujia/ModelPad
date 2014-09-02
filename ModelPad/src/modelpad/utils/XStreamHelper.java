package modelpad.utils;

import modelpad.datamodel.AbstractElement;
import modelpad.datamodel.EAttribute;
import modelpad.datamodel.EClass;
import modelpad.datamodel.EReference;
import modelpad.datamodel.EReferenceInfo;
import modelpad.datamodel.Level;
import modelpad.datamodel.Solution;

import com.thoughtworks.xstream.XStream;

public class XStreamHelper {

	/**
	 * Get a new ready-to-use xstream instance.
	 * @return
	 */
	public static XStream getConfiguredXStream() {
		// changes to this method result in recreate all xml level files!
		
		XStream xstream = new XStream();
		xstream.setMode(XStream.ID_REFERENCES);

		xstream.alias("class", EClass.class);
		xstream.alias("attribute", EAttribute.class);
		xstream.alias("reference", EReference.class);
		xstream.alias("info", EReferenceInfo.class);
		xstream.alias("solution", Solution.class);
		xstream.alias("level", Level.class);

		xstream.addImplicitCollection(Solution.class, "structure", EClass.class);

		xstream.useAttributeFor(String.class);
		xstream.useAttributeFor(boolean.class);

		xstream.omitField(AbstractElement.class, "mObservable");
		xstream.omitField(AbstractElement.class, "mRecycler");

		xstream.aliasSystemAttribute("rid", "reference");	// to avoid confusion
		return xstream;
	}
}
