package de.hpi.krestel.mySearchEngine.booleanQueries;

import java.io.IOException;
import java.util.Set;

import de.hpi.krestel.mySearchEngine.QueryProcessingException;
import de.hpi.krestel.mySearchEngine.TermLengthException;

public abstract class BinaryBooleanSetOperation<T> implements BooleanSetOperation<T> {
	private final BooleanSetOperation<T> left;
	private final BooleanSetOperation<T> right;
	
	public BinaryBooleanSetOperation(BooleanSetOperation<T> left, BooleanSetOperation<T> right) {
		this.left = left;
		this.right = right;
	}
	
	@Override
	public Set<T> execute() throws IOException, TermLengthException, QueryProcessingException {
		Set<T> l = left.execute();
		Set<T> r = right.execute();
		return processResult(l, r);
	}
	
	protected abstract Set<T> processResult(Set<T> left, Set<T> right);
	
	@Override
	public void print(int indent, int step) {
		String indentSpace  = new String(new char[indent]).replace('\0', ' ');
		System.out.println(indentSpace + getClass().getSimpleName());
		left.print(indent + step, step);
		right.print(indent + step, step);
	}
}
