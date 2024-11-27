package se.umu.cs;

import java.io.IOException;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

public class TUI 
{

    public static void main( String[] args )
    {
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();

            BasicWindow window1 = new BasicWindow();
            Panel panel1 = new Panel();
            panel1.addComponent(new TextBox("INPUT FIELD"));
            window1.setComponent(panel1);

            BasicWindow window2 = new BasicWindow();
            Panel panel2 = new Panel();
            panel2.addComponent(new TextBox("INPUT FIELD"));
            window2.setComponent(panel2);
            
            MultiWindowTextGUI gui = new MultiWindowTextGUI(screen, new NeoWindowManager(), new EmptySpace(TextColor.ANSI.BLUE));
            
            gui.addWindow(window1);
            gui.addWindowAndWait(window2);

            
        } catch (IOException ex) {
        }
        
    }
}
