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

import com.google.common.annotations.Beta;

/**
 * Collected {@link Exception exceptions} occured during destroy attempts.
 *
 * @since 2.0
 * @author Willi Schoenborn
 */
@Beta
public interface DestroyErrors {

    /**
     * Adds the specified exception to this errors.
     * 
     * @param object the instance being closed
     * @param cause the exception thrown when the close was attempted
     */
    void destroyError(Object object, Exception cause);

    /**
     * Checks if this collection of errors contains any exception and
     * throws a runtime exception if neccessary.
     *
     * @since 2.0
     * @throws RuntimeException if neccessary
     */
    void throwIfNecessary();

}

