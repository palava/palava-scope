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
 * The default {@link DestroyStrategy} implementation which checks for
 * {@link Destroyable} instances.
 *
 * @since 2.0
 * @author Willi Schoenborn
 */
final class DefaultDestroyStrategy implements DestroyStrategy {
    
    private static final Logger LOG = LoggerFactory.getLogger(DefaultDestroyStrategy.class);

    @Override
    public void destroy(Object object, DestroyErrors errors) {
        if (object instanceof Destroyable) {
            try {
                LOG.trace("Destroying destroyable {}", object);
                Destroyable.class.cast(object).destroy();
            /*CHECKSTYLE:OFF*/
            } catch (RuntimeException e) {
            /*CHECKSTYLE:ON*/
                errors.destroyError(object, e);
            }
        }
    }

}
