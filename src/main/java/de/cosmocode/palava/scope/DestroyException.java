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
import java.util.List;

import com.google.inject.internal.Errors;
import com.google.inject.spi.Message;

/**
 * Indicates one or more errors during {@link MoreScopes#close(com.google.inject.Injector, Class)}.
 *
 * @since 2.0
 * @author Willi Schoenborn
 */
final class DestroyException extends RuntimeException {

    private static final long serialVersionUID = 2065909913574478493L;

    DestroyException(List<Message> messages) {
        super(Errors.format("Destroy errors", messages));
    }
    
    DestroyException(Exception e) {
        this(Collections.singletonList(new Message(Collections.emptyList(), e.getMessage(), e)));
    }
    
}
