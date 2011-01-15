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

import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.MapEvictionListener;
import com.google.common.collect.MapMaker;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.Singleton;

/**
 * A {@link Singleton} based {@link UnitOfWorkScope}.
 *
 * @since 1.3
 * @author Willi Schoenborn
 */
@Beta
final class SingletonUnitOfWorkScope implements UnitOfWorkScope, MapEvictionListener<Object, Boolean> {
    
    private static final Logger LOG = LoggerFactory.getLogger(SingletonUnitOfWorkScope.class);

    private final ConcurrentMap<Object, Boolean> scoped;
    
    private DestroyStrategy strategy = NoopDestroyStrategy.INSTANCE;
    private boolean active;
    
    SingletonUnitOfWorkScope() {
        // weak keys is ok, the singleton scope is keeping the reference
        this.scoped = new MapMaker().weakKeys().evictionListener(this).makeMap();
    }
    
    @Inject(optional = true)
    void setStrategy(DestroyStrategy strategy) {
        this.strategy = Preconditions.checkNotNull(strategy, "Strategy");
    }
    
    @Override
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
        Preconditions.checkNotNull(key, "Key");
        Preconditions.checkNotNull(unscoped, "Unscoped");
        return new Provider<T>() {
            
            private final Provider<T> provider = Scopes.SINGLETON.scope(key, unscoped);
            
            @Override
            public T get() {
                final T instance = provider.get();
                scoped.putIfAbsent(instance, Boolean.TRUE);
                return instance;
            }
            
        };
    }

    @Override
    public void begin() {
        LOG.trace("Entering {}", this);
        active = true;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void end() {
        Preconditions.checkState(isActive(), "No scope block in progress");
        LOG.trace("Exiting {}", this);
        
        final DestroyErrors errors = new DefaultDestroyErrors();
        
        for (Object value : scoped.keySet()) {
            strategy.destroy(value, errors); 
        }
        
        scoped.clear();
        active = false;
        
        errors.throwIfNecessary();
        
        LOG.trace("Successfully exited {}", this);
    }
    
    @Override
    public void onEviction(Object value, Boolean ignored) {
        strategy.destroy(value, ThrowingDestroyErrors.INSTANCE); 
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
    
}
