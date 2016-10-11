package irc;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;

import jvn.JvnException;
import jvn.JvnObject;
import jvn.JvnServerImpl;

public class IrcProxy {
	public TextArea		text;
	public TextField	data;
	Frame 			frame;
	SentenceInterface sentenceProxy;
	public static void main(String argv[]) {
		   try {
			// initialize JVN
				JvnServerImpl js = JvnServerImpl.jvnGetServer();
				SentenceInterface jo = (SentenceInterface)ProxyInvocationHandler.newInstProxy(new Sentence());
				// create the graphical part of the Chat application
				 new IrcProxy(jo);
			   
			   } catch (Exception e) {
				   System.out.println("IRC problem : " + e.getMessage());
			   }
			}

		  /**
		   * IrcProxy Constructor
		   @param jo the JVN object representing the Chat
		   **/
	 /**
	   * IRC Constructor
	   @param jo the JVN object representing the Chat
	   **/
		public IrcProxy(SentenceInterface jo) {
			sentenceProxy = jo;
			frame=new Frame();
			frame.setLayout(new GridLayout(1,1));
			text=new TextArea(10,60);
			text.setEditable(false);
			text.setForeground(Color.red);
			frame.add(text);
			data=new TextField(40);
			frame.add(data);
			Button read_button = new Button("read");
			read_button.addActionListener(new readListenerProxy(this));
			frame.add(read_button);
			Button write_button = new Button("write");
			write_button.addActionListener(new writeListenerProxy(this));
			frame.add(write_button);
			frame.setSize(545,201);
			frame.addWindowListener(new WindowAdapter()
	        {
	            @Override
	            public void windowClosing(WindowEvent e)
	            {
	                	try {
							JvnServerImpl.jvnGetServer().jvnTerminate();
							System.out.println("Serveur supprimé");
						} catch (JvnException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	               
	                
	                e.getWindow().dispose();
	            }
	        });
			text.setBackground(Color.black); 
			frame.setVisible(true);
		
		}
	}
/**
 * Internal class to manage user events (read) on the CHAT application
 **/
class readListenerProxy implements ActionListener {
	IrcProxy irc;
 
	public readListenerProxy (IrcProxy i) {
		irc = i;
	}
  
/**
 * Management of user events
 **/
	public void actionPerformed (ActionEvent e) {
	

		
		// invoke the method
		String s = irc.sentenceProxy.read();
		
	
		
		// display the read value
		irc.data.setText(s);
		irc.text.append(s+"\n");
	 
	}

}

/**
* Internal class to manage user events (write) on the CHAT application
**/
class writeListenerProxy implements ActionListener {
	IrcProxy irc;

	public writeListenerProxy (IrcProxy i) {
      	irc = i;
	}

/**
  * Management of user events
 **/
	public void actionPerformed (ActionEvent e) {
	
		// get the value to be written from the buffer
  String s = irc.data.getText();
      	
 
		
		// invoke the method
		irc.sentenceProxy.write(s);
		
	
	 
	}
}


