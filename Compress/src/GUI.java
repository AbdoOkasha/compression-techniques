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

import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Font;


public class GUI extends JFrame {

	private JPanel contentPane;
	private JTextField data;
	private String value="";
	public boolean compress_btn_state=false;

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

		JButton de_compress_btn=new JButton("decompress");
		JButton compress_btn = new JButton("append");
		JButton btnAdd = new JButton("rewrite");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
						    		de_compress_btn.setEnabled(true);
						    		if(data.getText().length()>=1) {
						    			compress_btn.setEnabled(true);
						    			btnAdd.setEnabled(true);
						    		}
						    		compress_btn_state=true;
						    	}
						    	
						    	else {
						    		de_compress_btn.setEnabled(false);
						    		compress_btn.setEnabled(false);
						    		btnAdd.setEnabled(false);
						    		compress_btn_state=false;
						    	}
						    }
						    else {
						    	de_compress_btn.setEnabled(false);
						    	compress_btn.setEnabled(false);
						    	btnAdd.setEnabled(false);
					    		compress_btn_state=false;
						    }
						}
							
					}
						
				});
		
		
		data = new JTextField();
		GridBagConstraints gbc_data = new GridBagConstraints();
		gbc_data.gridwidth = 8;
		gbc_data.insets = new Insets(0, 0, 5, 5);
		gbc_data.fill = GridBagConstraints.HORIZONTAL;
		gbc_data.gridx = 3;
		gbc_data.gridy = 2;
		panel.add(data, gbc_data);
		data.setColumns(10);
		data.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {

			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				value=data.getText();
				if(value.length()>=1) {
					if(compress_btn_state==true) {
						compress_btn.setEnabled(true);
						btnAdd.setEnabled(true);
					}
					else {
						compress_btn.setEnabled(false);
						btnAdd.setEnabled(false);
					}
				}
				else {
					compress_btn.setEnabled(false);
					btnAdd.setEnabled(false);
				}
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
			}
		});
		
		GridBagConstraints gbc_compress_btn = new GridBagConstraints();
		gbc_compress_btn.insets = new Insets(0, 0, 5, 5);
		gbc_compress_btn.gridx = 3;
		gbc_compress_btn.gridy = 3;
		compress_btn.setEnabled(false);
		panel.add(compress_btn, gbc_compress_btn);
		compress_btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource()==compress_btn) {
					String input=data.getText();
					data.setText(null);
					btnAdd.setEnabled(false);
					compress_btn.setEnabled(false);
					try {
						main.compress(input,browse.getText(),true);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
				
			}
		});
		
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnAdd.gridx = 10;
		gbc_btnAdd.gridy = 3;
		btnAdd.setEnabled(false);
		panel.add(btnAdd, gbc_btnAdd);
		btnAdd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0){
				if(arg0.getSource()==btnAdd) {
					String input=data.getText();
					data.setText(null);
					btnAdd.setEnabled(false);
					compress_btn.setEnabled(false);
					try {
						main.compress(input,browse.getText(),false);
					} catch (IOException e) {
						e.printStackTrace();
					}
				
			}
				
			}
		});
		
		
		JTextArea deComp_text = new JTextArea();
		deComp_text.setFont(new Font("Courier New", Font.BOLD | Font.ITALIC, 12));
		GridBagConstraints gbc_deComp_text = new GridBagConstraints();
		gbc_deComp_text.gridheight = 6;
		gbc_deComp_text.gridwidth = 8;
		gbc_deComp_text.insets = new Insets(0, 0, 0, 5);
		gbc_deComp_text.fill = GridBagConstraints.BOTH;
		gbc_deComp_text.gridx = 1;
		gbc_deComp_text.gridy = 5;
		deComp_text.setForeground(Color.BLACK);
		panel.add(deComp_text, gbc_deComp_text);
		
		GridBagConstraints gbc_de_compress_btn = new GridBagConstraints();
		gbc_de_compress_btn.insets = new Insets(0, 0, 5, 5);
		gbc_de_compress_btn.gridx = 10;
		gbc_de_compress_btn.gridy = 7;
		de_compress_btn.setEnabled(false);
		panel.add(de_compress_btn, gbc_de_compress_btn);
		de_compress_btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource()==de_compress_btn) {
					try {
						String result=main.decompress(browse.getText());
						deComp_text.setText(result);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}	
			}
		});
	}
}
