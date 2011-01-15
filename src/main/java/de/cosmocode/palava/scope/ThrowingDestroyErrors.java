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

import com.google.common.base.Throwables;

/**
 * A reusable {@link DestroyErrors} implementations which directly throws
 * a {@link DestroyException} when {@link #destroyError(Object, Exception)}
 * is called. This is particularly useful when only one Strategy is used.
 *
 * @since 
 * @author Willi Schoenborn
 */
enum ThrowingDestroyErrors implements DestroyErrors {

    INSTANCE;

    @Override
    public void destroyError(Object object, Exception cause) {
        Throwables.propagateIfInstanceOf(cause, DestroyException.class);
        throw new DestroyException(cause);
    }
    
    @Override
    public void throwIfNecessary() {
        // nothing to do
    }
    
}
