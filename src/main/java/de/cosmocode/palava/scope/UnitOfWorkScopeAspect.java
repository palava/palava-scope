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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import de.cosmocode.commons.Throwables;
import de.cosmocode.palava.core.aop.PalavaAspect;

/**
 * An {@link Aspect} which manages {@link UnitOfWorkScope} states required
 * by methods annotated with {@link UnitOfWork}.
 *
 * @since 2.0
 * @author Willi Schoenborn
 */
@Aspect
final class UnitOfWorkScopeAspect extends PalavaAspect {

    private static final Logger LOG = LoggerFactory.getLogger(UnitOfWorkScope.class);
    
    private UnitOfWorkScope scope;
    
    @Inject
    void setScope(UnitOfWorkScope scope) {
        this.scope = Preconditions.checkNotNull(scope, "Scope");
    }
    
    @Pointcut("execution(@de.cosmocode.palava.scope.UnitOfWork * *.*(..))")
    @SuppressWarnings("unused")
    private void unitOfWork() { }
    
    /**
     * An advice around all methods annotated with {@link UnitOfWork} which
     * handles the correct usage of the {@link UnitOfWorkScope}.
     *
     * @since 1.3
     * @param point the proceeding join point
     * @return the result returned by the proceeding join point execution
     */
    @Around("unitOfWork()")
    public Object aroundUnitOfWork(ProceedingJoinPoint point) {
        checkState();
        LOG.trace("Handling UnitOfWorkScope at {}", point.getStaticPart());
        if (scope.isActive()) {
            LOG.trace("UnitOfWorkScope already in progress");
            return proceed(point);
        } else {
            LOG.trace("Beginning managed UnitOfWorkScope");
            scope.begin();
            try {
                return proceed(point);
            } finally {
                scope.end();
                LOG.trace("Ended managed UnitOfWorkScope");
            }
        }
    }
    
    private Object proceed(ProceedingJoinPoint point) {
        try {
            return point.proceed();
        /* CHECKSTYLE:OFF */
        } catch (Throwable e) {
        /* CHECKSTYLE:ON */
            throw Throwables.sneakyThrow(e);
        }
    }
    
}
