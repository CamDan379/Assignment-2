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
		private int y=0;
		// private int velX=2;
		private Score scr = new Score();
		Timer tm = new Timer(5,this);
		private Thread[] threads;
		private Thread mainThread;

		
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
				//int temp = words[i].getY()+20+(int)((y*(words[i].getSpeed()/1500)));
		    	g.drawString(words[i].getWord(),words[i].getX(),words[i].getY());
				//threads[i].start();
		    }
			tm.start();
		   
		  }

		  public void actionPerformed(ActionEvent e) {
			// if(y>500){
			// 	this.setVisible(false);
			// 	scr.missedWord();
			// 	return;
			// }
			// y += 2;
			repaint();
		  }
		
		WordPanel(WordRecord[] words, int maxY) {
			this.words=words; //will this work?
			noWords = words.length;
			done=false;
			this.maxY=maxY;
			threads = new Thread[words.length];
			for(int i = 0; i < words.length; i++){
				threads[i] = new Thread(words[i]);
				threads[i].start();
			}
		}
		
		public void run() {
		//add in code to animate this
			System.out.println("test");
			javax.swing.Timer t = new Timer(5, this);
			tm.start();

		}

		public synchronized void startPanel(){
			mainThread = new Thread(this);
			mainThread.start();
			for (int i = 0; i < words.length; i++) {
				threads[i] = new Thread(words[i]);
				threads[i].start();
			}
		}
		public synchronized void stop(){
			try
			{
				for(int i = 0; i < threads.length; i++){
					threads[i].interrupt();
				}
				mainThread.join();
			}
			catch(InterruptedException e)
			{
				System.out.println("interrupted "+e);
			}
		}

	}


