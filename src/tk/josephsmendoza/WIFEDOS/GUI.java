package tk.josephsmendoza.WIFEDOS;

import java.awt.Button;
import java.awt.Component;
import java.awt.Container;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class GUI extends JFrame{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public GUI() {
	this.setContentPane(getLoadingGif());
	this.pack();
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setVisible(true);
	useSystemLookAndFeel();
	this.setContentPane(getMainView());
	setOptimizedSize();
    }

    private JPanel getMainView() {
	JPanel mainView=new JPanel();
	mainView.setLayout(new GridLayout(0,1));
	mainView.add(getInfoView());
	mainView.add(getControlView());
	return mainView;
    }

    private Component getControlView() {
	JPanel controlView=new JPanel();
	controlView.setLayout(new GridLayout());
	controlView.add(getButton("ADD",l -> regAdd()));
	controlView.add(getButton("REMOVE",l -> regRemove()));
	controlView.add(getButton("FIND",l -> findEXE()));
	return controlView;
    }

    private Object findEXE() {
	// TODO Auto-generated method stub
	return null;
    }

    private Object regRemove() {
	// TODO Auto-generated method stub
	return null;
    }

    private Object regAdd() {
	// TODO Auto-generated method stub
	return null;
    }

    private Component getButton(String string, ActionListener l) {
	Button b=new Button(string);
	b.addActionListener(l);
	return b;
    }

    private JScrollPane getInfoView() {
	JScrollPane finalPane=new JScrollPane();
	JPanel infoView=new JPanel();
	infoView.setLayout(new BoxLayout(infoView,BoxLayout.X_AXIS));
	infoView.add(getKeyView());
	infoView.add((getDebuggerView()));
	finalPane.add(infoView);
	return finalPane;
    }

    private Component getDebuggerView() {
	JPanel debuggerView=new JPanel();
	debuggerView.setLayout(new BoxLayout(debuggerView,BoxLayout.Y_AXIS));
	for(String key:WIFEDOS.getReg().keySet()) {
	    debuggerView.add(getItemView(WIFEDOS.getReg().get(key)));
	}
	return debuggerView;
    }

    private Component getItemView(String string) {
	return new JTextField(string);
    }

    private JPanel getKeyView() {
	JPanel keyView=new JPanel();
	keyView.setLayout(new BoxLayout(keyView,BoxLayout.Y_AXIS));
	for(String key:WIFEDOS.getReg().keySet()) {
	    keyView.add(getItemView(key));
	}
	return keyView;
    }

    private JLabel getLoadingGif() {
	return new JLabel(new ImageIcon(getClass().getResource("loading.gif")));
    }

    private JScrollPane getItemsPane() {
	// TODO Auto-generated method stub
	return null;
    }

    private void setOptimizedSize() {
	DisplayMode dm=GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
	this.setSize(dm.getWidth()/2, dm.getHeight()/2);
    }

    private void useSystemLookAndFeel() {
	try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
