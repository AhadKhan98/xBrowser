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
	
	private void exitBrowser() {
        System.exit(0);
    }
	
	private void back() {
        URL currentUrl = displayPane.getPage();
 
        int pageIndex = pages.indexOf(currentUrl.toString());
        try {
            showPage(
                    new URL((String) pages.get(pageIndex - 1)), false);
        } catch (Exception e) {}
    }
	
	private void forward() {
        URL currentUrl = displayPane.getPage();
        int pageIndex = pages.indexOf(currentUrl.toString());
        try {
            showPage(
                    new URL((String) pages.get(pageIndex + 1)), false);
        } catch (Exception e) {}
    }
	
	private void go() {
        URL verifiedUrl = verifyUrl(urlTextField.getText());
        if (verifiedUrl != null) {
            showPage(verifiedUrl, true);
        } else {
            showError("Invalid URL");
        }
    }
	
	private void showError(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage,
                "Error", JOptionPane.ERROR_MESSAGE);
    }
	
	private URL verifyUrl(String url) {
        // Only allow HTTP URLs.
        if (!url.toLowerCase().startsWith("http://"))
            return null;
         
        // Verify format of URL.
        URL verifiedUrl = null;
        try {
            verifiedUrl = new URL(url);
        } catch (Exception e) {
            return null;
        }
         
        return verifiedUrl;
    }
	
	private void showPage(URL pageUrl, boolean addToList) {
        // Show hour glass cursor while crawling is under way.
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         
        try {
            // Get URL of page currently being displayed.
            URL currentUrl = displayPane.getPage();
             
            // Load and display specified page.
            displayPane.setPage(pageUrl);
             
            // Get URL of new page being displayed.
            URL newUrl = displayPane.getPage();
             
            // Add page to list if specified.
            if (addToList) {
                int listSize = pages.size();
                if (listSize > 0) {
                    int pageIndex =
                            pages.indexOf(currentUrl.toString());
                    if (pageIndex < listSize - 1) {
                        for (int i = listSize - 1; i > pageIndex; i--) {
                            pages.remove(i);
                        }
                    }
                }
                pages.add(newUrl.toString());
            }
             
            // Update location text field with URL of current page.
            urlTextField.setText(newUrl.toString());
             
            // Update buttons based on the page being displayed.
            updateButtons();
        } catch (Exception e) {
            // Show error messsage.
            showError("Unable to load page");
        } finally {
            // Return to default cursor.
            setCursor(Cursor.getDefaultCursor());
        }
    }
	
	private void updateButtons() {
        if (pages.size() < 2) {
            backBtn.setEnabled(false);
            fwdBtn.setEnabled(false);
        } else {
            URL currentUrl = displayPane.getPage();
            int pageIndex = pages.indexOf(currentUrl.toString());
            backBtn.setEnabled(pageIndex > 0);
            fwdBtn.setEnabled(
                    pageIndex < (pages.size() - 1));
        }
    }
	
	 public void hyperlinkUpdate(HyperlinkEvent event) {
	        HyperlinkEvent.EventType eventType = event.getEventType();
	        if (eventType == HyperlinkEvent.EventType.ACTIVATED) {
	            if (event instanceof HTMLFrameHyperlinkEvent) {
	                HTMLFrameHyperlinkEvent linkEvent =
	                        (HTMLFrameHyperlinkEvent) event;
	                HTMLDocument document =
	                        (HTMLDocument) displayPane.getDocument();
	                document.processHTMLFrameHyperlinkEvent(linkEvent);
	            } else {
	                showPage(event.getURL(), true);
	            }
	        }
	    }
	 
	 public static void main(String[] args) {
	        xBrowser browser = new xBrowser();
	        browser.setVisible(true);
	
	 }
}