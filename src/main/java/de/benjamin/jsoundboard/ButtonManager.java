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

package de.benjamin.jsoundboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ButtonManager {
    private Properties buttonList = new Properties();

    ButtonManager() {
        File theDir = new File(System.getProperty("user.home") + "/.JSoundboard");

        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());
            boolean result = false;

            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException se) {
                //handle it
            }
            if (result) {
                System.out.println("DIR created");
            }
        }
        try {
            FileInputStream in = new FileInputStream(System.getProperty("user.home") + "/.JSoundboard/Jsoundboard.xml");
            buttonList.loadFromXML(in);
        } catch (IOException e) {
            System.out.println("not able to read config");
        }
    }

    void addButton(String name, String path) {

        File theDir = new File(System.getProperty("user.home") + File.separator + "JSoundboard");

        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());
            boolean result = false;

            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException se) {
                //handle it
            }
            if (result) {
                System.out.println("DIR created");
            }
        }
        try {
            Path source = new File(path).toPath();
            String splits[] = path.split(File.separator);
            path = theDir + File.separator + splits[splits.length-1];
            System.out.println(path);
            FileOutputStream dst = new FileOutputStream(path);
            Files.copy(source,dst);
        } catch (IOException e) {
            System.out.println("not able to copy audio file");
        }

        if (!buttonList.containsKey(name)) {
            buttonList.setProperty(name, path);

            try {
                FileOutputStream fos = new FileOutputStream(System.getProperty("user.home") + "/.JSoundboard/Jsoundboard.xml");
                buttonList.storeToXML(fos, "These are your buttons");
            } catch (IOException ex) {
                System.out.println("not able to read config");
            }

        }

    }

    void deleteButton(String name) {
        buttonList.remove(name);
        try {
            FileOutputStream fos = new FileOutputStream(System.getProperty("user.home") + "/.JSoundboard/Jsoundboard.xml");
            buttonList.storeToXML(fos, "These are your buttons");
        } catch (IOException ex) {
            System.out.println("not able to read config");
        }
    }

    public void getAction(String name) {
        buttonList.getProperty(name);
    }

    Set<Map.Entry<Object, Object>> getButtons() {
        return buttonList.entrySet();
    }
}
