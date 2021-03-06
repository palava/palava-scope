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

import java.io.Closeable;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.Beta;

/**
 * A {@link DestroyStrategy} which supports {@link Closeable}s.
 *
 * @since 2.1
 * @author Willi Schoenborn
 */
@Beta
public final class CloseableDestroyStrategy implements DestroyStrategy {
    
    private static final Logger LOG = LoggerFactory.getLogger(CloseableDestroyStrategy.class);

    @Override
    public void destroy(Object object, DestroyErrors errors) {
        if (object instanceof Closeable) {
            try {
                LOG.trace("Closing closeable {}", object);
                Closeable.class.cast(object).close();
            } catch (IOException e) {
                errors.destroyError(object, e);
            }
        }
    }

}
