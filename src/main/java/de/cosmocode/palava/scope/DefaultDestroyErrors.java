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

import java.util.Collections;

import com.google.inject.internal.Errors;
import com.google.inject.spi.Message;

/**
 * Default {@link DestroyErrors} implementation.
 *
 * @since 1.3
 * @author Willi Schoenborn
 */
final class DefaultDestroyErrors implements DestroyErrors {
    
    private final Errors errors = new Errors();

    @Override
    public void destroyError(Object object, Exception cause) {
        final String message = Errors.format("Failed to close %s", object);
        errors.addMessage(new Message(Collections.emptyList(), message, cause));
    }

    @Override
    public void throwIfNecessary() {
        if (errors.hasErrors()) {
            throw new DestroyException(errors.getMessages());
        }
    }

}
