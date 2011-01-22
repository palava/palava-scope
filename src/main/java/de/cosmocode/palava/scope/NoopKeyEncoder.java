package de.cosmocode.palava.scope;

import com.google.common.base.Function;
import com.google.inject.Key;

/**
 * A noop implementation used by {@link ScopingProvider}.
 * 
 * @author Willi Schoenborn
 * @param <T> generic key type
 */
final class NoopKeyEncoder<T> implements Function<Key<T>, Object> {

	@Override
	public Object apply(Key<T> input) {
		return input;
	}
	
}
