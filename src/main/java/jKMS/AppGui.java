package jKMS;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.GridBagLayout;

import javax.swing.JCheckBox;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AppGui extends JFrame{
	static final long serialVersionUID = 1337;
	
	private JButton btnClose, btnOpenBrowser;
	private JLabel lblStatus;
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
		
		
		btnOpenBrowser = new JButton(LogicHelper.getLocalizedMessage("GUI.btnOpen"));
		btnOpenBrowser.setBounds(50, 65, 175, 50);
		btnOpenBrowser.setEnabled(false);
		btnOpenBrowser.setActionCommand("Browser");
		btnOpenBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				URI redirect = null;
				
				try {
					redirect = new URI("http://localhost:8080/autoRedirect");
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}

				try {
					Desktop.getDesktop().browse(redirect);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		getContentPane().add(btnOpenBrowser);
		
		lblStatus = new JLabel(LogicHelper.getLocalizedMessage("GUI.lblLoading"));
		lblStatus.setFont(new Font("Courier New", Font.PLAIN, 15));
		lblStatus.setForeground(new Color(0x00FF0000));
		lblStatus.setBounds(225, 65, 180, 50);
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setVerticalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblStatus);
		
		btnClose = new JButton(LogicHelper.getLocalizedMessage("GUI.btnClose"));
		btnClose.setBounds(405, 65, 175, 50);
		btnClose.setEnabled(false);
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
		
		chckbxShowLog = new JCheckBox(LogicHelper.getLocalizedMessage("GUI.showLog"));
		GridBagConstraints gbc_chckbxShowLog = new GridBagConstraints();
		gbc_chckbxShowLog.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxShowLog.gridx = 0;
		gbc_chckbxShowLog.gridy = 0;
		chckbxShowLog.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (chckbxShowLog.isSelected()){
					setSize(640, 480);
					btnOpenBrowser.setBounds(50, 390, 200, 50);
					lblStatus.setBounds(225, 390, 180, 50);
					btnClose.setBounds(380, 390, 200, 50);
					scrollPane.setVisible(true);
					//validate();
					//repaint();
				}
				else{
					setSize(640, 180);
					btnOpenBrowser.setBounds(50, 65, 200, 50);
					lblStatus.setBounds(225, 65, 180, 50);
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
	
	public void setLoaded(){
		lblStatus.setText(LogicHelper.getLocalizedMessage("GUI.lblReady"));
		lblStatus.setForeground(new Color(0x00009900));
		btnOpenBrowser.setEnabled(true);
		btnClose.setEnabled(true);
	}
	
	public void changeLanguage(){
		btnOpenBrowser.setText(LogicHelper.getLocalizedMessage("GUI.btnOpen"));
		btnClose.setText(LogicHelper.getLocalizedMessage("GUI.btnClose"));
		lblStatus.setText(LogicHelper.getLocalizedMessage("GUI.lblReady"));
		chckbxShowLog.setText(LogicHelper.getLocalizedMessage("GUI.showLog"));
	}
}
