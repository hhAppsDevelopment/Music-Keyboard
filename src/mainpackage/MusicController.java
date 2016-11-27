package mainpackage;

import javax.sound.midi.*;

public class MusicController {

	private Sequencer sequencer;
	private Sequence sequence;
	private Track longTrack;
	private int instrument;
	private int trackLength;
	private int mode; // 0:default 1:record 2:play
	private int delay;
	
	public MusicController(){
		try {
			
			sequencer = MidiSystem.getSequencer();
			sequence = new Sequence(Sequence.SMPTE_25,1);
			sequencer.open();
			
			
		} catch (Exception e) {

			e.printStackTrace();
			
		}
		trackLength = 0;
		mode = 0;

	}
	
	public void record(){
		
		try {
			
			sequencer = MidiSystem.getSequencer();
			sequence = new Sequence(Sequence.SMPTE_25,1);
			sequencer.open();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		longTrack = sequence.createTrack();
		trackLength = 0;
		mode = 1;
	}
	
	public void playOne(int instrument,int note){

		try {
			
			Sequence oneSequence = new Sequence(Sequence.SMPTE_25,1);
			Track oneTrack = oneSequence.createTrack();
			
			MidiEvent event = null;
			
			ShortMessage instrM = new ShortMessage();
			instrM.setMessage(ShortMessage.PROGRAM_CHANGE,0,instrument,0);
			MidiEvent instrEvent = new MidiEvent(instrM,1);
			oneTrack.add(instrEvent);
			
			ShortMessage onMessage = new ShortMessage();
			onMessage.setMessage(144,0,note,100);
			MidiEvent onEvent = new MidiEvent(onMessage,1);
			oneTrack.add(onEvent);
			
			ShortMessage offMessage = new ShortMessage();
			offMessage.setMessage(128,0,note,100);
			MidiEvent offEvent = new MidiEvent(offMessage,delay);
			oneTrack.add(offEvent);
			
			sequencer.setSequence(oneSequence);
			sequencer.start();
			
		} catch (Exception e) {
			
			e.printStackTrace();

		}
		
		
	}
	
	public void addNote(int instrument,int note,int tick){
		
		try{
			
			playOne(instrument,note);
			if(mode == 1){
				MidiEvent event = null;
				
				ShortMessage instrM = new ShortMessage();
				instrM.setMessage(ShortMessage.PROGRAM_CHANGE,0,instrument,0);
				MidiEvent instrEvent = new MidiEvent(instrM,tick);
				longTrack.add(instrEvent);
				
				ShortMessage onMessage = new ShortMessage();
				onMessage.setMessage(144,0,note,100);
				MidiEvent onEvent = new MidiEvent(onMessage,tick);
				longTrack.add(onEvent);
				
				ShortMessage offMessage = new ShortMessage();
				offMessage.setMessage(128,0,note,100);
				MidiEvent offEvent = new MidiEvent(offMessage,tick+delay);
				longTrack.add(offEvent);
			}
			
			
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
		
	}
	
	public void play(Sequence seq){
		
		
		try {
				
			sequencer.setTickPosition(1);
			sequencer.setSequence(seq);
			sequencer.start();
				
			mode = 2;
				
		} catch (Exception e) {
				e.printStackTrace();
		}
			
	}
	
	public void setInstrument(int i){
		instrument = i;
	}
	
	public int getInstrument(){
		return instrument;
	}
	
	public void setTrackLength(int i){
		trackLength = i;
	}
	
	public int getTrackLength(){
		return trackLength;
	}
	
	public int getMode(){
		return mode;
	}
	
	public void stop(){
		
		if(mode == 2){
			sequencer.stop();
		}
		mode = 0;
		
	}
	
	public void setDelay(int i){
		delay = i;
	}
	
	public Sequence getSequence(){
		return sequence;
	}
	
	public void setSequence(Sequence seq){
		sequence = seq;
	}
	
	public String[][] getInstrumentList(){
		
		try {
			
			Synthesizer s = MidiSystem.getSynthesizer();
			Instrument[] inst = s.getAvailableInstruments();
			String[][] instList = new String[inst.length][2];
			
			for(int i = 0; i<inst.length; i++){

				instList[i][0] = inst[i].getName();
				instList[i][1] = inst[i].getPatch().getProgram() + "";
				
			}
			return instList;

			
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
			return null;
		}
		
		
		
	}
	
}
