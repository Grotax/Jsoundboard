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
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javazoom.jl.decoder.JavaLayerException;

/**
 *
 * @author Benjamin Brahmer
 */
public class SoundController {

    private SoundPlayer player;
    private SoundPlayerMP3 playmp3;
    private Thread thread;
    private Thread threadMP3;
    private final GUI gui;

    public SoundController(GUI gui) {
        this.gui = gui;
    }

    public void play(String fileName) {
        this.stop();
        if (fileName.toLowerCase().endsWith(".mp3")) {
            try {
                FileInputStream fis = new FileInputStream(fileName);
                playmp3 = new SoundPlayerMP3(fis);
                threadMP3 = new Thread(playmp3);
                threadMP3.start();
            } catch (JavaLayerException e) {
                gui.showError("Not able to play the File");
            } catch (FileNotFoundException e) {
                gui.showError("File not found.");
            }
        } else {
            try {
                File audioFile = new File(fileName);
                player = new SoundPlayer(audioFile);
                thread = new Thread(player);
                thread.start();
            } catch (UnsupportedAudioFileException e) {
                gui.showError("Unsupported audio format");
            } catch (IOException e) {
                gui.showError("File not found. "+fileName);
            } catch (LineUnavailableException e) {
                gui.showError("Can't play audio");
            }
        }

    }

    public void stop() {
        if (thread != null && thread.isAlive()) {
            player.stopAudio();
        }
        if (playmp3 != null && threadMP3.isAlive()) {
            playmp3.stopAudio();
        }
    }
}
