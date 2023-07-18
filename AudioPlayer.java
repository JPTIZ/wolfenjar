//=============================================================================
// AudioPlayer
//-----------------------------------------------------------------------------
// Static methods for audio (BGM/SFX) playing.
//=============================================================================

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.net.URL;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;

public class AudioPlayer {

	private static Sequencer sequencer;

	public static void playBGM(String filename) {
        try {
        	if (AudioPlayer.sequencer == null) {
                AudioPlayer.sequencer = MidiSystem.getSequencer();
                AudioPlayer.sequencer.open();
            }

            InputStream is = new BufferedInputStream(new FileInputStream(new File("sound/"+filename)));

            AudioPlayer.sequencer.setSequence(is);
            AudioPlayer.sequencer.setLoopCount(900); // A big number that probably won't play all those times

            if (!Game.isMuted()) AudioPlayer.sequencer.start();
        } catch (Exception e) {
            Game.log("Failed to play \""+filename+"\": "+e.getMessage());
        }
    }

    public static void pauseBGM() {
        Game.log("Audio OFF");
        if (AudioPlayer.sequencer != null) AudioPlayer.sequencer.stop();
    }

    public static void continueBGM() {
        Game.log("Audio ON");
        if (AudioPlayer.sequencer != null) AudioPlayer.sequencer.start();
    }

    public static void loopSFX(String filename) {
        if (Game.isMuted()) return;
        try {
            // Open an audio input stream.
            File soundFile = new File("sound/"+filename);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public static void playSFX(String filename) {
        if (Game.isMuted()) return;
		try {
			// Open an audio input stream.
			File soundFile = new File("sound/"+filename);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
			// Get a sound clip resource.
			Clip clip = AudioSystem.getClip();
			// Open audio clip and load samples from the audio input stream.
			clip.open(audioIn);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}