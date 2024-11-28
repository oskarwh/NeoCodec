package se.umu.cs;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;

public class OutputWindow extends BasicWindow {


    public OutputWindow() {

        Panel panel = new Panel();
        panel.addComponent(new TextBox("placeholder"));
        
        // Add panel to window
        this.setComponent(panel);
    }
}
