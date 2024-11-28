package se.umu.cs.windows;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;

public class ControllWindow extends BasicWindow {
    private Panel panel = null;
    private TextBox textBox = null;
    
    public ControllWindow() {
        this.panel = new Panel();
        this.textBox = new TextBox("placeholder", TextBox.Style.SINGLE_LINE);
        this.textBox.setTheme(new SimpleTheme(TextColor.ANSI.BLACK, new TextColor.RGB(208,207,204)));
        
        // Handle enter keystroke
        //this.textBox.handleKeyStroke(KeyStroke.fromString(keyStr));
        
        this.panel.addComponent(this.textBox);

        // Add panel to window
        this.setComponent(this.panel);
    }

    public void updateSize() {
        TerminalSize winSize = this.getSize();
        if (winSize != null) {
            // Update 
            this.panel.setSize(winSize);
            this.panel.setPosition(TerminalPosition.TOP_LEFT_CORNER);
            
            // Set size of textbox
            this.textBox.setSize(winSize);
        }
    }
}
