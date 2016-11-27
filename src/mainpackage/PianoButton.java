package mainpackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PianoButton extends JPanel{

	private int note;
	private JLabel text,text2;
	
	public PianoButton(int i,String s,int k,String s2,String s3){
		note = i;
		text = new JLabel(s);
		text.setAlignmentY(CENTER_ALIGNMENT);
		text2 = new JLabel(s2);
		text2.setAlignmentY(CENTER_ALIGNMENT);
		
		this.add(text);
		this.add(text2);

		text2.setFont(new Font(text2.getFont().getName(),Font.ITALIC,10));
		
		if(k==0){
			this.setBackground(Color.WHITE);
			this.setSize(20,100);
		}else{
			this.setBackground(Color.BLACK);
			this.setSize(20, 80);
			text.setForeground(Color.WHITE);
			text2.setForeground(Color.WHITE);
			text.setText(text.getText()+s3);
		}
		this.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	
	public int getNote(){
		return note;
	}
	
	
}
