package de.hpi.krestel.mySearchEngine.booleanQueries;

import java.util.Set;

public class And<T> extends BinaryBooleanSetOperation<T> {
	public And(BooleanSetOperation<T> left, BooleanSetOperation<T> right) {
		super(left, right);
	}

	@Override
	protected Set<T> processResult(Set<T> left, Set<T> right) {
		left.retainAll(right);
		return left;
	}	
}
