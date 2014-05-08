package de.hpi.krestel.mySearchEngine.booleanQueries;

import java.util.List;

public class Or<T> extends BinaryBooleanSetOperation<T> {
	public Or(BooleanSetOperation<T> left, BooleanSetOperation<T> right) {
		super(left, right);
	}

	@Override
	protected List<T> processResult(List<T> left, List<T> right) {
		left.addAll(right);
		return left;
	}	
}
