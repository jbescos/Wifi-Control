package com.wifictrl.sender.ui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wifictrl.common.core.Constants;
import com.wifictrl.common.core.Info;
import com.wifictrl.sender.core.InmediateSender;

public class Main {

	private final static Logger log = LogManager.getLogger();

	public static void main(String[] args) throws SocketException, UnknownHostException {
		log.info("Args "+Arrays.toString(args));
		final InmediateSender sender = new InmediateSender(args[0]);
		JFrame frame = new JFrame();
		frame.setUndecorated(true);
		frame.setResizable(false);
		JLabel text = new JLabel("", SwingConstants.CENTER);
		frame.add(new JLabel("Press ALT+Tab to change screen and ALT+F4 to close it.", SwingConstants.CENTER), BorderLayout.CENTER);
		frame.add(text, BorderLayout.NORTH);
		frame.validate();
 
 		GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
		
		frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
            	log.info("Exiting program");
            	System.exit(0);
            }
        });
		Toolkit.getDefaultToolkit().addAWTEventListener(new Listener(sender, frame.getWidth(), frame.getHeight(), text), AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_WHEEL_EVENT_MASK );
	}

	private static class Listener implements AWTEventListener {
		
		private final InmediateSender sender;
		private final JLabel text;
		private final int width;
		private final int height;
		private final int HEIGHT_FIX = 26;
		private final int WIDTH_FIX = 1;
		
		public Listener(InmediateSender sender, int width, int height, JLabel text){
			this.sender = sender;
			this.text = text;
			this.width = width - WIDTH_FIX;
			this.height = height - HEIGHT_FIX;
			log.debug("Size: "+this.width+"x"+this.height);	
		}
		
        public void eventDispatched(AWTEvent event) {
            switch(event.getID()) {
            case KeyEvent.KEY_RELEASED:
            	sendKey((KeyEvent) event, Constants.KEY_RELEASED);
            	break;
            case KeyEvent.KEY_PRESSED:
            	sendKey((KeyEvent) event, Constants.KEY_PRESSED);
            	break;
            case MouseEvent.MOUSE_RELEASED:
            	sendReleased((MouseEvent) event, Constants.MOUSE_RELEASED);
            	break;
            case MouseEvent.MOUSE_PRESSED:
            	sendReleased((MouseEvent) event, Constants.MOUSE_PRESSED);
            	break;
            case MouseEvent.MOUSE_DRAGGED:
            	sendMove((MouseEvent) event, Constants.MOUSE_MOVE);
            	break;
            case MouseEvent.MOUSE_MOVED:
            	sendMove((MouseEvent) event, Constants.MOUSE_MOVE);
                break;
            case MouseEvent.MOUSE_WHEEL:
            	sendWheel((MouseWheelEvent) event);
                break;
            default:
            	log.debug("Uncatch event: "+event);
          }
        }
        
        private void sendKey(KeyEvent event, int action){
			Info<Integer> info = new Info<>();
			info.setAction(action);
			info.setData(event.getKeyCode());
			sender.send(info);
			text.setText("Key: "+event.getKeyChar());
		}
        
        private void sendMove(MouseEvent event, int action){
			Info<Integer[]> info = new Info<>();
			Integer[] xy = new Integer[]{event.getX(), event.getY(), width, height};
			info.setAction(action);
			info.setData(xy);
			sender.send(info);
			text.setText("Move: ("+xy[0]+","+xy[1]+")");
		}
        
        private void sendWheel(MouseWheelEvent event) {
			Info<Integer> info = new Info<>();
			info.setAction(Constants.MOUSE_WHEEL);
			info.setData(event.getWheelRotation());
			sender.send(info);
			text.setText("Wheel: "+event.getWheelRotation());
		}

		private void sendReleased(MouseEvent event, int action) {
			Info<Integer> info = new Info<>();
			info.setAction(action);
			int botButton = -1;
			if(MouseEvent.BUTTON1 == event.getButton()){
				botButton = InputEvent.BUTTON1_DOWN_MASK;
			}else if(MouseEvent.BUTTON2 == event.getButton()){
				botButton = InputEvent.BUTTON2_DOWN_MASK;
			}else if(MouseEvent.BUTTON3 == event.getButton()){
				botButton = InputEvent.BUTTON3_DOWN_MASK;
			}
			info.setData(botButton);
			sender.send(info);
			text.setText("Button: "+botButton);
		}
    }
}
