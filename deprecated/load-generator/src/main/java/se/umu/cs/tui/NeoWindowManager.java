package se.umu.cs.tui;

import java.util.List;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;

import se.umu.cs.tui.windows.ControllWindow;

public class NeoWindowManager extends DefaultWindowManager {
    private final String controllWindowString = "se.umu.cs.tui.windows.ControllWindow";
    private final String outputWindowString = "se.umu.cs.tui.windows.OutputWindow";

    @Override
    public void prepareWindows(WindowBasedTextGUI textGUI, List<Window> allWindows, TerminalSize screenSize) {
        TerminalSize size = textGUI.getScreen().getTerminalSize();

        for (Window window : allWindows) {
            if (window.getClass().getName().equals(controllWindowString)) {
                int height = 3;
                int width = size.getColumns() - 2;

                window.setPosition(new TerminalPosition(1, (size.getRows()/16)*15 + 1));
                window.setDecoratedSize(new TerminalSize(width < 1 ? 1 : width, height));

                ControllWindow cWin = (ControllWindow) window;
                cWin.updateSize();
            }else if (window.getClass().getName().equals(outputWindowString)) {
                int height = ((size.getRows()/16)*15) - 2;
                int width = size.getColumns() - 2;

                window.setPosition(new TerminalPosition(1, 1));
                window.setDecoratedSize(new TerminalSize(width < 1 ? 1 : width, height < 1 ? 1 : height));
            }
        }

    }    
}
