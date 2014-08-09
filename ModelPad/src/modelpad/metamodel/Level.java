package modelpad.metamodel;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import com.google.common.collect.Sets;

public class Level implements Serializable {

	private static final long serialVersionUID = -659819209903272852L;

	private String title;
	private String subtitle;
	private String question;

	private Solution expectedSolution;
	private Set<EClass> surplusClasses = Sets.newHashSet();
	private Set<EAttribute> surplusAttrs = Sets.newHashSet();
	private Set<EReferenceInfo> surplusRefInfos = Sets.newHashSet();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Set<ElementBase> getAllElements() {
		Set<ElementBase> elements = Sets.newHashSet();
		for (EClass c : expectedSolution.getStructuredClasses()) {
			elements.add(c);
			elements.addAll(c.getAttributes());
			for (EReference ref : c.getReferences()) {
				elements.add(ref.getInfo());
			}
		}

		elements.addAll(surplusClasses);
		elements.addAll(surplusAttrs);
		elements.addAll(surplusRefInfos);

		// restore to initial state
		for (ElementBase e : elements) {
			e.dispose();
		}

		return elements;
	}

	public Solution getExpectedSolution() {
		return expectedSolution;
	}

	public void setExpectedSolution(Solution standardSolution) {
		this.expectedSolution = standardSolution;
	}

	public Set<EClass> getSurplusClasses() {
		return Collections.unmodifiableSet(surplusClasses);
	}

	public void addSurplusClasses(EClass... classes) {
		Collections.addAll(surplusClasses, classes);
	}

	public Set<EAttribute> getSurplusAttrs() {
		return Collections.unmodifiableSet(surplusAttrs);
	}

	public void addSurplusAttrs(EAttribute... attrs) {
		Collections.addAll(surplusAttrs, attrs);
	}

	public Set<EReferenceInfo> getSurplusRefInfos() {
		return Collections.unmodifiableSet(surplusRefInfos);
	}

	public void addSurplusRefInfos(EReferenceInfo... refInfos) {
		Collections.addAll(surplusRefInfos, refInfos);
	}

}
