

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;


public class WordPanel extends JPanel implements Runnable, ActionListener {
		public static volatile boolean done;
		private WordRecord[] words;
		private int noWords;
		private int maxY;
		// private int x=0;
		// private int velX=2;
		//Timer tm = new Timer(5,this);
		private Thread thread;

		
		public void paintComponent(Graphics g) {
		    int width = getWidth();
		    int height = getHeight();
		    g.clearRect(0,0,width,height);
		    g.setColor(Color.red);
		    g.fillRect(0,maxY-10,width,height);

		    g.setColor(Color.black);
		    g.setFont(new Font("Helvetica", Font.PLAIN, 26));
		   //draw the words
		   //animation must be added 
		    for (int i=0;i<noWords;i++){	    	
		    	//g.drawString(words[i].getWord(),words[i].getX(),words[i].getY());	
		    	g.drawString(words[i].getWord(),words[i].getX(),words[i].getY()+20);  //y-offset for skeleton so that you can see the words	
		    }
		   
		  }

		  public void actionPerformed(ActionEvent e) {

			//x = x +velX;
		  }
		
		WordPanel(WordRecord[] words, int maxY) {
			this.words=words; //will this work?
			noWords = words.length;
			done=false;
			this.maxY=maxY;		
		}
		
		public void run() {
		//add in code to animate this
		



		}

		public synchronized void start(){
			thread = new Thread(this);
			thread.start();
		}
		public synchronized void stop(){
			try
			{
				thread.join();
			}
			catch(InterruptedException e)
			{
				System.out.println("interrupted "+e);
			}
		}

	}


