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

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.inject.Key;
import com.google.inject.OutOfScopeException;
import com.google.inject.Provider;

/**
 * {@link Provider} implementation which handles the scoping of this scope.
 *
 * @since 2.0
 * @author Willi Schoenborn
 * @param <T> generic target type
 */
public final class ScopingProvider<T> implements Provider<T> {
    
    private static final Logger LOG = LoggerFactory.getLogger(ScopingProvider.class);
    
    private final SupplyingScope scope;
    private final Key<T> key;
    private final Provider<T> unscoped;
    
    private Function<? super Key<T>, Object> encoder = new NoopKeyEncoder<T>();

    public ScopingProvider(SupplyingScope scope, Key<T> key, Provider<T> unscoped) {
        this.scope = Preconditions.checkNotNull(scope, "Scope");
        this.key = Preconditions.checkNotNull(key, "Key");
        this.unscoped =  Preconditions.checkNotNull(unscoped, "Unscoped");
    }
    
    public void setEncoder(Function<? super Key<T>, Object> encoder) {
        this.encoder = Preconditions.checkNotNull(encoder, "Encoder");
    }
    
    private void checkInScope(ScopeContext context) {
        if (context == null) {
            throw new OutOfScopeException(
                "Can't access " + key + " outside of a " + scope + " block" 
            );
        }
    }
    
    @Override
    public T get() {
        final ScopeContext context = scope.get();
        
        checkInScope(context);
        
        final Object encoded = encoder.apply(key);
        
        @SuppressWarnings("unchecked")
        final T scoped = (T) context.get(encoded);
        
        // is there a scoped version?
        if (scoped == null && !context.containsKey(encoded)) {
            final T value = unscoped.get();
            context.putIfAbsent(encoded, value);
            LOG.trace("No scoped version for {} found, created {}", key, value);
            return value;
        } else {
            LOG.trace("Found scoped version for {}: {}", key, scoped);
            return scoped;
        }
    }

    @Override
    public String toString() {
        return unscoped + " in " + scope;
    }
    
}
