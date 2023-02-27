package ui;

import javax.swing.*;

import model.Event;
import model.EventLog;

// taken from alarm system:
// https://github.students.cs.ubc.ca/CPSC210/AlarmSystem.git

/**
 * Represents a screen printer for printing event log to screen.
 */
public class ScreenPrinter extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private JTextArea logArea;

    /**
     * Constructor sets up window in which log will be printed on screen
     */
    public ScreenPrinter() {
        setTitle("Event Log");
        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        add(scrollPane);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
        printLog(EventLog.getInstance());

    }

    public void printLog(EventLog el) {
        for (Event next : el) {
            logArea.setText(logArea.getText() + next.toString() + "\n\n");
        }
        repaint();
    }

}
