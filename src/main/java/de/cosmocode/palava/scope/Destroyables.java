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

/**
 * Utility class for {@link Destroyable}s.
 *
 * @since 1.2
 * @author Willi Schoenborn
 */
public final class Destroyables {

    private static final Logger LOG = LoggerFactory.getLogger(Destroyables.class);

    private Destroyables() {
        
    }
    
    /**
     * Destroy the given object if it is {@link Destroyable}.
     * 
     * @since 1.2
     * @param object the object to be destroyed
     * @throws RuntimeException if {@link Destroyable#destroy()} failed
     */
    public static void destroy(Object object) {
        if (object instanceof Destroyable) {
            Destroyable.class.cast(object).destroy();
        }
    }
    
    /**
     * Destroys the given object if it is {@link Destroyable}
     * and logs errors that occur.
     * 
     * @since 1.2
     * @param object the object to be destroyed
     */
    public static void destroySilently(Object object) {
        if (object instanceof Destroyable) {
            try {
                LOG.trace("Destroying {}", object);
                Destroyable.class.cast(object).destroy();
                /*CHECKSTYLE:OFF*/
            } catch (RuntimeException e) {
                /*CHECKSTYLE:ON*/
                LOG.error("Failed to destroy: " + object, e);
            }
        }
    }
    
}
