package PantryPal;

import java.io.IOException;
import java.net.URISyntaxException;

interface Whisper {
    String whisperTranscribe(String fileName) throws IOException, URISyntaxException;
}
