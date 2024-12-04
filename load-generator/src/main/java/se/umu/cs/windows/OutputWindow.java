package se.umu.cs.windows;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;

public class OutputWindow extends BasicWindow {
    private Panel panel = null;
    private TextBox textBox = null;

    public OutputWindow() {
        this.panel = new Panel();
        this.textBox = new TextBox("");
        this.textBox = new TextBox("placeholder", TextBox.Style.SINGLE_LINE);
        
        this.panel.addComponent(this.textBox);
        
        // Add panel to window
        this.setComponent(panel);
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

    public void clearOutput() {
        this.textBox.setText("");
    }
}
