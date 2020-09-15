import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Page extends JPanel
{
	public JTextField name;
	public JLabel nameLabel;
	public JTextField file;
	public JLabel fileLabel;
	public JButton button;
	
	public Page(ActionListener AL)
	{
		
		nameLabel = new JLabel("STUDENT NAME");
		nameLabel.setBounds(50,50,120,20);
		fileLabel = new JLabel("FILE NAME");
		fileLabel.setBounds(50,100,120,20);
		
		name = new JTextField();
		name.setBounds(180,50,100,20);
		file = new JTextField();
		file.setBounds(180,100,100,20);
		
		button =new JButton("UPLOAD");
		button.setBounds(150,150,100,20);
		button.addActionListener(AL);
		setLayout(null);
		add(name);
		add(file);
		add(nameLabel);
		add(fileLabel);
		add(button);
        setVisible(true);
	}
	
}

