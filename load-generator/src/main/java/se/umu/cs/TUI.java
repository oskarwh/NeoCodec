package se.umu.cs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import se.umu.cs.controller.KeyController;
import se.umu.cs.windows.ControllWindow;


public class TUI 
{

    public static void main( String[] args )
    {
        try {
            File file = new File("stderr");
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos);
            System.setErr(ps);

            KeyController keyController = new KeyController();

            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();

            BasicWindow outputWindow = new OutputWindow();
            outputWindow.addWindowListener(keyController);

            BasicWindow controllWindow = new ControllWindow();
            controllWindow.addWindowListener(keyController);

            // Create interface and add windows to it
            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new NeoWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));

            gui.addWindow(outputWindow);
            gui.addWindowAndWait(controllWindow);
        } catch (IOException ex) {
        }
        
    }
}
