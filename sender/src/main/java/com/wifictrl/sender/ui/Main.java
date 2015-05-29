package com.wifictrl.sender.ui;

import java.awt.AWTEvent;
import java.awt.Dimension;
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
		JFrame frame = new JFrame("Sender: Keep this window open to send events");
		frame.setSize(new Dimension(500, 500));
		Toolkit.getDefaultToolkit().addAWTEventListener(new Listener(sender, frame.getSize()), AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_WHEEL_EVENT_MASK );
		frame.setVisible(true);
//		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setSize(new Dimension(500, 500));
		frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
            	log.info("Exiting program");
            	System.exit(0);
            }
        });
		frame.setVisible(true);
	}

	private static class Listener implements AWTEventListener {
		
		private final InmediateSender sender;
		private final Dimension frame;
		
		public Listener(InmediateSender sender, Dimension frame){
			this.sender = sender;
			this.frame = frame;
			log.debug("Size: "+frame.getWidth()+"x"+frame.getWidth());	
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
            case MouseEvent.MOUSE_CLICKED:
            	sendReleased((MouseEvent) event, Constants.MOUSE_CLICKED);
            	break;
            case MouseEvent.MOUSE_PRESSED:
            	sendReleased((MouseEvent) event, Constants.MOUSE_PRESSED);
            	break;
            case MouseEvent.MOUSE_MOVED:
            	sendMove((MouseEvent) event);
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
		}
        
        private void sendMove(MouseEvent event){
			Info<Integer[]> info = new Info<>();
			Integer[] xy = new Integer[]{event.getX(), event.getY(), frame.getSize().width, frame.getSize().height};
			info.setAction(Constants.MOUSE_MOVE);
			info.setData(xy);
			sender.send(info);
		}
        
        private void sendWheel(MouseWheelEvent event) {
			Info<Integer> info = new Info<>();
			info.setAction(Constants.MOUSE_WHEEL);
			info.setData(event.getWheelRotation());
			sender.send(info);
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
		}
    }
}
