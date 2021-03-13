package javax.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import static javax.swing.UIManager.setLookAndFeel;
import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import javax.swing.UnsupportedLookAndFeelException;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;

public abstract class AbstractFrame extends JFrame implements ActionListener {
    
    private class WindowListener extends WindowAdapter {
        @Override
        public void windowOpened(WindowEvent e) {
            onInitComponents();
            pack();
        }
    }
    
    static {
        initLookAndFeel();
    }
    
    public AbstractFrame() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);
        addWindowListener(new WindowListener());
    }

    @Override
    public final void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command != null && !command.isEmpty()) {
            onCommand(command);
        }
    }
    
    protected void onInitComponents() {}
    
    protected void onCommand(String command) {}
    
    private void message(String title, String message, int type) {
        showMessageDialog(this, message, title, type);
    }
    
    protected void message(String title, String message) {
        message(title, message, PLAIN_MESSAGE);
    }
    
    protected void message(String message) {
        message("Message", message);
    }
    
    protected void error(String title, String message) {
        message(title, message, ERROR_MESSAGE);
    }
    
    protected void error(String message) {
        error("Error", message);
    }

    public static void initLookAndFeel() {
        try {
            setLookAndFeel(getSystemLookAndFeelClassName());
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ignore) {
        }
    }
    
}
