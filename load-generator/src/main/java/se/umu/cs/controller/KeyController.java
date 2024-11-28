package se.umu.cs.controller;

import java.util.concurrent.atomic.AtomicBoolean;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowListener;
import com.googlecode.lanterna.input.KeyStroke;

public class KeyController implements WindowListener {
    
    public KeyController() {

    }

    @Override
    public void onInput(Window basePane, KeyStroke keyStroke, AtomicBoolean deliverEvent) {
        System.err.println("Pressed: " + keyStroke);
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
