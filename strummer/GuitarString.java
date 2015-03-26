

class GuitarString {
  int tuned;   // open string
  int fret;    // where's the finger
  boolean  muted;  // do not play
  
  final String[] noteNames = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
  
  GuitarString(int openStringPitch) {
    muted = true;
    tuned = openStringPitch;
    fret = 0;
  }
  
  int getMIDIPitch() {
    return muted ? 0 : tuned + fret;
  }
  
    boolean isStringMuted() {
    return muted;
  }
  
  String asString() {
    int pitch = getMIDIPitch();
    if (pitch != 0) {
      return noteNames[pitch%12] + pitch/12;
    }
    
    return "None";
  }
}
  
