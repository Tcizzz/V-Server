
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.TextField;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class clientui extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	//private String pwd;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					clientui frame = new clientui();
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
	public class TextFieldHintListener implements FocusListener{
		
		private String hintText;
		private TextField textField;
		
		public TextFieldHintListener(TextField TextField,String hintText) {
			this.textField = TextField;
			this.hintText = hintText;
			TextField.setText(hintText);  //Ĭ��ֱ����ʾ

		}
	 
		@Override
		public void focusGained(FocusEvent e) {
			//��ȡ����ʱ�������ʾ����
			String temp = textField.getText();
			if(temp.equals(hintText)) {
				textField.setText("");
			}
		}
	 
		@Override
		public void focusLost(FocusEvent e) {
			//ʧȥ����ʱ��û���������ݣ���ʾ��ʾ����
			String temp = textField.getText();
			if(temp.equals("")) {
				textField.setText(hintText);
			}
		}
	 
	}
	
	
	
	
	
	
	public clientui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("ע��");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				JFrame frame1 = new JFrame("�´���");
				frame1.setLocation(100,50);//��������Ļ��λ��
				frame1.setSize(400,300);//				�����С
				JLabel lblNewLabel = new JLabel("�˺�");
				lblNewLabel.setBounds(91, 58, 58, 15);
				contentPane.add(lblNewLabel);
			
				JLabel lblNewLabel_1 = new JLabel("����");
				lblNewLabel_1.setBounds(91, 113, 58, 15);
				contentPane.add(lblNewLabel_1);
				
		
				/*
				textField = new JTextField();
				textField.setBounds(129, 55, 203, 18);
				contentPane.add(textField);
				textField.setColumns(10);*/
				
				TextField textField1 = new TextField();
				textField1 .setBounds(129, 55, 203, 18);
				textField1 .addFocusListener(new TextFieldHintListener(textField1, "�����볤��Ϊ9λ���˺�"));
				frame1.getContentPane().add(textField1);
				
				TextField textField2 = new TextField();
				textField2 .setBounds(132, 110, 200, 18);
				textField2 .addFocusListener(new TextFieldHintListener(textField2, "���볤��λ6-16λ���ֻ�Ӣ����ĸ"));
				frame1.getContentPane().add(textField2);
				
				
				JButton button1 = new JButton("ȡ��");
				button1.setBounds(70, 180, 90, 25);
				button1.setBorderPainted(false);
				button1.addActionListener(new ActionListener(){
					//������ťִ�еķ���
					public void actionPerformed(ActionEvent e) {
						//System.exit(0);
						frame1.setVisible(false);
					}
					
				});
				
				JButton button2 = new JButton("ע��");
				button2.setBounds(270, 180, 90, 25);
				button2.setBorderPainted(false);
				button2.addActionListener(new ActionListener(){
					//������ťִ�еķ���
					public void actionPerformed(ActionEvent e) {
						String account=textField1.getText().toString();
						String password=textField2.getText().toString();
						
						if(account.length()==9&&password.length()<16&&password.length()>6) {
							String data=null;
							if(password.length()>=10) {
								data=0+account+password.length()+password;
							}
							if(password.length()<10) {
								data=0+account+0+password.length()+password;
							}
							System.out.println("reginst"+data);
							String result=null;
							try {
								result=registersocket.register("172.20.10.3",55533,data,account);
								
							} catch (IOException e1) {// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							if(result.equals("1")) {
							
								data=1+data.substring(1,10)+33337+65537;
								try {
									result=registersocket.register("172.20.10.3",55533,data,account);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								if(result.equals("1")) {
									frame1.setVisible(false);
									JFrame frame = new JFrame("�´���");
									frame.setLocation(200,50);
									frame.setSize(100,100);
									JLabel lblNewLabe3 = new JLabel("ע��ɹ�");
									lblNewLabe3.setBounds(91, 58, 58, 15);
									contentPane.add(lblNewLabe3);	
									frame.add(lblNewLabe3);
									frame.setVisible(true);
								}
								
								else {
									textField1 .addFocusListener(new TextFieldHintListener(textField1, "�����볤��Ϊ9λ���˺�"));
									textField2 .addFocusListener(new TextFieldHintListener(textField2, "���볤��λ6-16λ���ֻ�Ӣ����ĸ"));
									JFrame frame = new JFrame("�´���");
									frame.setLocation(200,50);
									frame.setSize(100,100);
									JLabel lblNewLabe4 = new JLabel("ע��ʧ��");
									lblNewLabe4.setBounds(91, 58, 58, 15);
									contentPane.add(lblNewLabe4);
									frame.add(lblNewLabe4);
									frame.setVisible(true);
								}
							}
							else {
								textField1 .addFocusListener(new TextFieldHintListener(textField1, "�����볤��Ϊ9λ���˺�"));
								textField2 .addFocusListener(new TextFieldHintListener(textField2, "���볤��λ6-16λ���ֻ�Ӣ����ĸ"));
								JFrame frame = new JFrame("�´���");
								frame.setLocation(200,50);
								frame.setSize(100,100);
								JLabel lblNewLabe4 = new JLabel("ע��ʧ��");
								lblNewLabe4.setBounds(91, 58, 58, 15);
								contentPane.add(lblNewLabe4);
								frame.add(lblNewLabe4);
								frame.setVisible(true);
							}
						}
						else {
							textField1 .addFocusListener(new TextFieldHintListener(textField1, "�����볤��Ϊ9λ���˺�"));
							textField2 .addFocusListener(new TextFieldHintListener(textField2, "���볤��λ6-16λ���ֻ�Ӣ����ĸ"));
							JFrame frame = new JFrame("�´���");
							frame.setLocation(200,50);
							frame.setSize(100,100);
							JLabel lblNewLabe4 = new JLabel("ע��ʧ��");
							lblNewLabe4.setBounds(91, 58, 58, 15);
							contentPane.add(lblNewLabe4);
							frame.add(lblNewLabe4);
							frame.setVisible(true);
						}	
						
						System.out.println(account);
						System.out.println(password);
						
						
					}
					
				});
				
				Panel pan = new Panel();
				pan.setSize(100, 100);
				
				frame1.getContentPane().add(lblNewLabel);
				frame1.getContentPane().add(lblNewLabel_1);
				frame1.add(button1);
				frame1.add(button2);
				frame1.getContentPane().add(pan);
 
 
//				��ʾ����
				frame1.setVisible(true);
			    
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(79, 196, 97, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("��¼");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String account=textField.getText().toString();
				String password=textField_1.getText().toString();
				Long startTs = System.currentTimeMillis();
				String data=2+account+1+startTs;
				data="5"+account+"hello";
				
				String result=null;
				try {
					result=registersocket.register("172.20.10.2",55533,data,account);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(result.equals(0)) {
					System.out.println("shibai");
				}
				else {
					String Kctgs=result.substring(0,64);
					String IDtgs=result.substring(64,65);
					String Ts1=result.substring(65,78);
					String lifttime2=result.substring(78,91);
					
				}
				
				
				System.out.println(account);
				System.out.println(password);
				
			}
		});
		btnNewButton_1.setBounds(289, 196, 97, 23);
		contentPane.add(btnNewButton_1);
		
		
		
		
		JLabel lblNewLabel = new JLabel("�˺�");
		lblNewLabel.setBounds(91, 58, 58, 15);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("����");
		lblNewLabel_1.setBounds(91, 113, 58, 15);
		contentPane.add(lblNewLabel_1);
		
		textField = new JTextField();
		textField.setBounds(129, 55, 203, 18);
		contentPane.add(textField);
		textField.setColumns(10);
		
		//JPasswordField jPasswordField = new JPasswordField();
		//String pwd=jPasswordField.getPassword().toString().trim();
		textField_1 = new JTextField();
		textField_1.setBounds(132, 110, 200, 18);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
	}
}
