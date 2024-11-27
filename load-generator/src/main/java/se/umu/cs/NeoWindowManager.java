package se.umu.cs;

import java.util.List;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;

public class NeoWindowManager extends DefaultWindowManager {
    private final String controllWindowString = "ControllWindow";

    /*@Override
    public void onAdded(WindowBasedTextGUI textGUI, Window window, List<Window> allWindows) {
        switch (window.getClass().getName()) {
            case controllWindowString:
                
                break;
            default:
                throw new AssertionError();
        }
        
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onAdded'");
    }

    @Override
    public void onRemoved(WindowBasedTextGUI textGUI, Window window, List<Window> allWindows) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onRemoved'");
    }*/

    @Override
    public void prepareWindows(WindowBasedTextGUI textGUI, List<Window> allWindows, TerminalSize screenSize) {
        TerminalSize size = textGUI.getScreen().getTerminalSize();
        
        allWindows.get(0).setPosition(new TerminalPosition(0, (size.getRows()/8)*7));
        allWindows.get(0).setDecoratedSize(new TerminalSize(size.getColumns(), size.getRows()/8));
        
        allWindows.get(1).setPosition(TerminalPosition.TOP_LEFT_CORNER);
        allWindows.get(1).setDecoratedSize(new TerminalSize(size.getColumns(), (size.getRows()/8)*7-2));
    }
    
}
