package de.hpi.krestel.mySearchEngine.booleanQueries;

import java.util.Set;

public class ButNot<T> extends BinaryBooleanSetOperation<T> {
	public ButNot(BooleanSetOperation<T> left, BooleanSetOperation<T> right) {
		super(left, right);
	}

	@Override
	protected Set<T> processResult(Set<T> left, Set<T> right) {
		left.removeAll(right);
		return left;
	}	
}
