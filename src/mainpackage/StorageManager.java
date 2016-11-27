package mainpackage;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

public class StorageManager {
	
	private File f;
	private RandomAccessFile raf;
	int k;	
	
	public String out = "do di re ri mi fa fi solsi la li ti";
	
	
	public void initializeSolmizationFile(){
		
		try {
			f = new File("solmizations.solm");
			f.delete();
			f.createNewFile();
			raf = new RandomAccessFile(f, "rwd");			
			
			for(int i = 0; i<out.length(); i++){
				raf.seek(i*2);
				raf.writeChar(out.toCharArray()[i]);
			}
						
			
		} catch (Exception e) {			
		}
		
	}

	public String getSolmization(int i) {
		
		String solm = "";
		try {
								
			for(int j = 0; j<3; j++){
				raf.seek((i+j)*2);
				char c = raf.readChar();
				if(c!=' ')solm = solm + c;
			}
			
		} catch (Exception e) {
		}
		return solm;
	}
	

	
	
	

}
