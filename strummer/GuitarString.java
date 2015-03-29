import themidibus.*;
import processing.serial.*;
import cc.arduino.*;


class GuitarString {
  // Setup. Generally does not change:
  private int tuned;   // open string
  
  // State
  private int fret;    // where's the finger. -1 = muted
  private long last_played_at;  // milli time of when string was struck
  private int lastPitch;
  
  // Data
  final String[] noteNames = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
  
  GuitarString(int openStringPitch) {
    tuned = openStringPitch;
    fret = 0;
    lastPitch = 0;
    last_played_at = 0;
  }
  
  int getMIDIPitch() {
    return fret < 0 ? 0 : tuned + fret;
  }
  
  boolean isStringMuted() {
    return fret < 0;
  }
  
  void positionFinger(int atFret) {
    fret = atFret;
  }
  
  void playString(SoundGenerator output) {
     
    int pitch = getMIDIPitch();
    if (pitch == 0) {
      output.sendNoteOff(lastPitch);
    } else {
      output.sendNoteOn(pitch, 120);
      lastPitch = pitch;
    }
 
    last_played_at = System.currentTimeMillis();
  }
  
  void muteString(SoundGenerator output) {
    last_played_at = 0;

    if (lastPitch != 0) {
      output.sendNoteOff(lastPitch);    
    }

  }
  
  String asString() {
    int pitch = getMIDIPitch();
    if (pitch != 0) {
      return noteNames[pitch%12] + pitch/12;
    }
    
    return "None";
  }
  
}
  
