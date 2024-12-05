package se.umu.cs.tui.controllers;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowListener;
import com.googlecode.lanterna.input.KeyStroke;

import se.umu.cs.tui.windows.ControllWindow;
import se.umu.cs.tui.windows.OutputWindow;

public class KeyController implements WindowListener {
    private CommandController commands;
    private ControllWindow controllWindow;
    private OutputWindow outputWindow;

    public KeyController() {
        this.commands = new CommandController();
    }

    public KeyController(ControllWindow cw, OutputWindow ow) {
        this.commands = new CommandController(cw, ow);
        this.controllWindow = cw;
        this.outputWindow = ow;
    }

    @Override
    public void onInput(Window basePane, KeyStroke keyStroke, AtomicBoolean deliverEvent) {
        if (commands == null) {
            System.err.println("No command controller");
            return;
        }

        if (keyStroke.getCharacter() == null)
            return;

        if (keyStroke.isCtrlDown()) {
            switch (keyStroke.getCharacter()) {
                case 'e':
                    commands.clearOutput();
                    break;

                case 's':
                    commands.stopRun();
                    break;

                case 'c':
                    System.exit(0);
                    break;

                default:
                    System.err.println("Unhandled ctrl key: " + keyStroke.getCharacter());
                    break;
            }
        } else {
            // Handles normal key inputs
            System.err.println("Pressed: " + keyStroke);
            
            String commandStr = this.controllWindow.getInput();
            if (commandStr == null) {
                System.err.println("No input reference");
                return;
            } else if (keyStroke.getCharacter() == '\n') {
                try {
                    commands.handleCommand(commandStr);
                } catch (IOException e) {
                    System.err.println("Something went wrong.");
                }
            }

        }
    }

    @Override
    public void onUnhandledInput(Window basePane, KeyStroke keyStroke, AtomicBoolean hasBeenHandled) {

    }

    @Override
    public void onResized(Window window, TerminalSize oldSize, TerminalSize newSize) {
        // TODO Auto-generated method stub
       
    }

    @Override
    public void onMoved(Window window, TerminalPosition oldPosition, TerminalPosition newPosition) {
        // TODO Auto-generated method stub
        
    }
}
