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

import de.cosmocode.junit.UnitProvider;

/**
 * Abstract tests for {@link UnitOfWorkScope}s.
 *
 * @author Willi Schoenborn
 */
public abstract class AbstractUnitOfWorkScopeTest implements UnitProvider<UnitOfWorkScope> {

    /**
     * Tests {@link UnitOfWorkScope#inProgress()}.
     */
    @Test
    public void inProgress() {
        final UnitOfWorkScope unit = unit();
        Assert.assertFalse(unit.inProgress());
        unit.begin();
        Assert.assertTrue(unit.inProgress());
        unit.end();
        Assert.assertFalse(unit.inProgress());
    }

    /**
     * Tests whether {@link UnitOfWork} annotation weaving works.
     */
    @Test
    public void weave() {
        final UnitOfWorkScope unit = unit();
        Assert.assertFalse(unit.inProgress());
        scoped(unit);
        Assert.assertFalse(unit.inProgress());
    }

    @UnitOfWork
    private void scoped(UnitOfWorkScope unit) {
        Assert.assertTrue(unit.inProgress());
    }
    
}
