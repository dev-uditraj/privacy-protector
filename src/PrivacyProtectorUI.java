import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.apache.derby.drda.NetworkServerControl;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;
import java.sql.*;
import javax.crypto.Cipher;

public class PrivacyProtectorUI implements ActionListener {
	Connection con;
	Statement st;
	ResultSet rs;
	private String user1;
	public static final String DRIVER= "org.apache.derby.jdbc.EmbeddedDriver";
	public static final String JDBC_URL="jdbc:derby://localhost:1527/privacydb;create=true";
	public static final String JDBC_URLS ="jdbc:derby://localhost:1527/privacydb;shutdown=true";
	JFrame f = new JFrame();
	JPanel p = new JPanel();
	Dimension big = new Dimension(1000,750);
	Dimension small = new Dimension(500,400);
	JLabel l1,l2,l3,l4,l5,Title;
	JButton b1,b2,b3,b4,b5;
	JTextField tf1,tf2,tf3,searchfield;
	JPasswordField pf1,pf2;
	Font syswide = new Font("Arial",Font.ITALIC,20);
	Font smlsyswide = new Font("Arial",Font.PLAIN,17);
	FileDialog fc = new FileDialog(f);
	JComboBox<String> cb1,cb2;
	JTabbedPane tp = new JTabbedPane();
	DefaultTableModel model1;
	DefaultTableModel model2;
	DefaultTableModel model3;
	int thistab;
	JTable files ,files1,files2;
	
	PrivacyProtectorUI(String s)
	{
		try {
			NetworkServerControl server = new NetworkServerControl(InetAddress.getByName("localhost"),1527);
			server.start(null);
			con = DriverManager.getConnection(JDBC_URL);
			st = con.createStatement();
			} catch (Exception e) {	}
		try {st.executeQuery("Select * from login_Table");} catch(Exception e){ createTable(); }
		
		//Closing database on windows exit
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if(b1.getText()!="Login")
				{
					clearAll();
					loadLoginUI();
				}
				else
				{
					try {
						st.close();
						con.close();
						con = DriverManager.getConnection(JDBC_URLS);
						}
						catch(Exception e){}
					f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				}
			}
		});
		//Closing database on windows exit
		fc.setMode(FileDialog.LOAD);
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		p.setBackground(Color.WHITE);
		f.add(p);
		p.setLayout(null);
		l1 = new JLabel();
		l2 = new JLabel();
		l3 = new JLabel();
		l4 = new JLabel();
		l5 = new JLabel();
		Title = new JLabel();
		b1 = new JButton();
		b2 = new JButton();
		b3 = new JButton();
		b4 = new JButton();
		b5 = new JButton();
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		b4.addActionListener(this);
		b5.addActionListener(this);
		b5.setBackground(Color.WHITE);
		tf1 = new JTextField();
		tf2 = new JTextField();
		tf3 = new JTextField();
		pf1 = new JPasswordField();
		pf2 = new JPasswordField();
		f.setLocationByPlatform(true);
		if(s.equals("Login")){loadLoginUI();}
	}
	
	void clearField()
	{
		tf1.setText("");
		tf2.setText("");
		tf3.setText("");
		pf1.setText("");
		pf2.setText("");
	}
	
	void clearAll()
	{
		enableAll();
		clearField();
		p.removeAll();
		p.revalidate();
		p.repaint();
	}
	
	void enableAll()
	{
	tf1.setEditable(true);
	tf2.setEditable(true);
	tf3.setEditable(true);
	pf1.setEditable(true);
	pf2.setEditable(true);
	}
	
	void loadAscendingOrder()
	{
		try { //loading data of password manager for user
			ResultSet rs1 = st.executeQuery("select title, username, password from passwordvault_Table where fk_username ='"+user1+"' order by title ASC");
			int count=0;;
			model3.setRowCount(0);
			while(rs1.next())
				{	count++;
					String titlename = rs1.getString(1);
					String usernamename = rs1.getString(2);
					String passwordname = rs1.getString(3);
					model3.addRow(new Object[] {count,titlename,usernamename,passwordname}); }
			}catch(Exception e1) { }
			
			try { //loading data of encryptor for user
			ResultSet rs1 =st.executeQuery("select filename,log,status from storage_Table where fk_username = '"+user1+"' order by filename ASC");
			int count=0;
			model1.setRowCount(0);
			while(rs1.next())
			{
				count++;
				String filename = rs1.getString(1);
				Timestamp timestamp =rs1.getTimestamp(2);
				String datetime = timestamp.toString();
				String status = rs1.getString(3);
				model1.addRow(new Object[] {count,filename,datetime,status});
			}
			}catch(Exception e1) { }
	}
	
	void loadDescendingOrder()
	{
		try { //loading data of password manager for user
			ResultSet rs1 = st.executeQuery("select title, username, password from passwordvault_Table where fk_username ='"+user1+"' order by title DESC");
			int count=0;;
			model3.setRowCount(0);
			while(rs1.next())
				{	count++;
					String titlename = rs1.getString(1);
					String usernamename = rs1.getString(2);
					String passwordname = rs1.getString(3);
					model3.addRow(new Object[] {count,titlename,usernamename,passwordname}); }
			}catch(Exception e1) { }
			
			try { //loading data of encryptor for user
			ResultSet rs1 =st.executeQuery("select filename,log,status from storage_Table where fk_username = '"+user1+"' order by filename DESC");
			int count=0;
			model1.setRowCount(0);
			while(rs1.next())
			{
				count++;
				String filename = rs1.getString(1);
				Timestamp timestamp =rs1.getTimestamp(2);
				String datetime = timestamp.toString();
				String status = rs1.getString(3);
				model1.addRow(new Object[] {count,filename,datetime,status});
			}
			}catch(Exception e1) { }
	}
	
	void loadRecentOrder()
	{
		try { //loading data of password manager for user
			ResultSet rs1 = st.executeQuery("select title, username, password from passwordvault_Table where fk_username ='"+user1+"' order by log DESC");
			int count=0;;
			model3.setRowCount(0);
			while(rs1.next())
				{	count++;
					String titlename = rs1.getString(1);
					String usernamename = rs1.getString(2);
					String passwordname = rs1.getString(3);
					model3.addRow(new Object[] {count,titlename,usernamename,passwordname}); }
			}catch(Exception e1) { }
			
			try { //loading data of encryptor for user
			ResultSet rs1 =st.executeQuery("select filename,log,status from storage_Table where fk_username = '"+user1+"' order by log DESC");
			int count=0;
			model1.setRowCount(0);
			while(rs1.next())
			{
				count++;
				String filename = rs1.getString(1);
				Timestamp timestamp =rs1.getTimestamp(2);
				String datetime = timestamp.toString();
				String status = rs1.getString(3);
				model1.addRow(new Object[] {count,filename,datetime,status});
			}
			}catch(Exception e1) { }
	}
		
	void loadUpdateTable()
	{
		if(cb2.getSelectedItem()=="Ascending Order")
		{
			loadAscendingOrder();
		}
		
		if(cb2.getSelectedItem()=="Descending Order")
		{
			loadDescendingOrder();
		}
		
		if(cb2.getSelectedItem()=="Recent Item Order")
		{
			loadRecentOrder();
		}
	}
	
	void createTable()  //database table definations
	{
		try {
		st.execute("create table login_Table (userid int primary key GENERATED ALWAYS AS IDENTITY (START with 1000 INCREMENT by 1),"
				+ "username varchar(50) unique,"
				+ "password varchar(50),"
				+ "name varchar(50),"
				+ "email varchar(50),"
				+ "security_q varchar(200),"
				+ "security_a varchar(100))"); 
		
		st.execute("create table storage_Table (fileid int primary key GENERATED ALWAYS AS IDENTITY (START with 1 INCREMENT by 1),"
				+ "filename varchar(200),"
				+ "path varchar(1000),"
				+ "status varchar(1000) default 'encrypted',"
				+ "log timestamp default current_timestamp,"
				+ "fk_username varchar(50) references login_Table(username))"); 
		
		st.execute("create table passwordvault_Table (fk_username varchar(50) references login_Table(username),"
				+ "title varchar(50),"
				+ "username varchar(50),"
				+ "password varchar(50),"
				+ "log timestamp default current_timestamp)"); 
		
//		st.execute("create table hiddenfile_Table (fk_username varchar(50) references login_Table(username),"
//				+ "filename varchar(50),"
//				+ "path varchar(1000),"
//				+ "log timestamp default current_timestamp)"); i will implement it later (comming feature)
		
		}catch(Exception e) { }
	}
	
	//Start of Login Interface
	void loadLoginUI()
	{
		f.setTitle("Login : Privacy Protector");
		l1.setText("Username");
		l1.setFont(smlsyswide);
		l2.setText("Password");
		l2.setFont(smlsyswide);
		Title.setText("Please enter your credentials");
		b1.setText("Login");
		b2.setText("Clear");
		b3.setText("Forgot Password");
		b4.setText("New User Registration");
		l1.setBounds(70,80,100,30);
		l2.setBounds(70,120,100,30);
		tf1.setBounds(200, 80, 200, 30);
		pf1.setBounds(200, 120, 200, 30);
		b1.setBounds(30,200,140,30);
		b2.setBounds(180,200,140,30);
		b3.setBounds(330,200,140,30);
		b4.setBounds(130,250,250,30);
		b1.setBackground(Color.WHITE);
		b2.setBackground(Color.WHITE);
		b3.setBackground(Color.WHITE);
		b4.setBackground(Color.WHITE);
		Title.setBounds(20,10,300,30);
		Title.setFont(syswide);
		p.add(Title);
		p.add(l1);
		p.add(l2);
		p.add(tf1);
		p.add(pf1);
		p.add(b1);
		p.add(b2);
		p.add(b3);
		p.add(b4);
		f.setSize(small);
		f.setVisible(true);
		f.setResizable(false);
	}
	//End of Login Interface
	
	//Start of PasswordReset interface 
	void loadResetPasswordUI()
	{
		f.setTitle("Reset Password : Privacy Protector");
		f.setSize(small);
		Title.setText("Enter details to reset password:");
		l1.setText("Username");
		l2.setText("Security Question");
		l5.setText("Answer");
		l4.setText("New Password");
		l3.setText("Re-enter Password");
		l5.setFont(smlsyswide);
		l4.setFont(smlsyswide);
		l3.setFont(smlsyswide);
		b1.setText("Check");
		b2.setText("Reset");
		b3.setText("Clear");
		Title.setBounds(20,10,300,30);
		l1.setBounds(40,60,150,30);
		l2.setBounds(40,100,150,30);
		l5.setBounds(40,140,150,30);
		l3.setBounds(40,220,150,30);
		l4.setBounds(40,260,150,30);
		tf1.setBounds(200,60,200,30);
		tf2.setBounds(200,100,200,30);
		tf2.setEditable(false);
		tf3.setEditable(false);
		tf3.setBounds(200,140,200,30);
		b1.setBounds(400,60,80,30); //check button
		pf1.setBounds(200,220,200,30);
		pf2.setBounds(200,260,200,30);
		pf1.setEditable(false);
		pf2.setEditable(false);
		b2.setBounds(200,300,100,30);
		b3.setBounds(310,300,100,30);
		p.add(Title);
		p.add(l1);
		p.add(l2);
		p.add(l3);
		p.add(l4);
		p.add(l5);
		p.add(tf1);
		p.add(tf2);
		p.add(tf3);
		p.add(b1);
		p.add(b2);
		p.add(b3);
		p.add(pf1);
		p.add(pf2);
	}
	//end of password reset interface
	
	// Start of Registration interface 
	void loadRegistrationUI()
	{	
		f.setTitle("Registration : Privacy Protector");
		f.setSize(small);
		l1.setFont(smlsyswide);
		l2.setFont(smlsyswide);
		l3.setFont(smlsyswide);
		l4.setFont(smlsyswide);
		l5.setFont(smlsyswide);
		l1.setText("Name:");
		l2.setText("Email-id");
		l3.setText("Password:");
		l4.setText("Re-enter Password");
		l5.setText("UserName");
		Title.setText("Enter your details");
		Title.setFont(syswide);
		b1.setText("Create");
		b2.setText("Clear");
		Title.setBounds(20,10,300,30);
		l1.setBounds(40,60,150,30);
		l2.setBounds(40,100,150,30);
		l5.setBounds(40,140,150,30);
		l3.setBounds(40,180,150,30);
		l4.setBounds(40,220,150,30);
		tf1.setBounds(200,60,200,30);
		tf2.setBounds(200,100,200,30);
		tf3.setBounds(200,140,200,30);
		pf1.setBounds(200,180,200,30);
		pf2.setBounds(200,220,200,30);
		b1.setBounds(200,260,100,30);
		b2.setBounds(310,260,100,30);
		p.add(Title);
		p.add(l1);
		p.add(l2);
		p.add(l3);
		p.add(l4);
		p.add(l5);
		p.add(tf1);
		p.add(tf2);
		p.add(tf3);
		p.add(pf1);
		p.add(pf2);
		p.add(b1);
		p.add(b2);
	}
	// End of Registration interface
	
	// Start of Main User interface
	@SuppressWarnings({ "unchecked", "rawtypes" })
	void loadMainUI()
	{
		b1.setText("Encrypt File");
		b2.setText("Delete");
		b3.setText("Search");
		b5.setText("Reset Search");
		b4.setText("Decrypt File");
		f.setTitle("Dashboard : Privacy Protector");
		Title.setText("Your Protected Files:");
		l2.setText("Select order");
		l2.setBounds(750,10,100,30);
		cb2 = new JComboBox();
		cb2.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent ie) {
				if(ie.getItem()=="Ascending Order")
				{
					loadAscendingOrder();
				}
				
				if(ie.getItem()=="Descending Order")
				{
					loadDescendingOrder();
				}
				
				if(ie.getItem()=="Recent Item Order")
				{
					loadRecentOrder();
				}
				}});
		
		cb2.addItem("Recent Item Order");
		cb2.addItem("Ascending Order");
		cb2.addItem("Descending Order");
		b1.setBounds(50,10,100,30);
		b4.setBounds(150,10,100,30);
		b2.setBounds(250,10,100,30);
		b3.setBounds(600,55,100,30);
		b5.setBounds(700,55,150,30);
		searchfield = new JTextField();
		searchfield.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				searchfield.setText("");
			}
			@Override public void mouseEntered(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mousePressed(MouseEvent arg0) {}
			@Override public void mouseReleased(MouseEvent arg0) {}
	    });
		searchfield.setBounds(300,55,300,30);
		searchfield.setText("Search with File Name");
		JScrollPane jsp = new JScrollPane();
		JScrollPane jsp1 = new JScrollPane();
//		JScrollPane jsp2 = new JScrollPane();
		files = new JTable();
		files.setDefaultEditor(Object.class, null);
		files1 = new JTable();
		files1.setDefaultEditor(Object.class, null);
//		files2 = new JTable();
//		files2.setDefaultEditor(Object.class, null);
		model1 = new DefaultTableModel(new String[] {"Sno","File Name","Time","Status"},0);
//		model2 = new DefaultTableModel(new String[] {"Sno","File Name","Time","Status"},0);
		model3 = new DefaultTableModel(new String[] {"Sno","Title","UserName","Password"},0);
		files.setModel(model1);
//		files2.setModel(model2);
		files1.setModel(model3);
		files.getColumnModel().getColumn(0).setPreferredWidth(10);
		files.getColumnModel().getColumn(1).setPreferredWidth(300);
		files.getColumnModel().getColumn(2).setPreferredWidth(100);
		files.getColumnModel().getColumn(3).setPreferredWidth(40);
//		files2.getColumnModel().getColumn(0).setPreferredWidth(10);
//		files2.getColumnModel().getColumn(1).setPreferredWidth(300);
//		files2.getColumnModel().getColumn(2).setPreferredWidth(100);
//		files2.getColumnModel().getColumn(3).setPreferredWidth(40);
		files1.getColumnModel().getColumn(0).setPreferredWidth(100);
		files1.getColumnModel().getColumn(1).setPreferredWidth(300);
		files1.getColumnModel().getColumn(2).setPreferredWidth(300);
		files1.getColumnModel().getColumn(3).setPreferredWidth(300);
		jsp.setBounds(0,0,915,550);
//		jsp2.setBounds(0,0,915,550);
		jsp1.setBounds(0,0,915,550);
		cb2.setBounds(850,10,130,30);
		jsp.setViewportView(files);       
		jsp1.setViewportView(files1);       
//		jsp2.setViewportView(files2);       
		f.setSize(big);
		JButton choose1 = new JButton("Choose");
		choose1.addActionListener(this);
		JPanel p1=new JPanel(),p3 = new JPanel();  
		p1.setLayout(null);
//		p2.setLayout(null);
		p3.setLayout(null);
		p1.setBackground(Color.WHITE);
		JTabbedPane tp=new JTabbedPane(); 
		tp.setBounds(40,100,915,550);  
		tp.add("Encryptor",p1);  
//		tp.add("Hide Files",p2);  
		tp.add("Password Manager",p3);  
		tp.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e)
			{
				if(tp.getSelectedIndex()==1)
				{
					b1.setText("Add Entry");
					b4.setEnabled(false);
					p.repaint();
					thistab = 2;
					Title.setText("Your Protected Passwords:");
					searchfield.setText("Search with Title");
				}
				
				if(tp.getSelectedIndex()==0)
				{
					b1.setText("Encrypt File");
					b4.setText("Decrypt File");
					b4.setEnabled(true);
					thistab=0;
					Title.setText("Your Protected Files:");
					searchfield.setText("Search with File Name");
				}
//				else if(tp.getSelectedIndex()==1)
//				{
//					b1.setText("Hide File");
//					b4.setText("Unhide");
//					b4.setEnabled(true);
//					thistab=1;
//				}
			}});
		p.add(tp); 
		p1.add(jsp);
		p3.add(jsp1);
//		p2.add(jsp2);
		Title.setBounds(40,55,300,30);
		Title.setFont(syswide);
		p.add(l2);
		p.add(Title);
		p.add(cb2);
		p.add(b1);
		p.add(b2);
		p.add(b3);
		p.add(b5);
		p.add(b4);
		p.add(searchfield);
		f.setResizable(false);
	}
	// End of Main User interface
	
	public static void main(String args[])
	{
		new PrivacyProtectorUI("Login");
	}

	//performing actionevent
	@SuppressWarnings("unlikely-arg-type")
	@Override
	public void actionPerformed(ActionEvent ae) {
		//for Login Methods
		
		if(ae.getActionCommand()=="Login")
		{
			
			if(tf1.getText().equals("")||pf2.getPassword().equals(""))
			{
				JOptionPane.showMessageDialog(p,"Username or Password should not be empty! Please enter all details!","Error Encountered",JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				char arr[] = pf1.getPassword();
				String pwd = new String(arr);
				String user = tf1.getText();
				try {
				ResultSet rs =st.executeQuery("select * from login_Table where username='"+user+"' and password ='"+pwd+"'");
				if(rs.next())
				{
					user1=rs.getString(2);
					clearAll();
					loadMainUI();
					loadUpdateTable();
				}
				else
				{
					JOptionPane.showMessageDialog(p,"Incorrect Authentication! Username and Password invalid!","Error Encountered",JOptionPane.ERROR_MESSAGE);
				}}
				catch(Exception e) {}
			}	
		}
		
		if(ae.getActionCommand()=="Clear")
		{
			clearField();
		}	
		
		if(ae.getActionCommand()=="Create")
		{
		  char arr[] = pf1.getPassword() , arr1[] = pf2.getPassword();
		  String pwd = new String(arr) , pwd1 = new String(arr1);
		
			if(tf1.getText().equals("")||tf2.getText().equals("")||tf3.getText().equals("")||pf1.getPassword().equals("")||pf2.getPassword().equals(""))
			{
				JOptionPane.showMessageDialog(f, "All fields must be filled!", "Error Encountered", JOptionPane.ERROR_MESSAGE);
			}
			else if(!pwd.equals(pwd1))
			{
				JOptionPane.showMessageDialog(f, "Password did not match!", "Error Encountered", JOptionPane.ERROR_MESSAGE);
				pf1.setText("");
				pf2.setText("");
			}
			else if(pwd.length()<8||pwd1.length()<8)
			{
				JOptionPane.showMessageDialog(f, "Password length should be more than 8 characters!", "Error Encountered", JOptionPane.ERROR_MESSAGE);
			}
			else if (tf2.getText().contains("@")==false||tf2.getText().contains(".com")==false) {
				JOptionPane.showMessageDialog(f, "Email should contain '@' and should be vaid! ", "Error Encountered", JOptionPane.ERROR_MESSAGE);
			}
			else
			{	try { ResultSet rs = st.executeQuery("select * from login_Table where username = '"+tf3.getText()+"'");
					if(rs.next())
					{
						JOptionPane.showMessageDialog(f, "Username already taken! Please choose another one!", "Error Encountered", JOptionPane.ERROR_MESSAGE);
					}
					else
					{	
						int x=0;
						JTextField question = new JTextField();
						JTextField answer = new JTextField();
						Object[] tandc = {"This software uses ENCRYPTION ALGORITHM to encrypt the data",
								"and provides privacy in terms of hiding files, facility for",
								"storing passwords, encryption etc. This software is not responsible",
								"for any loss of files like unable to unhide files or username and",
								"passwords in case of the masterpassword loss! By accepting the terms",
								"and conditons, you agree to the above mentioned statements!"};
						
						Object[] closesoftware = {"Sorry the software can't be used without accepting T&C!"};
						
						Object[] message = {"Due to security reasons one more security layer is required for password reset purposes!",
							"Please enter details!",
							"Make your own security question and answer!",
							" ",
						    "Set Security Question:", question,
						    "Set Security Answer:", answer};
						
						int updatesecurity=JOptionPane.CANCEL_OPTION;
						while(x==0)
						{
						updatesecurity = JOptionPane.showConfirmDialog(f, message, "Security Prompt", JOptionPane.PLAIN_MESSAGE);
						if(updatesecurity==JOptionPane.OK_OPTION)
						{
						if(question.getText().equals("")||answer.getText().equals(""))
						{
							JOptionPane.showMessageDialog(f, "Question or Answer both should be filled", "Error Encountered", JOptionPane.ERROR_MESSAGE);
							x=0;
						}else
						{
							x=1;
						}
						
						}
						}
						int accepttandc = JOptionPane.showConfirmDialog(f, tandc, "T&C Prompt", JOptionPane.OK_CANCEL_OPTION);
						if(accepttandc==JOptionPane.OK_OPTION)
						{
						st.executeUpdate("insert into login_Table (username,password,name,email,security_q,security_a) values('"+tf3.getText()+"','"+pwd+"','"+tf1.getText()+"','"+tf2.getText()+"', '"+question.getText()+"','"+answer.getText()+"')");
						JOptionPane.showMessageDialog(f, "Successfully Registered!", "Success", JOptionPane.PLAIN_MESSAGE);
						clearField();
						}
						else
						{
							JOptionPane.showConfirmDialog(null, closesoftware, "T&C Prompt", JOptionPane.PLAIN_MESSAGE);
							clearAll();
							loadLoginUI();
						}
					} }catch(Exception e) {} }
			}
		
		if(ae.getActionCommand()=="New User Registration")
		{
			clearAll();
			loadRegistrationUI();
		}
		
		if(ae.getActionCommand()=="Encrypt File")
		{
			FileOutputStream encrypted=null;
			String rfilename=null,rfilepath=null;
			try {
				fc.setDirectory("C:\\");
				fc.setMode(FileDialog.LOAD);
				fc.setVisible(true);
				String getfile = fc.getDirectory();
				String getname = fc.getFile();
				rfilename = getname;
				String newget =getfile.concat(getname);
				FileInputStream original = null;
				original = new FileInputStream(newget);
				fc.setMode(FileDialog.SAVE);
				fc.setVisible(true);
				String savefile = fc.getDirectory();
				String savename = fc.getFile();
				rfilename = savename;
				String newsave = savefile.concat(savename);
				rfilepath=newsave;
				encrypted = new FileOutputStream(newsave);
				ResultSet rs =st.executeQuery("select * from login_Table where username='"+user1+"'");
				rs.next();
				String pass = rs.getString("password");
				new CryptoAlgo().encryptOrDecrypt(pass, Cipher.ENCRYPT_MODE, original, encrypted);
				st.executeUpdate("insert into storage_Table(filename, path,fk_username) values ('"+rfilename+"','"+rfilepath+"','"+user1+"') ");
				loadUpdateTable();
				Object[] message ={"File encrypted successfully!",
					"Path: "+ rfilepath};
				JOptionPane.showMessageDialog(f, message, "Success", JOptionPane.PLAIN_MESSAGE);
			} catch (Exception e) 
			{	
				JOptionPane.showMessageDialog(f, "File not found!", "Error Encountered", JOptionPane.ERROR_MESSAGE);
			}
			catch(Throwable e1)
			{ 
				JOptionPane.showMessageDialog(f, "Error occured while encrypting the File", "Error Encountered", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if(ae.getActionCommand()=="Decrypt File")
		{
			int row = files.getSelectedRow();
			if(row==-1)
			{
				JOptionPane.showMessageDialog(f, "You didn't selected any file please select!", "Error Encountered", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				String getfilename = files.getModel().getValueAt(row, 1).toString();
				try {
					ResultSet rs = st.executeQuery("select path from storage_Table where filename ='"+getfilename+"' and fk_username ='"+user1+"'");
					if(rs.next())
					{
						String getpath = rs.getString(1);
						String rfilename=null,rfilepath=null;
						//////
						FileOutputStream encrypted=null;
						FileInputStream original = null;
						original = new FileInputStream(getpath);
						fc.setMode(FileDialog.SAVE);
						fc.setVisible(true);
						String savefile = fc.getDirectory();
						String savename = fc.getFile();
						rfilename = savename;
						String newsave = savefile.concat(savename);
						rfilepath=newsave;
						encrypted = new FileOutputStream(newsave);
						ResultSet r =st.executeQuery("select * from login_Table where username='"+user1+"'");
						r.next();
						String pass = r.getString("password");
						new CryptoAlgo().encryptOrDecrypt(pass, Cipher.DECRYPT_MODE, original, encrypted);
						st.executeUpdate("update storage_Table set filename='"+rfilename+"', path='"+rfilepath+"',status='decrypted' where fk_username='"+user1+"'and path='"+getpath+"'");
						loadUpdateTable();
							Object[] message ={"File decrypted successfully!",
								"Path: "+ rfilepath};
							JOptionPane.showMessageDialog(f, message, "Success", JOptionPane.PLAIN_MESSAGE);
					}
				} catch (Exception e) 
				{	
					JOptionPane.showMessageDialog(f, "File not found!", "Error Encountered", JOptionPane.ERROR_MESSAGE);
				}
				catch(Throwable e1)
				{	
					JOptionPane.showMessageDialog(f, "Error occured while decrypting the File", "Error Encountered", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
		if(ae.getActionCommand()=="Delete"&&thistab==0)
		{
			int row =files.getSelectedRow();
			if(row==-1)
			{
				JOptionPane.showMessageDialog(f, "There is no file selected!", "Error Encountered", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				String getfilename = files.getModel().getValueAt(row, 1).toString();
				try {
					st.executeUpdate("delete from storage_Table where filename='"+getfilename+"' and fk_username='"+user1+"'");
					loadUpdateTable();
				}catch(Exception e) { }
			}
		}
		
		if(ae.getActionCommand()=="Delete"&&thistab==2)
		{
			int row =files1.getSelectedRow();
			if(row==-1)
			{
				JOptionPane.showMessageDialog(f, "There is no file selected!", "Error Encountered", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				String title = files1.getModel().getValueAt(row, 1).toString();
				String username = files1.getModel().getValueAt(row, 2).toString();
				String password = files1.getModel().getValueAt(row, 3).toString();
				try {
					st.executeUpdate("delete from passwordvault_Table where title='"+title+"' and username ='"+username+"' and password ='"+password+"' and fk_username='"+user1+"'");
					loadUpdateTable();
					}catch(Exception e1) { }
				}
			}
			
		if(ae.getActionCommand()=="Search"&&thistab==0)
		{
			if(searchfield.getText().equals(""))
			{
			JOptionPane.showMessageDialog(f, "Please enter some data first for searching!", "Error Encountered", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
			try {
			rs =st.executeQuery("select filename,log,status from storage_Table where fk_username = '"+user1+"'and filename like'"+searchfield.getText()+"%'");
			int count=0;
			model1.setRowCount(0);
			while(rs.next())
			{
				count++;
				String filename = rs.getString(1);
				Timestamp timestamp =rs.getTimestamp(2);
				String datetime = timestamp.toString();
				String status = rs.getString(3);
				model1.addRow(new Object[] {count,filename,datetime,status});
			}
			if(model1.getRowCount()==0)
			{
				JOptionPane.showMessageDialog(f, "No results found", "Error", JOptionPane.ERROR_MESSAGE);
			}
			}catch(Exception e) {};
			}
		}
		
		if(ae.getActionCommand()=="Search"&&thistab==2)
		{
			if(searchfield.getText().equals(""))
			{
			JOptionPane.showMessageDialog(f, "Please enter some data first for searching!", "Error Encountered", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
			try {
			rs =st.executeQuery("select title, username, password from passwordvault_Table where fk_username ='"+user1+"'and title like'"+searchfield.getText()+"%'");
			int count=0;
			model3.setRowCount(0);
			while(rs.next())
				{	count++;
					String titlename = rs.getString(1);
					String usernamename = rs.getString(2);
					String passwordname = rs.getString(3);
					model3.addRow(new Object[] {count,titlename,usernamename,passwordname}); }
			if(model3.getRowCount()==0)
			{
				JOptionPane.showMessageDialog(f, "No results found", "Error", JOptionPane.ERROR_MESSAGE);
			}
			}catch(Exception e) {};
			}
		}
		
		if(ae.getActionCommand()=="Reset Search")
		{
			loadUpdateTable();
			searchfield.setText("");
		}
		
		if(ae.getActionCommand()=="Add Entry")
		{
			JTextField title = new JTextField(), username = new JTextField(), password = new JTextField();
			int getentry;
			Object[] entry = {"Enter the details",
					"Enter Title", title,
				    "Enter Username:", username,
				    "Enter Password:", password};
			getentry = JOptionPane.showConfirmDialog(f, entry, "Add Entry", JOptionPane.OK_CANCEL_OPTION);
			if(getentry==JOptionPane.OK_OPTION)
			{
				if(title.equals("")||username.equals("")||password.equals(""))
				{
					JOptionPane.showMessageDialog(f, "All Entries should be files", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					try {
						st.executeUpdate("insert into passwordvault_Table(fk_username,title,username,password) values ('"+user1+"','"+title.getText()+"','"+username.getText()+"','"+password.getText()+"')");
					loadUpdateTable();
					JOptionPane.showMessageDialog(f, "Entry added successfully!", "Success", JOptionPane.PLAIN_MESSAGE);
						
					}catch(Exception e) {
						JOptionPane.showMessageDialog(f, "Entry not added error occoured!", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			else
			{
				JOptionPane.showMessageDialog(f, "Entry canceled", "Prompt", JOptionPane.ERROR_MESSAGE);
			}
			}
			
		if(ae.getActionCommand()=="Forgot Password")
		{
			clearAll();
			loadResetPasswordUI();
		}
		
		if(ae.getActionCommand()=="Check")
		{
			try {
				ResultSet rs = st.executeQuery("select * from login_Table where username='"+tf1.getText()+"'");
			if(!rs.next())
				{
				JOptionPane.showMessageDialog(f, "Username not found!", "Error Encountered", JOptionPane.ERROR_MESSAGE);
				}
			else
			{
				p.remove(b1);
				p.repaint();
				b1.setText("Validate");
				tf2.setText(rs.getString(6));
				tf3.setEditable(true);
				b1.setBounds(200,180,100,30);
				p.add(b1);
			}
			}catch(Exception e) {}
		}
			
		if(ae.getActionCommand()=="Validate")
		{
			try {
				ResultSet rs = st.executeQuery("select * from login_Table where username='"+tf1.getText()+"'");
				rs.next();
				if(tf3.getText().equals(rs.getString(7)))
				{
					pf1.setEditable(true);
					pf2.setEditable(true);	
				}
				else
				{
					JOptionPane.showMessageDialog(f, "Answer did not match! Enter again!", "Error Encountered", JOptionPane.ERROR_MESSAGE);
				}
			}catch(Exception e) {}
		}
		
		if(ae.getActionCommand()=="Reset")
		{
			char arr[] = pf1.getPassword(), arr1[] = pf2.getPassword();
			String pwd = new String(arr), pwd1 = new String(arr1);
			if(!pwd.equals(pwd1))
			{
				JOptionPane.showMessageDialog(f, "Password did not match! Enter again", "Error Encountered", JOptionPane.ERROR_MESSAGE);
				pf1.setText("");
				pf2.setText("");
			}
			else
			{
			try {
				st.executeUpdate("update login_Table set password='"+pwd+"' where username='"+tf1.getText()+"'");
				JOptionPane.showMessageDialog(f, "Password successfully updated!", "Success", JOptionPane.PLAIN_MESSAGE);
			clearAll();
			loadResetPasswordUI();
			}catch(Exception e) {}
			}	
		}	
	}
}
