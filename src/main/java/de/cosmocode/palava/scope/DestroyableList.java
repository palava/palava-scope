package de.cosmocode.palava.scope;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingList;

/**
 * A {@link Destroyable} {@link List} implementation.
 *
 * @since 1.2
 * @author Willi Schoenborn
 * @param <E> the generic element type
 */
public final class DestroyableList<E> extends ForwardingList<E> implements Destroyable {

    private final List<E> list;
    
    private DestroyableList(List<E> list) {
        this.list = Preconditions.checkNotNull(list, "List");
    }
    
    @Override
    protected List<E> delegate() {
        return list;
    }
    
    @Override
    public void destroy() {
        for (E element : this) {
            Destroyables.destroySilently(element);
        }
        clear();
    }

    /**
     * Creates a {@link Destroyable} list backed by the specified list.
     * 
     * @since 1.2
     * @param <E> the generic element type
     * @param list the backing list
     * @return a {@link Destroyable} list backed by the given list
     * @throws NullPointerException if list is null
     */
    public static <E> List<E> of(List<E> list) {
        return new DestroyableList<E>(list);
    }
    
}
