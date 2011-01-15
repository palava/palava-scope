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

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Scope;

/**
 * An abstract {@link Scope} implementation which handles destruction.
 *
 * @since 2.0
 */
abstract class DestroyingScope implements Scope, DestroyStrategy {

    private DestroyStrategy strategy = NoopDestroyStrategy.INSTANCE;
    
    @Inject(optional = true)
    void setStrategy(DestroyStrategy strategy) {
        this.strategy = Preconditions.checkNotNull(strategy, "Strategy");
    }

    @Override
    public void destroy(Object object, DestroyErrors errors) {
        strategy.destroy(object, errors);
    }
    
    /**
     * Destroys the given objects if destruction is required
     * using the bound {@link DestroyStrategy DestroyStrategies}.
     *
     * @since 2.0
     * @param objects the objects to be destroyed
     */
    protected void destroy(Iterable<Object> objects) {
        final DestroyErrors errors = new DefaultDestroyErrors();
        
        for (Object value : objects) {
            strategy.destroy(value, errors); 
        }

        errors.throwIfNecessary();
    }
    
    /**
     * Destroys the given key-value paris if destruction is required
     * using the bound {@link DestroyStrategy DestroyStrategies}.
     *
     * @since 2.0
     * @param objects the objects to be destroyed
     */
    protected void destroy(Map<? extends Object, ? extends Object> objects) {
        final DestroyErrors errors = new DefaultDestroyErrors();
        
        for (Entry<? extends Object, ? extends Object> entry : objects.entrySet()) {
            strategy.destroy(entry.getKey(), errors);
            strategy.destroy(entry.getValue(), errors);
        }

        errors.throwIfNecessary();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
    
}

