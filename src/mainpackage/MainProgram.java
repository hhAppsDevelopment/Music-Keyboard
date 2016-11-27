 package mainpackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.MidiSystem;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainProgram {

	private JFrame frame;
	private JPanel bgPanel;
	
	private JPanel keyboardPanel;
	
	private JPanel recordPanel;
	private JButton recordButton;
	private JButton stopButton;
	private JButton playButton;
	private JButton saveButton;
	private JButton openButton;
	private JButton setChooseButton;
	private JLabel timerLabel;
	private JComboBox instListCombo;
	private ButtonGroup mode;
	private JRadioButton alphabetical;
	private JRadioButton solmizational;
	private JComboBox jcb;
	
	private JPanel inputPanel;
	private JTextField first;
	private JTextField second;
	
	private MusicController mc;
	private StorageManager sm;
	
	private String letters = "CCDDEFFGGAAH";
	private String[][] instList;
	
	private Timer timer;
	private int interval;
	private int defaultDelay = 25;
	private int x = 36;
	private int y = 95;
	
	
	public static void main(String[] args){
		MainProgram mp = new MainProgram();
		
		mp.start();
	}
	
	public void setupGui(String[] instruments){
		
		frame = new JFrame("Music Keyboard");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
		bgPanel = new JPanel();
		keyboardPanel = new JPanel();
		recordPanel = new JPanel();
		inputPanel = new JPanel();
		
		bgPanel.setLayout(new BoxLayout(bgPanel,BoxLayout.PAGE_AXIS));
		bgPanel.add(keyboardPanel);
		bgPanel.add(recordPanel);
		frame.setContentPane(bgPanel);
		
		keyboardPanel.setLayout(null);
		
		recordPanel.setLayout(null);
		
		inputPanel.setLayout(new GridLayout(0,3));
		
		inputPanel.add(new JLabel("Accidental:"));
		
		String[] number = new String[15];
		for (int i = 7; i>0; i--){
			number[i+7] = i+"#";
			number[7-i] = i+"♭";
		}
		number[7] = "0";
		
		jcb = new JComboBox<String>(number);
		jcb.setSelectedIndex(7);
		inputPanel.add(jcb);
		
		setChooseButton = new JButton("Set");
		setChooseButton.addActionListener(new SetChooseListener());
		inputPanel.add(setChooseButton);
		
		recordButton = new JButton("Record");
		recordButton.addActionListener(new RecordListener());
		
		stopButton = new JButton("Stop");
		stopButton.addActionListener(new StopListener());
		
		playButton = new JButton("Play");
		playButton.addActionListener(new PlayListener());
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(new SaveListener());
		
		openButton = new JButton("Open");
		openButton.addActionListener(new OpenListener());
		
		timerLabel = new JLabel(" 0 s ");
		
		setChooseButton = new JButton("Choose set");
		setChooseButton.addActionListener(new SetChooseListener());
		
		instListCombo = new JComboBox(instruments);
		instListCombo.addActionListener(new InstListListener());
		
		recordButton.setSize(90,30);
		recordButton.setLocation(15,50);
		stopButton.setSize(90,30);
		stopButton.setLocation(110,50);
		playButton.setSize(90,30);
		playButton.setLocation(205,50);
		saveButton.setSize(90,30);
		saveButton.setLocation(300,50);
		openButton.setSize(90,30);
		openButton.setLocation(395,50);
		timerLabel.setSize(90,30);
		timerLabel.setLocation(490,50);
		instListCombo.setSize(110,30);
		instListCombo.setLocation(585,50);
		inputPanel.setSize(250,30);
		inputPanel.setLocation(810,50);
		
		recordPanel.add(recordButton);
		recordPanel.add(stopButton);
		recordPanel.add(playButton);
		recordPanel.add(saveButton);
		recordPanel.add(openButton);
		recordPanel.add(timerLabel);
		recordPanel.add(inputPanel);
		recordPanel.add(instListCombo);
		
		frame.setSize(1210,300);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-(frame.getSize().width/2), 0);
		
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	public String[] getInstNames(){
		
		instList = mc.getInstrumentList();
		String[] inst = new String[instList.length];
		for(int i = 0;i<instList.length;i++){
			inst[i] = instList[i][0];
			
		}
		
		return validateInstNames(inst, 0);
		
	}
		
	public String[] validateInstNames(String[] inst, int f){
		String s = inst[f];
		
		for(int i = 0; i<f;i++){
			if (s.equals(inst[i])){
				
				for(int j = f;j < inst.length-1; j++){
					inst[j] = inst[j+1];
				}
				inst[inst.length-1] = "Default";
				
			}
		}
		if(f+1<inst.length){
			inst = validateInstNames(inst,f+1);
		}
		
		return inst;		
		
	}
	
	
	public void start(){
		
		
		mc = new MusicController();
		sm = new StorageManager();
		
		sm.initializeSolmizationFile();
		
		mc.setInstrument(1);
		mc.setDelay(25);
		
		setupGui(getInstNames());
		changeSet();
	}
	
	public void timer(){
		interval = 0;
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask(){
			public void run() {
				interval++;
				timerLabel.setText(" " + Math.round(interval/25)+" s ");
			}
		}, 40, 40);
	}
		
	public void setupKeyboard(String[] solm){
		
		keyboardPanel.removeAll();
		for (int i=x;i<=y;i++){
			int k = 0;
			String accidental = "";
				if(i != x && jcb.getSelectedIndex() >= 7 && letters.charAt(i%letters.length())==letters.charAt((i-1)%letters.length())) {
					accidental = "#";
					k = 1;
				}
				else if(i != y && jcb.getSelectedIndex() < 7 && letters.charAt(i%letters.length())==letters.charAt((i+1)%letters.length())){
					accidental = "♭";
					k = 1;
				}
			PianoButton but = new PianoButton(i,Character.toString(letters.charAt(i%letters.length())),k,solm[(i-x)%12], accidental);
			but.addMouseListener(new ButtonListener());
			keyboardPanel.add(but);
			but.setLocation((i-x)*but.getWidth(), 0);
			frame.revalidate();
			frame.repaint();
		}
	}
	
	public class ButtonListener implements MouseListener{

		public void mouseClicked(MouseEvent e) {

		}

		public void mousePressed(MouseEvent e) {
			
			((PianoButton)e.getSource()).setBorder(BorderFactory.createLineBorder(Color.white));
			
			PianoButton pb = (PianoButton) e.getSource();
			mc.addNote(mc.getInstrument(), pb.getNote(), interval);

		}

		public void mouseReleased(MouseEvent e) {
			((PianoButton)e.getSource()).setBorder(BorderFactory.createLineBorder(Color.black));
		}

		public void mouseEntered(MouseEvent e) {
			
		}

		public void mouseExited(MouseEvent e) {
			
		}
			
			
		
	}
	
	public class RecordListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			mc.record();
			timer();
		}
		
	}
	
	public class PlayListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			mc.play(mc.getSequence());
		}
		
	}
	
	public class StopListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			if(timer!=null) timer.cancel();
			if(mc.getMode() == 1){
				mc.setTrackLength(interval);
			}
			mc.stop();
		}
		
	}
	
	public class SaveListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			
			JFileChooser chooser = new JFileChooser();
			int i = chooser.showSaveDialog(null);
			
			if (i == JFileChooser.APPROVE_OPTION){
				try {
					File f = new File(chooser.getSelectedFile().getAbsolutePath()+".music");
					MidiSystem.write(mc.getSequence(), MidiSystem.getMidiFileTypes()[0] ,f);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		}
		
	}
	
	public class OpenListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Compatible files (.music)","music");
			chooser.setFileFilter(filter);
			int i = chooser.showOpenDialog(null);
			if (i == JFileChooser.APPROVE_OPTION){
				try {
					mc.setSequence(MidiSystem.getSequence(chooser.getSelectedFile()));
					mc.play(mc.getSequence());
				} catch(Exception ex){
					ex.printStackTrace();
				}
			}
						
		}
		
	}
	
	public class SetChooseListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			
			MainProgram.this.changeSet();
				
		}
		
	}
	
	public class InstListListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			
			int k = 0;
			for (int i = 0;i<instList.length;i++){
				if(((String)((JComboBox)e.getSource()).getSelectedItem()).equals(instList[i][0])) k=i;
			}
			mc.setInstrument(Integer.parseInt(instList[k][1]));
			
		}
		
	}
	
	public void changeSet(){
		
		int selection = jcb.getSelectedIndex();
		if(selection < 7){
			letters = "CDDEEFGGAAHH";
		}else{
			letters = "CCDDEFFGGAAH"; 
		}
		
		selection = (1+selection*5)%12; 
		String[] solm = new String[12];
		for(int i = 0; i < 12; i++){
			solm[i] = sm.getSolmization(((selection+i)%12)*3);					
		}
		setupKeyboard(solm);
		
	}



	
}
