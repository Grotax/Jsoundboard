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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javazoom.jl.player.Player;

/**
 *
 * @author Benjamin Brahmer
 */
public class SoundPlayer implements Runnable {

    private boolean stopClip = false;
    private final int BUFFER_SIZE = 4096;
    private final AudioInputStream audioStream;
    private final AudioFormat format;
    private final DataLine.Info info;
    private final SourceDataLine audioLine;

    public SoundPlayer(File audioFile) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audioStream = AudioSystem.getAudioInputStream(audioFile);
        format = audioStream.getFormat();
        info = new DataLine.Info(SourceDataLine.class, format);
        audioLine = (SourceDataLine) AudioSystem.getLine(info);
    }

    @Override
    public void run() {

        try {
            audioLine.open(format, BUFFER_SIZE);
            audioLine.start();
            System.out.println("Playback started.");
            byte[] bytesBuffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((!stopClip && (bytesRead = audioStream.read(bytesBuffer)) != -1)) {
                audioLine.write(bytesBuffer, 0, bytesRead);

            }
            audioLine.drain();
            audioLine.stop();
            audioLine.close();

            System.out.println("Playback completed.");
        } catch (LineUnavailableException | IOException ex) {
            Logger.getLogger(SoundPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void stopAudio() {
        stopClip = true;

    }
}
