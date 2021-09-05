import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

import java.util.Scanner;
import java.util.concurrent.*;
//model is separate from the view.

public class WordApp {
//shared variables
	static int noWords=4;
	static int totalWords;

   	static int frameX=1000;
	static int frameY=600;
	static int yLimit=480;
	static boolean gameStarted = false;

	static WordDictionary dict = new WordDictionary(); //use default dictionary, to read from file eventually

	static WordRecord[] words;
	static volatile boolean done;  //must be volatile
	static Score score;
	static Timer t;

	static WordPanel w;
	static JPanel g;

	static Thread[] threads;

	static JFrame frame = new JFrame("WordGame");
	
	
	
	public static void setupGUI(int frameX,int frameY,int yLimit) {
		// Frame init and dimensions
		score = new Score();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(frameX, frameY);
		g = new JPanel();
		g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS));
		g.setSize(frameX, frameY);

		w = new WordPanel(words, yLimit);
		w.setSize(frameX, yLimit + 100);
		g.add(w);

		JPanel txt = new JPanel();
		txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS));
		JLabel caught = new JLabel("Caught: " + score.getCaught() + "    ");
		JLabel missed = new JLabel("Missed:" + score.getMissed() + "    ");
		JLabel scr = new JLabel("Score:" + score.getScore() + "    ");
		txt.add(caught);
		txt.add(missed);
		txt.add(scr);

	   	final JTextField textEntry = new JTextField("", 20);
	   	textEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String text = textEntry.getText();
				for (int i = 0; i < words.length; i++) {
					if (words[i].matchWord(text)) {
						score.caughtWord(text.length());
					}
				}
				textEntry.setText("");
				textEntry.requestFocus();
			}
		});

	   txt.add(textEntry);
	   txt.setMaximumSize( txt.getPreferredSize());
	   g.add(txt);
	    
	   JPanel b = new JPanel();
       b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS)); 
	   JButton startB = new JButton("Start");
			// add the listener to the jbutton to handle the "pressed" event
		startB.addActionListener(new ActionListener()
		{
		   	public void actionPerformed(ActionEvent e) {
				gameStarted = true;
				w.startPanel();
				t = new Timer(1, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						caught.setText("Caught: " + score.getCaught() + "    ");
						scr.setText("Score:" + score.getScore() + "    ");
						missed.setText("Missed:" + score.getMissed() + "    ");
						if (score.getTotal() >= totalWords) {
							resetup();
						}
					}
				});
				t.start();
				textEntry.requestFocus(); // return focus to the text entry field
			}
		});
		JButton endB = new JButton("End");
			
				// add the listener to the jbutton to handle the "pressed" event
		endB.addActionListener(new ActionListener()
		{
		   public void actionPerformed(ActionEvent e)
		   {
			   if(gameStarted){
				gameStarted = false;
		    	w.stopGame();
			   }
			   else{
				   JOptionPane.showMessageDialog(null, "No Game Started");
			   }
		   }
		});
		JButton quitB = new JButton("Quit");
			
				// add the listener to the jbutton to handle the "pressed" event
		quitB.addActionListener(new ActionListener()
		{
		   public void actionPerformed(ActionEvent e)
		   {
			   	System.exit(0);
		   }
		});
		
		b.add(startB);
		b.add(endB);
		b.add(quitB);
		
		g.add(b);
    	
      frame.setLocationRelativeTo(null);  // Center window on screen.
      frame.add(g); //add contents to window
      frame.setContentPane(g);
      frame.setVisible(true);
	}

	public static void resetup() {
		t.stop();
		int tempScore = score.getScore();
		int tempMissed = score.getMissed();
		int tempCaught = score.getCaught();
		score.resetScore();
		w.stopGame();
		for (int i = 0; i < words.length; i++) {
			words[i].resetWord();
		}
		JOptionPane.showMessageDialog(null,
				"Score: " + tempScore + "\nCaught:" + tempCaught + "\nMissed:" + tempMissed,
				"Game Over", JOptionPane.DEFAULT_OPTION);
	}

   public static String[] getDictFromFile(String filename) {
		String [] dictStr = null;
		try {
			Scanner dictReader = new Scanner(new File(filename));//"C:/Users/camer/Desktop/Uni/2nd Year/2nd Semester/CSC2002S/Projects/Assignment2/Assignment-2/src/example_dict.txt"));
			int dictLength = dictReader.nextInt();
			dictStr=new String[dictLength];
			for (int i=0;i<dictLength;i++) {
				dictStr[i]= dictReader.next();
			}
			dictReader.close();
		} 
		catch (IOException e) {
	    	System.err.println("Problem reading file " + filename + " default dictionary will be used");
	    }
		return dictStr;
	}

	
	public static void main(String[] args) {
		//deal with command line arguments
		totalWords=Integer.parseInt(args[0]);  //total words to fall
		noWords=Integer.parseInt(args[1]); // total words falling at any point
		assert(totalWords>=noWords); // this could be done more neatly
		String[] tmpDict=getDictFromFile(args[2]);
		if (tmpDict!=null)
			dict= new WordDictionary(tmpDict);
		
		WordRecord.dict=dict; //set the class dictionary for the words.
		
		words = new WordRecord[noWords];  //shared array of current words
		threads = new Thread[words.length];
		setupGUI(frameX, frameY, yLimit);  
    	//Start WordPanel thread - for redrawing animation

		int x_inc=(int)frameX/noWords;
	  	//initialize shared array of current words

		for (int i=0;i<noWords;i++) {
			words[i]=new WordRecord(dict.getNewWord(), i * x_inc, yLimit, score);
		}
	}
}