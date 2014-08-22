package modelpad.utils;

import modelpad.metamodel.EAttribute;
import modelpad.metamodel.EClass;
import modelpad.metamodel.EReference;
import modelpad.metamodel.EReferenceInfo;
import modelpad.metamodel.ElementBase;
import modelpad.metamodel.Level;
import modelpad.metamodel.Solution;

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

		xstream.omitField(ElementBase.class, "mObservable");
		xstream.omitField(ElementBase.class, "mRecycler");

		xstream.aliasSystemAttribute("rid", "reference");	// to avoid confusion
		return xstream;
	}
}
