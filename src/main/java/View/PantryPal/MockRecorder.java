package PantryPal;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class MockRecorder implements Recorder {

    public AudioFormat getAudioFormat() {
        // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        return new AudioFormat(
                sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }

    // simply creates the audioFile for us
    public void startRecording() throws IOException {
        File audioFile = new File("src/main/java/View/PantryPal/recording.wav");
       // creates an empty audioInputStream
        AudioInputStream audioInputStream = new AudioInputStream(null, getAudioFormat(), 0);
        // writes recording.wav into the file system
        AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        audioFile);
    }

    // would normally stop the audio input and stop writing to the file, but since we don't have
    // microphone capabilities, not much can be done
    public void stopRecording() {
    }
    
}
