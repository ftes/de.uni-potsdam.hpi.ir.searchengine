package de.hpi.krestel.mySearchEngine.booleanQueries;

import java.util.Set;

public class Or<T> extends BinaryBooleanSetOperation<T> {
	public Or(BooleanSetOperation<T> left, BooleanSetOperation<T> right) {
		super(left, right);
	}

	@Override
	protected Set<T> processResult(Set<T> left, Set<T> right) {
		left.addAll(right);
		return left;
	}	
}
