package justinjohnson.java.subtitlebuilder;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI implements Runnable {

	private DefaultListModel<Object> listModel;
	private JButton skipButton;
	private JFrame frame;
	private JList<Object> list;
	private JScrollPane scrollPane;

	private class DisableSkipButton implements Runnable {

		private GUI gui;

		public DisableSkipButton(GUI gui) {
			this.gui = gui;
			try {
				if (SwingUtilities.isEventDispatchThread()) {
					this.run();
				} else {
					SwingUtilities.invokeAndWait(this);
				}
			} catch (Exception e) {
				System.err.println("There was a problem disabling the skip button!");
				e.printStackTrace();
			}
			return;
		}

		@Override
		public void run() {
			this.gui.skipButton.setEnabled(false);
			for (ActionListener actionListener : this.gui.skipButton.getActionListeners()) {
				this.gui.skipButton.removeActionListener(actionListener);
			}
			return;
		}

	}

	private class EnableSkipButton implements Runnable {

		private GUI gui;
		private Process process;

		public EnableSkipButton(GUI gui, Process process) {
			this.gui = gui;
			this.process = process;
			try {
				if (SwingUtilities.isEventDispatchThread()) {
					this.run();
				} else {
					SwingUtilities.invokeAndWait(this);
				}
			} catch (Exception e) {
				System.err.println("There was a problem enabling the skip button!");
				e.printStackTrace();
			}
			return;
		}

		@Override
		public void run() {
			new GUI.DisableSkipButton(this.gui).run();
			this.gui.skipButton.addActionListener(new GUI.SkipAction(this.gui, this.process));
			this.gui.skipButton.setEnabled(true);
			return;
		}

	}

	private class Log implements Runnable {

		private GUI gui;
		private String message;

		public Log(GUI gui, String message) {
			this.gui = gui;
			this.message = message;
			try {
				if (SwingUtilities.isEventDispatchThread()) {
					this.run();
				} else {
					SwingUtilities.invokeAndWait(this);
				}
			} catch (Exception e) {
				System.err.println("There was a problem logging to the GUI!");
				e.printStackTrace();
			}
			return;
		}

		@Override
		public void run() {
			this.gui.listModel.addElement(message);
			return;
		}

	}

	private class ParseMethodSelector implements Runnable {

		private GUI gui;

		public ParseMethodSelector(GUI gui) {
			this.gui = gui;
			try {
				if (SwingUtilities.isEventDispatchThread()) {
					this.run();
				} else {
					SwingUtilities.invokeAndWait(this);
				}
			} catch (Exception e) {
				System.err.println("There was a problem with the parse method selector input dialog!");
				e.printStackTrace();
			}
			return;
		}

		@Override
		public void run() {
			Object pattern = JOptionPane.showInputDialog(this.gui.frame, null, Main.TITLE + " - Parse Method",
					JOptionPane.QUESTION_MESSAGE, null, Main.PARSE_METHOD, Main.PARSE_METHOD[0]);
			if (pattern != null) {
				Main.CONFIGURATION.setParseMethod(pattern.toString());
			}
			return;
		}

	}

	private class PhotoshopPathSelector implements Runnable {

		private GUI gui;

		public PhotoshopPathSelector(GUI gui) {
			this.gui = gui;
			try {
				if (SwingUtilities.isEventDispatchThread()) {
					this.run();
				} else {
					SwingUtilities.invokeAndWait(this);
				}
			} catch (Exception e) {
				System.err.println("There was a problem with the photoshop path selector file chooser!");
				e.printStackTrace();
			}
			return;
		}

		@Override
		public void run() {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setApproveButtonText("Select");
			fileChooser.setApproveButtonMnemonic('s');
			fileChooser.setApproveButtonToolTipText("Select path");
			fileChooser.setCurrentDirectory(new File("/"));
			fileChooser.setDialogTitle(Main.TITLE + " - Photoshop Path");
			fileChooser.setFileFilter(new FileNameExtensionFilter("Program (*.APP;*.DMG;*.EXE)", "app", "dmg", "exe"));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int returnVal = fileChooser.showOpenDialog(this.gui.frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				Main.CONFIGURATION.setPhotoshopPath(fileChooser.getSelectedFile().getAbsolutePath());
			}
			return;
		}
	}

	private class SkipAction implements ActionListener {

		private GUI gui;
		private Process process;

		public SkipAction(GUI gui, Process process) {
			this.gui = gui;
			this.process = process;
			return;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			new GUI.DisableSkipButton(this.gui);
			this.process.destroy();
			return;
		}

	}

	private class TemplatePathSelector implements Runnable {

		private GUI gui;

		public TemplatePathSelector(GUI gui) {
			this.gui = gui;
			try {
				if (SwingUtilities.isEventDispatchThread()) {
					this.run();
				} else {
					SwingUtilities.invokeAndWait(this);
				}
			} catch (Exception e) {
				System.err.println("There was a problem with the template path selector file chooser!");
				e.printStackTrace();
			}
			return;
		}

		@Override
		public void run() {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setApproveButtonText("Select");
			fileChooser.setApproveButtonMnemonic('s');
			fileChooser.setApproveButtonToolTipText("Select path");
			fileChooser.setCurrentDirectory(new File("."));
			fileChooser.setDialogTitle(Main.TITLE + " - Template Path");
			fileChooser.setFileFilter(new FileNameExtensionFilter("Photoshop Document (*.PSD;*.PSDT)", "psd", "psdt"));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int returnVal = fileChooser.showOpenDialog(this.gui.frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				Main.CONFIGURATION.setTemplatePath(fileChooser.getSelectedFile().getAbsolutePath());
			}
			return;
		}
	}

	public GUI() {
		try {
			if (SwingUtilities.isEventDispatchThread()) {
				this.run();
			} else {
				SwingUtilities.invokeAndWait(this);
			}
		} catch (Exception e) {
			System.err.println("There was a problem building the GUI!");
			e.printStackTrace();
		}
		return;
	}

	public String log(String message) {
		new GUI.Log(this, message);
		return message;
	}

	public void DisableSkipButton() {
		new GUI.DisableSkipButton(this);
		return;
	}

	public void EnableSkipButton(Process process) {
		new GUI.EnableSkipButton(this, process);
		return;
	}

	public void ParseMethodSelector() {
		new GUI.ParseMethodSelector(this);
		return;
	}

	public void PhotoshopPathSelector() {
		new GUI.PhotoshopPathSelector(this);
		return;
	}

	public void TemplatePathSelector() {
		new GUI.TemplatePathSelector(this);
		return;
	}

	@Override
	public void run() {
		this.frame = new JFrame(Main.TITLE + " - Console");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = this.frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.setPreferredSize(new Dimension(540, 180));
		this.listModel = new DefaultListModel<>();
		this.list = new JList<>(this.listModel);
		this.scrollPane = new JScrollPane(this.list);
		contentPane.add(this.scrollPane, BorderLayout.CENTER);
		this.skipButton = new JButton("Skip");
		this.skipButton.setEnabled(false);
		contentPane.add(this.skipButton, BorderLayout.SOUTH);
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);
		return;
	}

}
