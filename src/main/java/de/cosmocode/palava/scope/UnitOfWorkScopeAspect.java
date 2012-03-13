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

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import de.cosmocode.commons.Throwables;
import de.cosmocode.palava.core.aop.PalavaAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aspect for {@link UnitOfWork}.
 */
@Aspect
public final class UnitOfWorkScopeAspect extends PalavaAspect {

    private static final Logger LOG = LoggerFactory.getLogger(UnitOfWorkScope.class);
    
    private UnitOfWorkScope scope;
    
    @Inject
    void setScope(UnitOfWorkScope scope) {
        this.scope = Preconditions.checkNotNull(scope, "Scope");
    }

    /**
     * Advices methods annotated with {@link UnitOfWork}.
     *
     * @param point the proceeding join point
     * @return the return value of the adviced join point
     */
    @Around("execution(@de.cosmocode.palava.scope.UnitOfWork * *(..))")
    public Object unitOfWork(ProceedingJoinPoint point) {
        checkInjected();
        LOG.trace("Handling UnitOfWorkScope at {}", point.getStaticPart());
        if (scope.inProgress()) {
            LOG.trace("UnitOfWorkScope already in progress");
            try {
                return point.proceed();
            // CHECKSTYLE:OFF
            } catch (Throwable e) {
            // CHECKSTYLE:ON
                throw Throwables.sneakyThrow(e);
            }
        } else {
            LOG.trace("UnitOfWorkScope not in progress");
            scope.begin();
            try {
                return point.proceed();
            // CHECKSTYLE:OFF
            } catch (Throwable e) {
            // CHECKSTYLE:ON
                throw Throwables.sneakyThrow(e);
            } finally {
                scope.end();
            }
        }
    }
    
}
