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

import com.google.common.base.Preconditions;
import com.google.inject.Key;
import com.google.inject.OutOfScopeException;
import com.google.inject.Provider;
import com.google.inject.Scope;

/**
 * Abstract skeleton implementation of the {@link Scope} interface.
 *
 * @author Willi Schoenborn
 */
public abstract class AbstractScope implements Scope {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractScope.class);
    
    @Override
    public final <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
        return new ScopedProvider<T>(key, unscoped);
    }

    /**
     * {@link Provider} implementation which handles the scoping of this scope.
     *
     * @since 1.3
     * @author Willi Schoenborn
     * @param <T> generic target type
     */
    private final class ScopedProvider<T> implements Provider<T> {
        
        private final String key;
        private final Provider<T> unscoped;

        private ScopedProvider(Key<T> key, Provider<T> unscoped) {
            this.key = Preconditions.checkNotNull(key, "Key").toString();
            this.unscoped =  Preconditions.checkNotNull(unscoped, "Unscoped");
        }
        
        @Override
        public T get() {
            LOG.trace("Intercepting scoped request with {} to {}", key, unscoped);
            final ScopeContext context = getContext();
            
            if (context == null) {
                throw new OutOfScopeException(
                    "Can't access " + key + " outside of a " + AbstractScope.this + " block" 
                );
            }
            
            @SuppressWarnings("unchecked")
            final T scoped = (T) context.get(key);
            
            // is there a scoped version?
            if (scoped == null && !context.containsKey(key)) {
                final T value = unscoped.get();
                context.putIfAbsent(key, value);
                LOG.trace("No scoped version for {} found, created {}", key, value);
                return value;
            } else {
                LOG.trace("Found scoped version for {}: {}", key, scoped);
                return scoped;
            }
        }

        @Override
        public String toString() {
            return unscoped + " in " + AbstractScope.this;
        }
    }
    
    /**
     * Provides the underlying {@link ScopeContext} of this scope.
     *
     * @since 1.3
     * @return the scoping context
     */
    protected abstract ScopeContext getContext();
    
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
