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

import org.junit.Assert;
import org.junit.Test;

import com.google.inject.Injector;

import de.cosmocode.junit.UnitProvider;

/**
 * Abstract tests for {@link UnitOfWorkScope}s.
 *
 * @author Willi Schoenborn
 */
public abstract class AbstractUnitOfWorkScopeTest implements UnitProvider<Injector> {

    /**
     * Tests {@link UnitOfWorkScope#isActive()}.
     */
    @Test
    public void manual() {
        final UnitOfWorkScope unit = unit().getInstance(UnitOfWorkScope.class);
        Assert.assertFalse(unit.isActive());
        unit.begin();
        Assert.assertTrue(unit.isActive());
        unit.end();
        Assert.assertFalse(unit.isActive());
    }

    /**
     * Tests {@link DestroyStrategy}.
     */
    @Test
    public void destroy() {
        final Injector injector = unit();
        final UnitOfWorkScope unit = injector.getInstance(UnitOfWorkScope.class);
        unit.begin();
        final DestroyableService service = injector.getInstance(DestroyableService.class);
        Assert.assertFalse(service.isDestroyed());
        unit.end();
        Assert.assertTrue(service.isDestroyed());
    }
    
}
