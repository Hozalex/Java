package javax.gui.events;

import java.awt.Component;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import static java.awt.event.HierarchyEvent.*;

public abstract class HierarchyAdapter implements HierarchyListener {

    private boolean isMaskPresent(long flag, int mask) {
        return (flag & mask) == mask; 
    }
    
    @Override
    public final void hierarchyChanged(HierarchyEvent e) {
        if (isMaskPresent(e.getChangeFlags(), PARENT_CHANGED)) {
            onParentChanged(e.getChangedParent());
        }
    }
    
    protected void onParentChanged(Component parent) {}

}
