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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;


/**
 * Abstract skeleton implementation of the {@link ScopeContext} interface.
 *
 * @author Willi Schoenborn
 */
public abstract class AbstractScopeContext implements ScopeContext {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractScopeContext.class);
    
    /**
     * Provide the underlying context map. Allows sub classes to 
     * plug-in different map implementations.
     * 
     * @return a context map
     */
    protected abstract Map<Object, Object> context();

    @Override
    public <K, V> void set(K key, V value) {
        Preconditions.checkNotNull(key, "Key");
        context().put(key, value);
    };
    
    @Override
    @SuppressWarnings("unchecked")
    public <K, V> V get(K key) {
        Preconditions.checkNotNull(key, "Key");
        return (V) context().get(key);
    };

    @Override
    public <K> boolean contains(K key) {
        Preconditions.checkNotNull(key, "Key");
        return context().containsKey(key);
    };
    
    @Override
    @SuppressWarnings("unchecked")
    public <K, V> V remove(K key) {
        Preconditions.checkNotNull(key, "Key");
        return (V) context().remove(key);
    };
    
    @Override
    public <K, V> void putAll(Map<? extends K, ? extends V> map) {
        Preconditions.checkNotNull(map, "Map");
        context().putAll(map);
    }
    
    @Override
    public Iterator<Entry<Object, Object>> iterator() {
        return context().entrySet().iterator();
    }
    
    @Override
    public void clear() {
        final Iterator<Entry<Object, Object>> iterator = iterator();
        
        while (iterator.hasNext()) {
            final Entry<Object, Object> entry = iterator.next();
            
            if (entry.getKey() instanceof Destroyable) {
                try {
                    LOG.trace("Destroying key {}", entry.getKey());
                    Destroyable.class.cast(entry.getKey()).destroy();
                    /*CHECKSTYLE:OFF*/
                } catch (RuntimeException e) {
                    /*CHECKSTYLE:ON*/
                    LOG.error("Failed to destroy scoped key: {}", e);
                }
            }
            if (entry.getValue() instanceof Destroyable) {
                try {
                    LOG.trace("Destroying value {}", entry.getValue());
                    Destroyable.class.cast(entry.getValue()).destroy();
                    /*CHECKSTYLE:OFF*/
                } catch (RuntimeException e) {
                    /*CHECKSTYLE:ON*/
                    LOG.error("Failed to destroy scoped value: {}", e);
                }
            }
            
            iterator.remove();
        }
    }
    
}
