package tk.josephsmendoza.ifeode.installer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PostInstall extends JFrame implements ActionListener {
    private JLabel additionalActionsLabel;
    private JButton finishButton;
    private JPanel panel;
    private JCheckBox desktopShortcutCheckBox;
    private JCheckBox startMenuShortcutCheckBox;

    public PostInstall() {
	initGUI();
    }

    private void initGUI() {
	getContentPane().add(getAdditionalActionsLabel(), BorderLayout.NORTH);
	getContentPane().add(getFinishButton(), BorderLayout.SOUTH);
	getContentPane().add(getPanel(), BorderLayout.CENTER);
    }

    public JLabel getAdditionalActionsLabel() {
	if (additionalActionsLabel == null) {
	    additionalActionsLabel = new JLabel("Additional actions:");
	}
	return additionalActionsLabel;
    }

    public JButton getFinishButton() {
	if (finishButton == null) {
	    finishButton = new JButton("Finish");
	    finishButton.addActionListener(this);
	}
	return finishButton;
    }

    public JPanel getPanel() {
	if (panel == null) {
	    panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	    panel.add(getDesktopShortcutCheckBox());
	    panel.add(getStartMenuShortcutCheckBox());
	}
	return panel;
    }

    public JCheckBox getDesktopShortcutCheckBox() {
	if (desktopShortcutCheckBox == null) {
	    desktopShortcutCheckBox = new JCheckBox("Desktop Link");
	    desktopShortcutCheckBox.setSelected(true);
	}
	return desktopShortcutCheckBox;
    }

    public JCheckBox getStartMenuShortcutCheckBox() {
	if (startMenuShortcutCheckBox == null) {
	    startMenuShortcutCheckBox = new JCheckBox("Start Menu Link");
	    startMenuShortcutCheckBox.setSelected(true);
	}
	return startMenuShortcutCheckBox;
    }

    public void actionPerformed(final ActionEvent e) {
	if (e.getSource() == getFinishButton()) {
	    finishButtonActionPerformed(e);
	}
    }

    protected void finishButtonActionPerformed(final ActionEvent e) {
	if (desktopShortcutCheckBox.isSelected()) {
	    Link.make(Link.Location.DESKTOP);
	}
    }
}
