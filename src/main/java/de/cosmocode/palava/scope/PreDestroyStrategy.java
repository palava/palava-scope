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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.Beta;

/**
 * A {@link DestroyStrategy} which supports {@link PreDestroy} annotations.
 *
 * @since 2.0
 * @author Willi Schoenborn
 */
@Beta
final class PreDestroyStrategy implements DestroyStrategy {
    
    private static final Logger LOG = LoggerFactory.getLogger(PreDestroyStrategy.class);

    @Override
    public void destroy(Object object, DestroyErrors errors) {
        destroy(object, object.getClass(), errors);
    }
    
    private void destroy(Object object, Class<?> type, DestroyErrors errors) {
        final Class<?> superType = type.getSuperclass();
        if (superType == null) return;
        
        // super classes go first
        destroy(object, superType, errors);
        
        for (Method method : type.getDeclaredMethods()) {
            invokeIfPossible(object, method, errors);
        }
    }
    
    private void invokeIfPossible(Object object, Method method, DestroyErrors errors) {
        if (method.isAnnotationPresent(PreDestroy.class)) {
            try {
                LOG.trace("Destroying {} using @PreDestroy-annotated method {}", object, method);
                method.invoke(object);
            } catch (IllegalArgumentException e) {
                errors.destroyError(object, e);
            } catch (IllegalAccessException e) {
                errors.destroyError(object, e);
            } catch (InvocationTargetException e) {
                errors.destroyError(object, e);
            }
        }
    }

}
