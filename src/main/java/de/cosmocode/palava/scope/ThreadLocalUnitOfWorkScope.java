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

/**
 * A {@link ThreadLocal} based {@link UnitOfWorkScope} implementation.
 *
 * @author Willi Schoenborn
 */
public final class ThreadLocalUnitOfWorkScope extends AbstractScope<ScopeContext> implements UnitOfWorkScope {

    private static final Logger LOG = LoggerFactory.getLogger(ThreadLocalUnitOfWorkScope.class);
    
    private final ThreadLocal<ScopeContext> context = new ThreadLocal<ScopeContext>();
    
    @Override
    public void begin() {
        Preconditions.checkState(!inProgress(), "Scope already entered");
        LOG.trace("Entering {}", this);
        context.set(new SimpleScopeContext());
    }

    @Override
    public boolean inProgress() {
        return context.get() != null;
    }
    
    @Override
    public void end() {
        Preconditions.checkState(inProgress(), "No scope block in progress");
        LOG.trace("Exiting {}", this);
        context.get().clear();
        context.remove();
    }

    @Override
    public ScopeContext get() {
        return context.get();
    }

}
