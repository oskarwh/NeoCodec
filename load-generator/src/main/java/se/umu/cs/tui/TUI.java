package se.umu.cs.tui;

import java.io.IOException;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import se.umu.cs.tui.controllers.KeyController;
import se.umu.cs.tui.windows.ControllWindow;
import se.umu.cs.tui.windows.OutputWindow;

public class TUI 
{
    public TUI() throws IOException {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        screen.startScreen();
        
        OutputWindow outputWindow = new OutputWindow();
        ControllWindow controllWindow = new ControllWindow();
        KeyController keyController = new KeyController(controllWindow, outputWindow);
                    
        outputWindow.addWindowListener(keyController);
        controllWindow.addWindowListener(keyController);

        // Create interface and add windows to it
        MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new NeoWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));

        gui.addWindow(outputWindow);
        gui.addWindowAndWait(controllWindow);
    }
}
