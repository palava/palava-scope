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

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

/**
 * A compound {@link DestroyStrategy} which combines multiple strategies
 * into one.
 *
 * @since 1.3
 * @author Willi Schoenborn
 */
final class CompoundDestroyStrategy implements DestroyStrategy {

    private final Iterable<DestroyStrategy> strategies;
    
    @Inject
    CompoundDestroyStrategy(Set<DestroyStrategy> strategies) {
        this.strategies = Preconditions.checkNotNull(strategies, "Strategies");
    }
    
    @Override
    public void destroy(Object instance, DestroyErrors errors) {
        for (DestroyStrategy strategy : strategies) {
            strategy.destroy(instance, errors);
        }
    }

}
