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
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Benjamin Brahmer
 */
public class SoundPlayer implements Runnable {

    private boolean stopClip = false;
    private final int BUFFER_SIZE = 4096;
    private final String audioFilePath;

    public SoundPlayer(String audioFilePath) {
        this.audioFilePath = audioFilePath;
    }

    @Override
    public void run() {
        File audioFile = new File(audioFilePath);

        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile)) {

            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            
            
            
            try (SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine(info)) {
                //BUFFER_SIZE = audioLine.getBufferSize();
                //System.out.println(BUFFER_SIZE);
                audioLine.open(format, BUFFER_SIZE);
                audioLine.start();
                System.out.println("Playback started.");
                byte[] bytesBuffer = new byte[BUFFER_SIZE];
                int bytesRead;

                while ((!stopClip && (bytesRead = audioStream.read(bytesBuffer)) != -1)) {
                    audioLine.write(bytesBuffer, 0, bytesRead);
                    
                }
                System.out.println("vor");
                audioLine.drain();
                System.out.println("nach");
                audioLine.stop();
                audioLine.close();

            } catch (LineUnavailableException ex) {
                System.out.println("Audio line for playing back is unavailable.");
                ex.printStackTrace();
            }

        } catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }

        System.out.println("Playback completed.");
    }

    public void stopAudio() {
        stopClip = true;

    }
}
