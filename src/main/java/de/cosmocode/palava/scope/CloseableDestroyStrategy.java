package de.cosmocode.palava.scope;

import java.io.Closeable;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.Beta;

/**
 * A {@link DestroyStrategy} which supports {@link Closeable}s.
 *
 * @since 2.1
 * @author Willi Schoenborn
 */
@Beta
public final class CloseableDestroyStrategy implements DestroyStrategy {
    
    private static final Logger LOG = LoggerFactory.getLogger(CloseableDestroyStrategy.class);

    @Override
    public void destroy(Object object, DestroyErrors errors) {
        if (object instanceof Closeable) {
            try {
                LOG.trace("Closing closeable {}", object);
                Closeable.class.cast(object).close();
            } catch (IOException e) {
                errors.destroyError(object, e);
            }
        }
    }

}
