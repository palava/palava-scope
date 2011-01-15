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
import com.google.inject.Provider;

/**
 * A {@link ThreadLocal} based {@link UnitOfWorkScope} implementation.
 *
 * @author Willi Schoenborn
 */
final class ThreadLocalUnitOfWorkScope extends AbstractUnitOfWorkScope implements SupplyingScope {

    private static final Logger LOG = LoggerFactory.getLogger(ThreadLocalUnitOfWorkScope.class);
    
    private final ThreadLocal<ScopeContext> context = new ThreadLocal<ScopeContext>();
    
    @Override
    public void begin() {
        checkNotActive();
        LOG.trace("Entering {}", this);
        context.set(new DefaultScopeContext());
        LOG.trace("Entered {}", this);
    }

    @Override
    public boolean isActive() {
        return context.get() != null;
    }
    
    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
        return new ScopingProvider<T>(this, key, unscoped);
    }

    @Override
    public ScopeContext get() {
        return context.get();
    }
    
    @Override
    public void end() {
        checkActive();
        LOG.trace("Exiting {}", this);
        
        final ScopeContext currentContext = context.get();
        
        try {
            destroy(currentContext);
        } finally {
            currentContext.clear();
            context.remove();
        }
        
        LOG.trace("Successfully exited {}", this);
    }

}
