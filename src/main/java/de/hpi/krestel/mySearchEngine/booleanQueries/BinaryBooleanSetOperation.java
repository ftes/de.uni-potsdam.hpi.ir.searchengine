package de.hpi.krestel.mySearchEngine.booleanQueries;

import java.io.IOException;
import java.util.List;

import de.hpi.krestel.mySearchEngine.search.QueryProcessingException;
import de.hpi.krestel.mySearchEngine.search.TermLengthException;

public abstract class BinaryBooleanSetOperation<T> implements BooleanSetOperation<T> {
	private final BooleanSetOperation<T> left;
	private final BooleanSetOperation<T> right;
	
	public BinaryBooleanSetOperation(BooleanSetOperation<T> left, BooleanSetOperation<T> right) {
		this.left = left;
		this.right = right;
	}
	
	@Override
	public List<T> execute(int topK) throws IOException, TermLengthException, QueryProcessingException {
		List<T> l = left.execute(topK);
		List<T> r = right.execute(topK);
		return processResult(l, r);
	}
	
	protected abstract List<T> processResult(List<T> left, List<T> right);
	
	@Override
	public void print(int indent, int step) {
		String indentSpace  = new String(new char[indent]).replace('\0', ' ');
		System.out.println(indentSpace + getClass().getSimpleName());
		left.print(indent + step, step);
		right.print(indent + step, step);
	}
}
