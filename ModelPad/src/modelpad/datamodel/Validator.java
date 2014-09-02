package modelpad.datamodel;

import java.util.Collection;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

public class Validator {

	/**
	 * Function that takes a EReference as input and output its EReferenceInfo.
	 */
	private static final Function<EReference, EReferenceInfo> toRefInfo = new Function<EReference, EReferenceInfo>() {
		@Override
		public EReferenceInfo apply(EReference arg) {
			return arg.getInfo();
		}
	};

	private static final Predicate<EReferenceInfo> notPlaceHolder = new Predicate<EReferenceInfo>() {

		@Override
		public boolean apply(EReferenceInfo arg) {
			return arg.getName().length() > 0;
		}
	};

	private Solution playerSolution;
	private Solution expectedSolution;

	/*
	 * Results collected. These collections contains objects in player's model
	 */
	private Set<EClass> matchedClasses;
	private Set<EAttribute> matchedAttrs;
	private Set<EReference> matchedRefs;

	private Set<EClass> unexpectedClasses;
	private Set<EAttribute> unexpectedAttrs;
	private Set<EReferenceInfo> unexpectedRefInfos;

	/*
	 * Classes cannot be "misplaced"
	 */
	private Set<EAttribute> misplacedAttrs;
	private Set<EReferenceInfo> misplacedRefInfos;

	/*
	 * objects of missing elements are in the expected solutions
	 */
	private Set<EClass> missingClasses;
	private Set<EAttribute> missingAttrs;
	private Set<EReferenceInfo> missingRefInfos;

	private Set<AbstractElement> allExpected;

	public Validator(Solution playerSolution, Solution expected) {
		this.playerSolution = playerSolution;
		this.expectedSolution = expected;
		validate();
	}

	public void validate() {

		Set<EClass> userClasses = Sets.newHashSet();
		Set<EAttribute> userAttrs = Sets.newHashSet();
		Set<EReference> userRefs = Sets.newHashSet();
		unbox(playerSolution, userClasses, userAttrs, userRefs);
		Set<EReferenceInfo> userRefInfos = FluentIterable.from(userRefs).transform(toRefInfo).toSet();

		Set<EClass> expClasses = Sets.newHashSet();
		Set<EAttribute> expAttrs = Sets.newHashSet();
		Set<EReference> expRefs = Sets.newHashSet();
		unbox(expectedSolution, expClasses, expAttrs, expRefs);
		Set<EReferenceInfo> expRefInfos = FluentIterable.from(expRefs).transform(toRefInfo).toSet();

		allExpected = ImmutableSet.copyOf(Iterables.concat(expClasses, expAttrs, expRefs));

		matchedClasses = FluentIterable.from(userClasses).filter(matchAnyIn(expClasses)).toSet();
		matchedAttrs = FluentIterable.from(userAttrs).filter(matchAnyIn(expAttrs)).toSet();
		matchedRefs = FluentIterable.from(userRefs).filter(matchAnyIn(expRefs)).toSet();
		Set<EReferenceInfo> matchedRefInfos = FluentIterable.from(matchedRefs).transform(toRefInfo).toSet();

		unexpectedClasses = FluentIterable.from(userClasses).filter(notLookslikeAnyIn(expClasses)).toSet();
		unexpectedAttrs = FluentIterable.from(userAttrs).filter(notLookslikeAnyIn(expAttrs)).toSet();
		unexpectedRefInfos = FluentIterable.from(userRefInfos).filter(notPlaceHolder).filter(notLookslikeAnyIn(expRefInfos)).toSet();

		misplacedAttrs = FluentIterable.from(userAttrs).filter(notIn(matchedAttrs)).filter(notIn(unexpectedAttrs))
				.toSet();
		misplacedRefInfos = FluentIterable.from(userRefInfos).filter(notPlaceHolder).filter(notIn(matchedRefInfos))
				.filter(notIn(unexpectedRefInfos)).toSet();

		missingClasses = FluentIterable.from(expClasses).filter(notLookslikeAnyIn(userClasses)).toSet();
		missingAttrs = FluentIterable.from(expAttrs).filter(notLookslikeAnyIn(userAttrs)).toSet();
		missingRefInfos = FluentIterable.from(expRefInfos).filter(notLookslikeAnyIn(userRefInfos)).toSet();
	}

	private <T> Predicate<T> notIn(final Collection<T> c) {
		return new Predicate<T>() {
			@Override
			public boolean apply(T arg) {
				return !c.contains(arg);
			}
		};
	}

	private <T extends AbstractElement> Predicate<T> matchAnyIn(final Collection<T> c) {
		return new Predicate<T>() {
			@Override
			public boolean apply(T arg) {
				for (T element : c) {
					if (arg.match(element)) return true;
				}
				return false;
			}
		};
	}

	private <T extends AbstractElement> Predicate<T> lookslikeAnyIn(final Collection<T> c) {
		return new Predicate<T>() {

			@Override
			public boolean apply(T arg) {
				for (T element : c) {
					if (arg.lookslike(element)) return true;
				}
				return false;
			}
		};
	}

	private <T extends AbstractElement> Predicate<T> notLookslikeAnyIn(final Collection<T> c) {
		return new Predicate<T>() {

			@Override
			public boolean apply(T arg) {
				for (T element : c) {
					if (arg.lookslike(element)) return false;
				}
				return true;
			}
		};
	}

	private void unbox(Solution structure, Collection<EClass> allClasses, Collection<EAttribute> allAttrs,
			Collection<EReference> allRefs) {
		for (EClass c : structure.getStructuredClasses()) {
			allClasses.add(c);
			allAttrs.addAll(c.getAttributes());
			allRefs.addAll(c.getReferences());
		}
	}

	public Set<AbstractElement> getAllExpected() {
		return allExpected;
	}

	public Set<AbstractElement> getAllMatched() {
		return ImmutableSet.copyOf(Iterables.concat(matchedClasses, matchedAttrs, matchedRefs));
	}

	public Set<EClass> getMatchedClasses() {
		return matchedClasses;
	}

	public Set<EAttribute> getMatchedAttrs() {
		return matchedAttrs;
	}

	public Set<EReference> getMatchedRefs() {
		return matchedRefs;
	}

	public Set<AbstractElement> getAllUnexpected() {
		return ImmutableSet.copyOf(Iterables.concat(unexpectedClasses, unexpectedAttrs, unexpectedRefInfos));
	}

	public Set<EClass> getUnexpectedClasses() {
		return unexpectedClasses;
	}

	public Set<EAttribute> getUnexpectedAttrs() {
		return unexpectedAttrs;
	}

	public Set<EReferenceInfo> getUnexpectedRefInfos() {
		return unexpectedRefInfos;
	}

	public Set<EAttribute> getMisplacedAttrs() {
		return misplacedAttrs;
	}

	public Set<EReferenceInfo> getMisplacedRefInfos() {
		return misplacedRefInfos;
	}

	public Set<AbstractElement> getAllMisplaced() {
		return ImmutableSet.copyOf(Iterables.concat(misplacedAttrs, misplacedRefInfos));
	}

	public Set<EClass> getMissingClasses() {
		return missingClasses;
	}

	public Set<EAttribute> getMissingAttrs() {
		return missingAttrs;
	}

	public Set<EReferenceInfo> getMissingRefInfos() {
		return missingRefInfos;
	}

	public Set<AbstractElement> getAllMissing() {
		return ImmutableSet.copyOf(Iterables.concat(missingClasses, missingAttrs, missingRefInfos));
	}
}
