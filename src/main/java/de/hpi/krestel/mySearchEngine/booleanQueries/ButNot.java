package de.hpi.krestel.mySearchEngine.booleanQueries;

import java.util.List;

public class ButNot<T> extends BinaryBooleanSetOperation<T> {
	public ButNot(BooleanSetOperation<T> left, BooleanSetOperation<T> right) {
		super(left, right);
	}

	@Override
	protected List<T> processResult(List<T> left, List<T> right) {
		left.removeAll(right);
		return left;
	}	
}
