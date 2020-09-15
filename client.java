import java.io.*;
import java.net.Socket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
public class client extends JFrame
{
  public final static int SOCKET_PORT = 55588;    
  private String FILE;  
  private int FILE_SIZE; 
  private int bytesRead;
  private BufferedOutputStream bos = null;
  private Socket sock = null;
  private Class cls;
  private JPanel  Panel;
  DataInputStream is = null;
   public client()
   {
    setSize(400,400);
    setLayout(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JLabel L=new JLabel("IP Address");
    L.setBounds(0,0,100,20);
    add(L);
    JTextField T=new JTextField();
    T.setBounds(130,0,170,20);
    add(T);
   
    setVisible(true);
    
    T.addActionListener(new ActionListener()
    {
	    public void actionPerformed(ActionEvent e)
		{
		    try
		    {
		      sock = new Socket(T.getText(), SOCKET_PORT);
		      System.out.println("Connecting...");
		      //receiving page.java from server
		      is = new DataInputStream(sock.getInputStream());
		      FILE=is.readUTF();
		      FILE_SIZE=is.readInt();
		      bos = new BufferedOutputStream(new FileOutputStream(FILE));
		      String newF=FILE.substring(0,FILE.indexOf('.'));
		      byte [] mybytearray  = new byte [FILE_SIZE];
	              bytesRead = is.read(mybytearray,0,mybytearray.length);
		      bos.write(mybytearray, 0 ,(int) FILE_SIZE);
	              bos.flush();

		      //adding page panel to frame
		      cls=Class.forName(newF);
		      Constructor constructor = cls.getDeclaredConstructor(ActionListener.class);
		      System.out.println(constructor.getName());
		      Panel =(JPanel)constructor.newInstance(new ButtonHandler());
		      Panel.setBounds(0,30,400,360);
		      add(Panel);
		      System.out.println("File " + FILE + " downloaded (" + bytesRead + " bytes read)");
		      bos.close();
		      repaint();
		    }catch(Exception e1){
		    	System.out.println(e1);
		    }
		}
	  });
  }
  public class ButtonHandler implements ActionListener
  {
	  public void actionPerformed(ActionEvent e)
	  {
		  try
		  {
			   //sending program to server
			  DataOutputStream os = new DataOutputStream(sock.getOutputStream());
			  System.out.print(cls.getName());
			  Field name = cls.getDeclaredField("name");
			  os.writeUTF(((JTextField)name.get(Panel)).getText());
			  Field f = cls.getDeclaredField("file");
			  String filename = ((JTextField)f.get(Panel)).getText();
			  System.out.print(filename);
			  os.writeUTF(filename);
			  
			  File file = new File(filename);
                          os.writeInt((int)file.length());
			  byte [] mybytearray  = new byte [(int)file.length()];
			  BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filename));
			  bis.read(mybytearray,0,mybytearray.length);
                          os.write(mybytearray,0,mybytearray.length);
			  
			  //Recieving report from server 
			  String str = is.readUTF();
			  System.out.println(str);
			  JTextArea field = new JTextArea(str);
			  field.setEditable(false);
			  field.setBounds(100,100,200,200);
			  field.setBackground(new Color(238,238,238));
			  Panel.removeAll();
			  Panel.add(field);
			  Panel.setVisible(true);
			  repaint();
			  bis.close();
			  sock.close();
                          os.close();
		  }catch(Exception e2) {System.out.println(e2);}
	  }
  }
  public static void main (String [] args ) throws IOException 
  {
	  new client();
  }

}
