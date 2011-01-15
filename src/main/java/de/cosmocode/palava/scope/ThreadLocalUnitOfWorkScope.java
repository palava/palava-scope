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
import com.google.inject.Inject;

/**
 * A {@link ThreadLocal} based {@link UnitOfWorkScope} implementation.
 *
 * @author Willi Schoenborn
 */
final class ThreadLocalUnitOfWorkScope extends AbstractScope implements UnitOfWorkScope {

    private static final Logger LOG = LoggerFactory.getLogger(ThreadLocalUnitOfWorkScope.class);
    
    private final ThreadLocal<ScopeContext> context = new ThreadLocal<ScopeContext>();
    
    private DestroyStrategy strategy = NoopDestroyStrategy.INSTANCE;
    
    @Inject(optional = true)
    void setStrategy(DestroyStrategy strategy) {
        this.strategy = Preconditions.checkNotNull(strategy, "Strategy");
    }
    
    @Override
    public void begin() {
        Preconditions.checkState(!isActive(), "%s already entered", this);
        LOG.trace("Entering {}", this);
        context.set(new DefaultScopeContext());
    }

    @Override
    public boolean isActive() {
        return context.get() != null;
    }
    
    @Override
    public void end() {
        Preconditions.checkState(isActive(), "No %s block in progress", this);
        LOG.trace("Exiting {}", this);
        
        final DestroyErrors errors = new DefaultDestroyErrors();
        final ScopeContext currentContext = context.get();
        
        for (Object value : currentContext.values()) {
            strategy.destroy(value, errors);
        }
        
        currentContext.clear();
        context.remove();
        
        errors.throwIfNecessary();
        LOG.trace("Successfully exited {}", this);
    }

    @Override
    protected ScopeContext getContext() {
        return context.get();
    }

}
