package PantryPal;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;

/**
 * Recorder
 */
interface Recorder {
    AudioFormat getAudioFormat();
    void startRecording() throws IOException;
    void stopRecording();
}
