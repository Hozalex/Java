package javax.gui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;

public abstract class AbstractDialog extends JDialog implements ActionListener {

    private class WindowListener extends WindowAdapter {

        @Override
        public void windowOpened(WindowEvent e) {
            onInitComponents();
            pack();
            setLocation(getX() - getWidth() / 2, getY() - getHeight() / 2);
        }
        
    }
    
    public AbstractDialog(Window parent) {
        super(parent);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        addWindowListener(new WindowListener());
    }
    
    protected void onInitComponents() {}
    
    protected void onCommand(String command) {}

    @Override
    public final void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command != null && !command.isEmpty()) {
            onCommand(command);
        }
    }
    
}
