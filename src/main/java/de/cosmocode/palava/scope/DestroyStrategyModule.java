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

import java.util.Set;

import com.google.common.annotations.Beta;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

/**
 * Binds {@link DestroyStrategy} to {@link CompoundDestroyStrategy}
 * and a {@link Set} of {@link DestroyStrategy DestroyStrategies} containing
 * the {@link DefaultDestroyStrategy}.
 *
 * @since 2.0
 * @author Willi Schoenborn
 */
@Beta
public final class DestroyStrategyModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(DestroyStrategy.class).to(CompoundDestroyStrategy.class).in(Singleton.class);
        final Multibinder<DestroyStrategy> multibinder = Multibinder.newSetBinder(binder, DestroyStrategy.class);
        multibinder.addBinding().to(DefaultDestroyStrategy.class).in(Singleton.class);
    }

}
