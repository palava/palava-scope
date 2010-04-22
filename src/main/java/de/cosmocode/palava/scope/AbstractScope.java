/**
 * Copyright 2010 CosmoCode GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.cosmocode.palava.scope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Key;
import com.google.inject.OutOfScopeException;
import com.google.inject.Provider;
import com.google.inject.Scope;

/**
 * Abstract skeleton implementation of the {@link Scope} interface.
 *
 * @author Willi Schoenborn
 * @param <S> the generic scope context type
 */
public abstract class AbstractScope<S extends ScopeContext> implements Scope, Provider<S> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractScope.class);
    
    @Override
    public final <T> Provider<T> scope(final Key<T> key, final Provider<T> provider) {
        return new Provider<T>() {

            @Override
            public T get() {
                LOG.trace("Intercepting scoped request with {} to {}", key, provider);
                final ScopeContext context = AbstractScope.this.get();
                if (context == null) {
                    throw new OutOfScopeException(String.format("Can't access %s outside of a %s block", 
                        key, AbstractScope.this
                    ));
                }
                final T cached = context.<Key<T>, T>get(key);
                // is there a cached version?
                if (cached == null && !context.contains(key)) {
                    final T unscoped = provider.get();
                    context.set(key, unscoped);
                    LOG.trace("No cached version for {} found, created {}", key, unscoped);
                    return unscoped;
                } else {
                    LOG.trace("Found cached version for {}: {}", key, cached);
                    return cached;
                }
            }
            
            @Override
            public String toString() {
                return String.format("%s[%s]", provider, AbstractScope.this);
            }

        };
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
