/*
 * The MIT License
 *
 * Copyright 2017 Benjamin Brahmer.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jsoundboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Benjamin Brahmer
 */
public class GUI implements ActionListener {

    private static JPanel mainPanel;
    private static JFrame mainFrame;
    private final Properties properties;

    private SoundPlayer player;
    private Thread thread;

    /**
     * Creates the main GUI
     */
    public GUI() {

        properties = new Properties();

        mainFrame = new JFrame("Jsoundboard");
        JMenuBar bar = new JMenuBar();
        JMenuItem menu = new JMenuItem("Add new Button");
        mainPanel = new JPanel();
        MenuListener menuListener = new MenuListener(this);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bar.add(menu);
        mainFrame.setJMenuBar(bar);

        mainFrame.add(mainPanel);
        mainFrame.setSize(400, 500);

        menu.addActionListener(menuListener);

        mainFrame.setVisible(true);
        FileInputStream in;
        try {
            in = new FileInputStream(System.getProperty("user.home")+"/JSoundboard/Jsoundboard.xml");
            System.getProperty("user.dir");
            properties.loadFromXML(in);
            for (Map.Entry<Object, Object> e : properties.entrySet()) {
                String buttonName = (String) e.getKey();
                System.out.println(buttonName);
                createButton(buttonName);
            }
        } catch (IOException ex) {
            System.out.println("Keine Datei gefunden");
        }
    }

    /**
     * Adds a new Button to the main GUI
     *
     * @param buttonName Name for the new Button
     * @param fileName Path to the file to be played
     */
    public void createNewButton(String buttonName, String fileName) {

        properties.setProperty(buttonName, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(System.getProperty("user.home")+"/JSoundboard/Jsoundboard.xml");
            properties.storeToXML(fos, "These are your buttons");
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        createButton(buttonName);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String command = ((JButton) e.getSource()).getActionCommand();
        if (thread != null && thread.isAlive()) {
            player.stopAudio();
        }
        player = new SoundPlayer(properties.getProperty(command));
        thread = new Thread(player);

        thread.start();
    }

    private void createButton(String buttonName) {
        JButton button = new JButton(buttonName);
        button.addActionListener(this);
        mainPanel.add(button);
        mainPanel.revalidate();
        mainFrame.validate();
    }
}
