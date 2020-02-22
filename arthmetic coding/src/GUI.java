import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;

import javax.swing.JButton;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.math.BigDecimal;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Font;


public class GUI extends JFrame {

	private JPanel contentPane;
	private String value="";
	public boolean compressBtnState=false;
	public floatingArthmetic f= new floatingArthmetic();
	JButton deCompressBtn=new JButton("decompress");
	JTextArea inputArea = new JTextArea();
	JButton addData = new JButton("compress");
	JTextArea outText = new JTextArea();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
				
				JButton browse = new JButton("compression/de_compression path");
				GridBagConstraints gbc_browse = new GridBagConstraints();
				gbc_browse.gridwidth = 9;
				gbc_browse.insets = new Insets(0, 0, 5, 5);
				gbc_browse.gridx = 2;
				gbc_browse.gridy = 0;
				panel.add(browse, gbc_browse);
				
						
						browse.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent arg0) {
								if(browse == arg0.getSource()){
				
									
									FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
								    dialog.setMode(FileDialog.LOAD);
								    dialog.setVisible(true);
								    String file =null;
								    file= dialog.getDirectory();
								    file+=dialog.getFile();
								    
								    if(file!=null) {
								    	browse.setText(file);
								    	String compare =file.substring(file.length()-4,file.length()); 
								    	if(compare.equals(".txt")) {
								    		deCompressBtn.setEnabled(true);
								    		if(inputArea.getText().length()>=1) {
								    			addData.setEnabled(true);
								    		}
								    		compressBtnState=true;
								    	}
								    	
								    	else {
								    		deCompressBtn.setEnabled(false);
								    		addData.setEnabled(false);
								    		compressBtnState=false;
								    	}
								    }
								    else {
								    	deCompressBtn.setEnabled(false);
								    	addData.setEnabled(false);
							    		compressBtnState=false;
								    }
								}
									
							}
								
						});
		
				
				GridBagConstraints gbc_deCompressBtn = new GridBagConstraints();
				gbc_deCompressBtn.insets = new Insets(0, 0, 5, 5);
				gbc_deCompressBtn.gridx = 2;
				gbc_deCompressBtn.gridy = 3;
				deCompressBtn.setEnabled(false);
				panel.add(deCompressBtn, gbc_deCompressBtn);
				deCompressBtn.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if(arg0.getSource()==deCompressBtn) {
							try {
								String path= browse.getText();
								String out=f.decode(path);
								outText.setText(out);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}	
					}
				});
		
		GridBagConstraints gbc_addData = new GridBagConstraints();
		gbc_addData.insets = new Insets(0, 0, 5, 5);
		gbc_addData.gridx = 11;
		gbc_addData.gridy = 3;
		addData.setEnabled(false);
		panel.add(addData, gbc_addData);
		addData.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource()==addData) {
					addData.setEnabled(false);
					try {
						String path= browse.getText();
						f.writeToFile(path, inputArea.getText(), false);
						BigDecimal out=f.encode(path);
						outText.setText(out.toString());
					} catch (IOException e) {
						e.printStackTrace();
					}
					inputArea.setText("");
				}
				
			}
		});
		
		
		
		outText.setFont(new Font("Courier New", Font.BOLD | Font.ITALIC, 12));
		GridBagConstraints gbc_outText = new GridBagConstraints();
		gbc_outText.gridheight = 7;
		gbc_outText.gridwidth = 6;
		gbc_outText.insets = new Insets(0, 0, 0, 5);
		gbc_outText.fill = GridBagConstraints.BOTH;
		gbc_outText.gridx = 0;
		gbc_outText.gridy = 4;
		outText.setForeground(Color.BLACK);
		panel.add(outText, gbc_outText);
		
		GridBagConstraints gbc_inputArea = new GridBagConstraints();
		gbc_inputArea.gridheight = 7;
		gbc_inputArea.gridwidth = 7;
		gbc_inputArea.fill = GridBagConstraints.BOTH;
		gbc_inputArea.gridx = 7;
		gbc_inputArea.gridy = 4;
		panel.add(inputArea, gbc_inputArea);
		inputArea.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				if(inputArea.getText().length()>0) {
					addData.setEnabled(true);
				}
				else {
					addData.setEnabled(false);
				}
				
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(inputArea.getText().length()>0) {
					addData.setEnabled(true);
				}
				else {
					addData.setEnabled(false);
				}
			}
		});
	}
}
