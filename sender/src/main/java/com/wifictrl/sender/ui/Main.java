package com.wifictrl.sender.ui;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wifictrl.common.core.Constants;
import com.wifictrl.common.core.Info;
import com.wifictrl.sender.core.BufferSender;

public class Main {

	private final static Logger log = LogManager.getLogger();

	public static void main(String[] args) throws SocketException, UnknownHostException {
		log.info("Args "+Arrays.toString(args));
		final BufferSender sender = new BufferSender(args[0]);
		JFrame frame = new JFrame("Sender: Keep this window open to send events");
		Toolkit.getDefaultToolkit().addAWTEventListener(new Listener(sender, frame), AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_WHEEL_EVENT_MASK );
			
		frame.setVisible(true);
//		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setSize(new Dimension(500, 500));
		frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
            	try {
					sender.stop();
				} catch (InterruptedException e1) {
					log.error("Can not stop the task", e1);
				}
            	log.info("Exiting program");
            	System.exit(0);
            }
        });
		frame.setVisible(true);
	}

	private static class Listener implements AWTEventListener {
		
		private final BufferSender sender;
		private final JFrame frame;
		
		public Listener(BufferSender sender, JFrame frame){
			try {
				this.sender = sender;
				this.frame = frame;
				sender.start();
			} catch (IOException e) {
				log.error("Error", e);
				throw new RuntimeException("Error", e);
			}
		}
		
        public void eventDispatched(AWTEvent event) {
            switch(event.getID()) {
            case KeyEvent.KEY_RELEASED:
            	sendKey((KeyEvent) event);
            	break;
            case MouseEvent.MOUSE_RELEASED:
            	sendReleased((MouseEvent) event);
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
        
        private void sendKey(KeyEvent event){
			Info<Integer> info = new Info<>();
			info.setAction(Constants.KEY_RELEASED);
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

		private void sendReleased(MouseEvent event) {
			Info<Integer> info = new Info<>();
			info.setAction(Constants.MOUSE_RELEASED);
			info.setData(event.getButton());
			sender.send(info);
		}
    }
}
