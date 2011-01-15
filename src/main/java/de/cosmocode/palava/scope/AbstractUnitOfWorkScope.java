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

import com.google.common.base.Preconditions;

/**
 * Abstract {@link UnitOfWorkScope} implementation.
 *
 * @since 2.0
 * @author Willi Schoenborn
 */
public abstract class AbstractUnitOfWorkScope extends DestroyingScope implements UnitOfWorkScope {
    
    protected final boolean isNotActive() {
        return !isActive();
    }
    
    /**
     * Checks that this scope is currently in progress.
     *
     * @since 2.0
     * @throws IllegalStateException if this scope is not active
     */
    protected final void checkActive() {
        Preconditions.checkState(isActive(), "No %s block in progress", this);
    }
    
    /**
     * Checks that this scope is currently <strong>not</strong> in progress.
     *
     * @since 2.0
     * @throws IllegalStateException if this scope is active
     */
    protected final void checkNotActive() {
        Preconditions.checkState(isNotActive(), "%s already in progress", this);
    }
    
}

