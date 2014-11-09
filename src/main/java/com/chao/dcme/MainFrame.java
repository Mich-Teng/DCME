package com.chao.dcme;

import com.chao.dcme.dialog.WaitingDialog;
import com.chao.dcme.exception.FileNotExistException;
import com.chao.dcme.local.LocalInfo;
import com.chao.dcme.local.LocalListener;
import com.chao.dcme.local.LocalSender;
import com.chao.dcme.local.PeerStatus;
import com.chao.dcme.protocol.Event;
import com.chao.dcme.util.Utilities;
import com.chao.dcme.util.VoteTool;
import com.petebevin.markdown.MarkdownProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-02 16:41.
 * Package: com.chao.dcme
 * Description: main framework of DCME
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class MainFrame {
    private final int WAITTING_TIME = 10;
    private JFrame frame = new JFrame();
    private JTextArea textArea = new JTextArea();
    private JEditorPane dispArea = new JEditorPane();
    private JLabel controlArea = new JLabel();
    private JTextArea logTextArea = new JTextArea();
    private JTextArea chatArea = new JTextArea();
    private LocalListener localListener = new LocalListener(this);
    private LocalInfo localInfo = new LocalInfo();


    private void initTextArea() {
        textArea.setFont(new Font("Serif", Font.PLAIN, 15));
        // only when new a document or open a new file or invited by
        // others can be edited
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setText("#The text window is currently locked.\n\n If you want to create a New document," +
                "please press new Document.\n\n If you want to Open Document, please press open document.\n\n " +
                "Otherwise you should wait for someone inviting you.");
        // add listener
        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent arg0) {
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                // Operational Transformation
                // TODO
                refreshDispArea();
            }
        });
    }

    private void initDispArea() {
        dispArea.setBackground(Color.WHITE);
        // set the background opaque
        dispArea.setOpaque(true);
        // set the font of the text
        dispArea.setFont(new Font("Serif", Font.PLAIN, 15));
    }

    private ActionListener createNewDocListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalInfo.setPeerStatus(PeerStatus.STARTER);
                textArea.setEditable(true);
                textArea.setText("");
                refreshDispArea();
            }
        };
    }

    private void unblockTextArea() {
        textArea.setEditable(true);
    }

    private ActionListener createOpenFileListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showOpenDialog(null);
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    String content = Utilities.readFile(path);
                    textArea.setText(content);
                    refreshDispArea();
                } catch (FileNotExistException exception) {
                    // TODO add into log window
                    exception.printStackTrace();
                }
            }
        };
    }

    private ActionListener createSaveFileListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showSaveDialog(null);
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                Utilities.writeFile(path, textArea.getText());
            }
        };
    }

    private ActionListener createInviteListener() {
        // create a dialog to let user input ip, port and invitation type
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // send invitation to specific ip and port
                String ipPort = JOptionPane.showInputDialog("Please input ip:port");
                String[] strs = ipPort.split(":");
                if (strs.length != 2)
                    JOptionPane.showMessageDialog(null, "Invalid Format of Ip and Port",
                            "Error!", JOptionPane.ERROR_MESSAGE);
                try {
                    LocalSender.sendInvitationMsg(strs[0], Integer.parseInt(strs[1]),
                            Event.INVITATION);
                    System.out.println("Invite server to network: " + strs[0] + ":" + strs[1]);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        };
    }

    private ActionListener createInviteROListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // send invitation to specific ip and port
                // send invitation to specific ip and port
                String ipPort = JOptionPane.showInputDialog("Please input ip:port");
                String[] strs = ipPort.split(":");
                if (strs.length != 2)
                    JOptionPane.showMessageDialog(null, "Invalid Format of Ip and Port",
                            "Error!", JOptionPane.ERROR_MESSAGE);
                try {
                    LocalSender.sendInvitationMsg(strs[0], Integer.parseInt(strs[1]),
                            Event.INVITATION_RO);
                    System.out.println("Invite server to network: " + strs[0] + ":" + strs[1]);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        };
    }

    private ActionListener createLockListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // whether all of them confirm
                LocalSender.sendLockRequestMsg();
                // present a dialog to user to show lock request sent and open a timer to wait for others
                WaitingDialog dialog = new WaitingDialog(frame, true, "Waiting for other peers' confirm..");

                if (VoteTool.isComplete()) {
                    JOptionPane.showMessageDialog(null, "Lock Request Confirmed!");
                    LocalSender.sendLockCommandMsg();
                } else {
                    JOptionPane.showMessageDialog(null, "Lock Request Failed!");
                }
            }
        };
    }

    private ActionListener createUnlockListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalSender.sendUnlockMsg();
                JOptionPane.showConfirmDialog(frame, "Unlock Request is sent!");
            }
        };
    }

    private ActionListener createKickOutListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String server = JOptionPane.showInputDialog("Please input the identifier of the server you want to kick out");
                // kicked out and wait for their confirm
                LocalSender.sendKickoutRequestMsg(server);
                // present a dialog to user to show lock request sent and open a timer to wait for others
                WaitingDialog dialog = new WaitingDialog(frame, true, "Waiting for other peers' confirm..");
                if (VoteTool.isComplete()) {
                    JOptionPane.showMessageDialog(null, "Kickout Request Confirmed!");
                    LocalSender.sendKickoutCommandMsg(server.getBytes());
                } else {
                    JOptionPane.showMessageDialog(null, "Kickout Request Failed!");
                }
            }
        };
    }

    private void initControlArea() {
        controlArea.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        String[] funcs = {"New Document", "Open File", "Save File", "Invite", "Invite_RO",
                "Lock", "Unlock", "Kick Out"};
        ActionListener[] listeners = {createNewDocListener(), createOpenFileListener(), createSaveFileListener(),
                createInviteListener(), createInviteROListener(), createLockListener(), createUnlockListener(),
                createKickOutListener()};
        for (int i = 0; i < funcs.length; i++) {
            JButton button = new JButton(funcs[i]);
            button.setBackground(Color.WHITE);
            constraints.weightx = 1.0 / 3;
            constraints.gridx = i % 3;
            constraints.gridy = i / 3;
            constraints.ipady = 0;
            controlArea.add(button, constraints);
            button.addActionListener(listeners[i]);
        }
        // init log info area
        JLabel logLabel = new JLabel("Log Window");
        logLabel.setFont(new java.awt.Font("Dialog", 1, 15));
        JScrollPane jScrollPaneLog = new JScrollPane();
        jScrollPaneLog.add(logTextArea);
        jScrollPaneLog.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPaneLog.setViewportView(logTextArea);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.gridx = 1;
        constraints.gridy = 3;
        controlArea.add(logLabel, constraints);
        constraints.gridx = 0;
        constraints.weighty = 0.45;
        constraints.gridheight = 20;
        constraints.gridy = 4;
        controlArea.add(jScrollPaneLog, constraints);
        logTextArea.setText("My Identifier: " + LocalInfo.getLocalIdentifier() + "\nMy Ip: " +
                LocalInfo.getLocalIp() + "\nMy Port: " + LocalInfo.getLocalPort() + "\n");

        // init chat area
        constraints.gridy = 25;
        constraints.gridheight = 1;
        constraints.gridwidth = 3;
        constraints.gridx = 1;
        constraints.weighty = 0.005;
        JLabel chatLabel = new JLabel("Chat Window");
        chatLabel.setFont(new java.awt.Font("Dialog", 1, 15));
        controlArea.add(chatLabel, constraints);
        JScrollPane jScrollPaneChat = new JScrollPane();
        jScrollPaneChat.add(chatArea);
        jScrollPaneChat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPaneChat.setViewportView(chatArea);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.weighty = 0.45;
        constraints.gridheight = 18;
        constraints.gridy = 26;
        controlArea.add(jScrollPaneChat, constraints);

        final JTextField inputWindow = new JTextField();
        constraints.weighty = 0;
        constraints.gridheight = 1;
        constraints.gridy = 45;
        controlArea.add(inputWindow, constraints);
        controlArea.setBackground(Color.WHITE);

        inputWindow.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    String msg = inputWindow.getText();
                    chatArea.append("Me: " + msg + "\n");
                    inputWindow.setText("");
                    LocalSender.sendChatMsg(msg);

                }
            }
        });
    }

    private void refreshDispArea() {
        MarkdownProcessor m = new MarkdownProcessor();
        dispArea.setContentType("text/html");
        dispArea.setText(String.format("<html>%s</html>", m.markdown(
                textArea.getText())).replaceAll("src=\"", "src=\"file:"));

    }

    private void init() {
        // set font of menu and items
        Font font = new Font("Calibri", Font.PLAIN, 10);
        UIManager.put("Menu.font", font);
        UIManager.put("MenuItem.font", font);
        // set title
        frame.setTitle("DCME v1.0");
        // set window size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(new Dimension((int) (screenSize.width * 0.8), (int) (screenSize.height * 0.8)));
        int windowWidth = frame.getWidth();
        int windowHeight = frame.getHeight();
        // set the window in the middle of the screen
        frame.setLocation(screenSize.width / 2 - windowWidth / 2, screenSize.height / 2 - windowHeight / 2);

        // set frame layout
        frame.setLayout(new GridLayout(1, 3, 5, 5));
        // set first component: edit text area
        initTextArea();
        JScrollPane jScrollPaneText = new JScrollPane();
        jScrollPaneText.add(textArea);
        jScrollPaneText.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPaneText.setViewportView(textArea);
        frame.add(jScrollPaneText);
        // set second component: display part of markdown file
        initDispArea();
        JScrollPane jScrollPaneDisp = new JScrollPane();
        jScrollPaneDisp.add(dispArea);
        jScrollPaneDisp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPaneDisp.setViewportView(dispArea);
        frame.add(jScrollPaneDisp);
        // set third component: control area

        initControlArea();
        frame.add(controlArea);

        refreshDispArea();
    }

    private void start() {
        frame.setVisible(true);
        Thread listenerThread = new Thread(localListener);
        listenerThread.start();
    }

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.init();
        mainFrame.start();
    }
}
