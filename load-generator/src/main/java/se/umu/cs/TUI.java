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
import se.umu.cs.windows.OutputWindow;

import se.umu.cs.pulsar.PClient;

public class TUI 
{
    /*
     * Arguments:
     *  <brokers> - Brokers consist of a comma separated list of host:port pairs
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1)
            throw new IllegalArgumentException("Usage: <brokers>");

        String brokers = args[0];

        try {
            File file = new File("stderr");
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos);
            System.setErr(ps);

            
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
        } catch (IOException ex) {

        }
        
    }
}
