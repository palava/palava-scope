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

import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingMap;

/**
 * A {@link Destroyable} {@link Map} implementation.
 *
 * @since 1.2
 * @author Willi Schoenborn
 * @param <K> generic key type
 * @param <V> generic value type
 */
public final class DestroyableMap<K, V> extends ForwardingMap<K, V> implements Destroyable {

    private final Map<K, V> map;
    
    private DestroyableMap(Map<K, V> map) {
        this.map = Preconditions.checkNotNull(map, "Map");
    }
    
    @Override
    protected Map<K, V> delegate() {
        return map;
    }
    
    @Override
    public void destroy() {
        for (Entry<K, V> entry : entrySet()) {
            Destroyables.destroySilently(entry.getKey());
            Destroyables.destroySilently(entry.getValue());
        }
        clear();
    }
    
    /**
     * Creates a {@link DestroyableMap} backed by the specified map.
     * 
     * @since 1.2
     * @param <K> the generic key type
     * @param <V> the generic value type
     * @param map the backing map
     * @return a {@link Destroyable} map backed by the given map
     * @throws NullPointerException if map is null
     */
    public static <K, V> Map<K, V> of(Map<K, V> map) {
        return new DestroyableMap<K, V>(map);
    }
    
}
