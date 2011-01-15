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

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.MapEvictionListener;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Sets;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.Singleton;

/**
 * A {@link Singleton} based {@link UnitOfWorkScope}.
 *
 * @since 2.0
 * @author Willi Schoenborn
 */
@Beta
final class SingletonUnitOfWorkScope extends AbstractUnitOfWorkScope 
    implements MapEvictionListener<Object, Boolean> {
    
    private static final Logger LOG = LoggerFactory.getLogger(SingletonUnitOfWorkScope.class);

    private final Set<Object> context;
    private boolean active;
    
    SingletonUnitOfWorkScope() {
        // weak keys is ok, the singleton scope is keeping the reference
        final ConcurrentMap<Object, Boolean> map = new MapMaker().weakKeys().evictionListener(this).makeMap();
        this.context = Sets.newSetFromMap(map);
    }

    @Override
    public void begin() {
        checkNotActive();
        LOG.trace("Entering {}", this);
        active = true;
    }

    @Override
    public boolean isActive() {
        return active;
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
                context.add(instance);
                return instance;
            }
            
        };
    }
    
    @Override
    public void onEviction(Object value, Boolean ignored) {
        destroy(value, ThrowingDestroyErrors.INSTANCE);
    }

    @Override
    public void end() {
        checkActive();
        LOG.trace("Exiting {}", this);

        try {
            destroy(context);
        } finally {
            context.clear();
            active = false;
        }
        
        LOG.trace("Successfully exited {}", this);
    }
    
}
