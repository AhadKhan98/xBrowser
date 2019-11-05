package xBrowser;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.html.*;

public class xBrowser extends JFrame
	
implements HyperlinkListener {
	
	private JButton backBtn, fwdBtn; // Forward and back buttons
	private JTextField urlTextField; // Text field containing url
	private JEditorPane displayPane; // Displaying pages
	private ArrayList pages = new ArrayList(); // All pages visited
	
	// Constructor
	public xBrowser() { 
		super("xBrowser"); // Sets title
		setSize(640, 480);
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exitBrowser();
            }
		});
		
	// Setting up file menu
	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu("File");
	fileMenu.setMnemonic(KeyEvent.VK_F);
	JMenuItem fileExitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
	fileExitMenuItem.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            exitBrowser();
        }
    });
	fileMenu.add(fileExitMenuItem);
    menuBar.add(fileMenu);
    setJMenuBar(menuBar);
	
    
    // Setting up forward and back buttons
    JPanel buttonPanel = new JPanel();
    // Back Button
    backBtn = new JButton("< Back");
    backBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            back();
        }
    });
    backBtn.setEnabled(false);
    buttonPanel.add(backBtn);
    // Forward Button
    fwdBtn = new JButton("Forward >");
    fwdBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            forward();
        }
    });
    fwdBtn.setEnabled(false);
    buttonPanel.add(fwdBtn);
    
    // Setting up url field
    urlTextField = new JTextField(40);
    urlTextField.addKeyListener(new KeyAdapter() {
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                go();
            }
        }
    });
    buttonPanel.add(urlTextField);
    
    // Setting up go button
    JButton goBtn = new JButton("GO");
    goBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            go();
        }
    });
    buttonPanel.add(goBtn);
    
    // Setting up the display pane
    displayPane = new JEditorPane();
    displayPane.setContentType("text/html");
    displayPane.setEditable(false);
    displayPane.addHyperlinkListener(this);
    
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(buttonPanel, BorderLayout.NORTH);
    getContentPane().add(new JScrollPane(displayPane),
            BorderLayout.CENTER);
	}
	
	
	
	
}