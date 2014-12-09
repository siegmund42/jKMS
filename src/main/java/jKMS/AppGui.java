package jKMS;

import jKMS.states.Evaluation;
import jKMS.states.Load;
import jKMS.states.Play;
import jKMS.states.Preparation;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JCheckBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

@Component
@Scope("singleton")
public class AppGui {
	@Autowired
	private Kartoffelmarktspiel kms;

	private JFrame frmJkms;
	private JButton btnClose;

	private JPanel logPanel;
	private JCheckBox chckbxShowLog;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppGui window = new AppGui();
					window.frmJkms.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		SpringApplication.run(Application.class, args);
		try {
			// Desktop.getDesktop().browse(new
			// URL("http://localhost:4242/index").toURI());
			BareBonesBrowserLaunch.openURL("http://localhost:8080/index");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
		frmJkms = new JFrame();
		frmJkms.setResizable(false);
		frmJkms.setTitle("jKMS");
		frmJkms.setBounds(100, 100, 450, 300);
		frmJkms.getContentPane().setLayout(null);
		frmJkms.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmJkms.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				confirmExit();
			}
		});

		JButton btnOpenBrowser = new JButton("Open Browser");
		btnOpenBrowser.setBounds(10, 210, 200, 50);
		btnOpenBrowser.setActionCommand("Browser");
		btnOpenBrowser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand() == "Browser") {
					/*
					 * if(kms.getState() instanceof Preparation){
					 * BareBonesBrowserLaunch
					 * .openURL("http://localhost:8080/index"); } else
					 * if(kms.getState() instanceof Load){
					 * BareBonesBrowserLaunch
					 * .openURL("http://localhost:8080/index"); } else
					 * if(kms.getState() instanceof Play){
					 * BareBonesBrowserLaunch
					 * .openURL("http://localhost:8080/index"); } else
					 * if(kms.getState() instanceof Evaluation){
					 * BareBonesBrowserLaunch
					 * .openURL("http://localhost:8080/index"); }
					 */

					// appendLog(String.valueOf(kms.getPlayerCount()));
					if (kms == null)
						textArea.append("null");
					else {
						textArea.append(kms.getState().toString());
					}
				}
			}
		});
		frmJkms.getContentPane().add(btnOpenBrowser);

		btnClose = new JButton("Close");
		btnClose.setBounds(234, 210, 200, 50);
		btnClose.setActionCommand("Exit");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand() == "Exit") {
					confirmExit();
				}
			}
		});
		frmJkms.getContentPane().add(btnClose);

		logPanel = new JPanel();
		logPanel.setBounds(10, 11, 424, 188);
		frmJkms.getContentPane().add(logPanel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0, 1.0, Double.MIN_VALUE };
		logPanel.setLayout(gbl_panel);

		chckbxShowLog = new JCheckBox("show Log");
		GridBagConstraints gbc_chckbxShowLog = new GridBagConstraints();
		gbc_chckbxShowLog.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxShowLog.gridx = 0;
		gbc_chckbxShowLog.gridy = 0;
		chckbxShowLog.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (chckbxShowLog.isSelected())
					textArea.setVisible(true);
				else
					textArea.setVisible(false);
			}
		});
		logPanel.add(chckbxShowLog, gbc_chckbxShowLog);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setVisible(false);
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 1;
		logPanel.add(textArea, gbc_textArea);
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
