/**
 * palava - a java-php-bridge
 * Copyright (C) 2007-2010  CosmoCode GmbH
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package de.cosmocode.palava.scope;

import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import de.cosmocode.palava.core.aop.AbstractPalavaAspect;

final aspect UnitOfWorkScopeAspect extends AbstractPalavaAspect issingleton() {

    private static final Logger LOG = LoggerFactory.getLogger(UnitOfWorkScope.class);
    
    private UnitOfWorkScope scope;
    
    @Inject
    void setUnitOfWorkScope(UnitOfWorkScope scope) {
        this.scope = Preconditions.checkNotNull(scope, "Scope");
    }
    
    pointcut unitOfWork(): execution(@UnitOfWork * *.*(..));
    
    @SuppressAjWarnings("adviceDidNotMatch")
    Object around(): unitOfWork() {
        checkState();
        LOG.trace("Handling UnitOfWorkScope at {}", thisJoinPointStaticPart);
        if (scope.inProgress()) {
            LOG.trace("UnitOfWorkScope already in progress");
            return proceed();
        } else {
            LOG.trace("UnitOfWorkScope not in progress");
            scope.begin();
            try {
                return proceed();
            } finally {
                scope.end();
            }
        }
    }
    
}
