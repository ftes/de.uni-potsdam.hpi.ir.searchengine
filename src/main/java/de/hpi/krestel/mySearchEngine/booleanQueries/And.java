package de.hpi.krestel.mySearchEngine.booleanQueries;

import java.util.List;

public class And<T> extends BinaryBooleanSetOperation<T> {
	public And(BooleanSetOperation<T> left, BooleanSetOperation<T> right) {
		super(left, right);
	}

	@Override
	protected List<T> processResult(List<T> left, List<T> right) {
		left.retainAll(right);
		return left;
	}	
}
