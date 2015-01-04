package jKMS;

import jKMS.states.Evaluation;
import jKMS.states.Load;
import jKMS.states.Play;
import jKMS.states.Preparation;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JCheckBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Configurable //for creating AppGui with new - also some additional changes required in project settings
//@Component
public class AppGui extends JFrame{
	static final long serialVersionUID = 1337;
	
	@Autowired
	private Kartoffelmarktspiel kms;
	
	private JButton btnClose, btnOpenBrowser;
	private JPanel logPanel;
	private JCheckBox chckbxShowLog;
	
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private MessageConsole console;
	
	/**
	 * Create the application.
	 */
	public AppGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//CREATE AND ADD COMPONENTS
		setResizable(false);
		setTitle("jKMS");
		setBounds(100, 100, 640, 480);
		setSize(640, 180);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				confirmExit();
			}
		});
		
		
		btnOpenBrowser = new JButton("Open Game in Browser");
		btnOpenBrowser.setBounds(50, 65, 200, 50);
		btnOpenBrowser.setActionCommand("Browser");
		btnOpenBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand() == "Browser") {
					if(kms.getState() instanceof Preparation){
						BareBonesBrowserLaunch.openURL("http://localhost:8080/index");
					}
					else if(kms.getState() instanceof Load){
						BareBonesBrowserLaunch.openURL("http://localhost:8080/load?s=1");
					}
					else if(kms.getState() instanceof Play){
						BareBonesBrowserLaunch.openURL("http://localhost:8080/play");
					}
					else if(kms.getState() instanceof Evaluation){
						BareBonesBrowserLaunch.openURL("http://localhost:8080/evaluate");
					}
				}
			}
		});
		getContentPane().add(btnOpenBrowser);
		
		btnClose = new JButton("Shut Down Server");
		btnClose.setBounds(380, 65, 200, 50);
		btnClose.setActionCommand("Exit");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand() == "Exit") {
					confirmExit();
				}
			}
		});
		getContentPane().add(btnClose);
		
		logPanel = new JPanel();
		logPanel.setBounds(10, 11, 614, 368);
		getContentPane().add(logPanel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		logPanel.setLayout(gbl_panel);
		
		chckbxShowLog = new JCheckBox("Show Log");
		GridBagConstraints gbc_chckbxShowLog = new GridBagConstraints();
		gbc_chckbxShowLog.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxShowLog.gridx = 0;
		gbc_chckbxShowLog.gridy = 0;
		chckbxShowLog.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (chckbxShowLog.isSelected()){
					setSize(640, 480);
					btnOpenBrowser.setBounds(50, 390, 200, 50);
					btnClose.setBounds(380, 390, 200, 50);
					scrollPane.setVisible(true);
					validate();
					repaint();
				}
				else{
					setSize(640, 180);
					btnOpenBrowser.setBounds(50, 65, 200, 50);
					btnClose.setBounds(380, 65, 200, 50);
					scrollPane.setVisible(false);
				}
			}
		});
		logPanel.add(chckbxShowLog, gbc_chckbxShowLog);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		scrollPane.setVisible(false);
		logPanel.add(scrollPane, gbc_scrollPane);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
		scrollPane.setViewportView(textArea);
		
		console = new MessageConsole(textArea, true);
		console.redirectErr();
		console.redirectOut();
		//console.setMessageLines(500);
		
		setVisible(true);
	}

	private void confirmExit() {
		// CONFIRM AND EXIT
		if (JOptionPane.showConfirmDialog(null,
				"Are you sure? This will end any running Kartoffelmarktspiel",
				"Exit", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION){
			System.exit(0);
		}
	}
}
