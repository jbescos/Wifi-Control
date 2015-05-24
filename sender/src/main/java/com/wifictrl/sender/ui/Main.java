package com.wifictrl.sender.ui;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wifictrl.sender.core.BufferSender;

public class Main {

	private final static Logger log = LogManager.getLogger();

	public static void main(String[] args) throws SocketException, UnknownHostException {
		final BufferSender sender = new BufferSender();
		Toolkit.getDefaultToolkit().addAWTEventListener(new Listener(sender), AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK );
		JFrame frame = new JFrame("Sender: Keep this windows open to send events");
		
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
		
		public Listener(BufferSender sender){
			try {
				this.sender = sender;
				sender.start();
			} catch (IOException e) {
				log.error("Error", e);
				throw new RuntimeException("Error", e);
			}
		}
		
        public void eventDispatched(AWTEvent event) {
            log.debug(event);
//            sender.send(obj);
        }
    }
}
