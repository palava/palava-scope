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

import com.google.common.collect.Maps;

/**
 * {@link ConcurrentMap} based {@link ScopeContext} implementation.
 *
 * @since 2.0
 * @author Willi Schoenborn
 */
public abstract class ConcurrentMapScopeContext extends AbstractScopeContext {

    private ConcurrentMap<Object, Object> context;

    @Override
    protected final ConcurrentMap<Object, Object> delegate() {
        if (context == null) {
            context = Maps.newConcurrentMap();
        }
        return context;
    }
    
    @Override
    public void clear() {
        if (context == null) {
            return;
        } else {
            context.clear();
        }
    }
    
}
