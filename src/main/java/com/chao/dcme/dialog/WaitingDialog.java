package com.chao.dcme.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-08 23:04.
 * Package: com.chao.dcme.dialog
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class WaitingDialog extends JDialog implements ActionListener {
    int seconds = 10;

    public WaitingDialog(JFrame parent, boolean modal, String text) {
        super(parent, true);
        if (parent != null) {
            Dimension parentSize = parent.getSize();
            Point p = parent.getLocation();
            setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
        }
        JPanel messagePane = new JPanel();
        messagePane.add(new JLabel(text));
        getContentPane().add(messagePane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        final ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                seconds--;
                if (seconds == 0) {
                    dispose();
                } else {
                    setTitle("The window will be closed after " + seconds + " seconds");
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
        setSize(new Dimension(350, 70));
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        setVisible(false);
        dispose();
    }
}
