package modelpad.metamodel;

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

	private Solution userSolution;
	private Level level;

	/*
	 * Results collected. These collections contains objects in player's model
	 */
	private Set<EClass> matchedClasses;
	private Set<EAttribute> matchedAttrs;
	private Set<EReference> matchedRefs;

	private Set<EClass> unexpectedClasses;
	private Set<EAttribute> unexpectedAttrs;
	private Set<EReferenceInfo> unexpectedRefInfos;

	private Set<EClass> misplacedClasses;
	private Set<EAttribute> misplacedAttrs;
	private Set<EReferenceInfo> misplacedRefInfos;

	/*
	 * not sure to provide this, because they are not the objects in player's model, which maybe confusing
	 */
	private Set<EClass> missingClasses;
	private Set<EAttribute> missingAttrs;
	private Set<EReferenceInfo> missingRefInfos;

	private Set<ElementBase> allExpected;

	public Validator(Solution userSolution, Level level) {
		this.userSolution = userSolution;
		this.level = level;
		validate();
	}

	private void validate() {

		Set<EClass> userClasses = Sets.newHashSet();
		Set<EAttribute> userAttrs = Sets.newHashSet();
		Set<EReference> userRefs = Sets.newHashSet();
		unbox(userSolution, userClasses, userAttrs, userRefs);
		Set<EReferenceInfo> userRefInfos = FluentIterable.from(userRefs).transform(toRefInfo).toSet();

		Set<EClass> expClasses = Sets.newHashSet();
		Set<EAttribute> expAttrs = Sets.newHashSet();
		Set<EReference> expRefs = Sets.newHashSet();
		unbox(level.getExpectedSolution(), expClasses, expAttrs, expRefs);

		allExpected = ImmutableSet.copyOf(Iterables.concat(expClasses, expAttrs, expRefs));

		matchedClasses = FluentIterable.from(userClasses).filter(matchAnyIn(expClasses)).toSet();
		matchedAttrs = FluentIterable.from(userAttrs).filter(matchAnyIn(expAttrs)).toSet();
		matchedRefs = FluentIterable.from(userRefs).filter(matchAnyIn(expRefs)).toSet();
		Set<EReferenceInfo> matchedRefInfos = FluentIterable.from(matchedRefs).transform(toRefInfo).toSet();

		unexpectedClasses = FluentIterable.from(userClasses).filter(lookslikeAnyIn(level.getSurplusClasses())).toSet();
		unexpectedAttrs = FluentIterable.from(userAttrs).filter(lookslikeAnyIn(level.getSurplusAttrs())).toSet();
		unexpectedRefInfos = FluentIterable.from(userRefInfos).filter(lookslikeAnyIn(level.getSurplusRefInfos()))
				.toSet();

		misplacedClasses = FluentIterable.from(userClasses).filter(notIn(matchedClasses))
				.filter(notIn(unexpectedClasses)).toSet();
		misplacedAttrs = FluentIterable.from(userAttrs).filter(notIn(matchedAttrs)).filter(notIn(unexpectedAttrs))
				.toSet();
		misplacedRefInfos = FluentIterable.from(userRefInfos).filter(notPlaceHolder).filter(notIn(matchedRefInfos))
				.filter(notIn(unexpectedRefInfos)).toSet();

	}

	private <T> Predicate<T> notIn(final Collection<T> c) {
		return new Predicate<T>() {
			@Override
			public boolean apply(T arg) {
				return !c.contains(arg);
			}
		};
	}

	private <T extends ElementBase> Predicate<T> matchAnyIn(final Collection<T> c) {
		return new Predicate<T>() {
			@Override
			public boolean apply(T arg) {
				for (T element : c) {
					if (arg.match(element))
						return true;
				}
				return false;
			}
		};
	}

	private <T extends ElementBase> Predicate<T> lookslikeAnyIn(final Collection<T> c) {
		return new Predicate<T>() {

			@Override
			public boolean apply(T arg) {
				for (T element : c) {
					if (arg.lookslike(element))
						return true;
				}
				return false;
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

	public Set<ElementBase> getAllExpected() {
		return allExpected;
	}

	public Set<ElementBase> getAllMatched() {
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

	public Set<ElementBase> getAllUnexpected() {
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

	public Set<EClass> getMisplacedClasses() {
		return misplacedClasses;
	}

	public Set<EAttribute> getMisplacedAttrs() {
		return misplacedAttrs;
	}

	public Set<EReferenceInfo> getMisplacedRefInfos() {
		return misplacedRefInfos;
	}

	public Set<ElementBase> getAllMisplaced() {
		return ImmutableSet.copyOf(Iterables.concat(misplacedClasses, misplacedAttrs, misplacedRefInfos));
	}
}
