
class MIDIKeyboard {
  
static final int  MAX_CHANNELS   = 16;
static final int  MAX_NOTES   = 128;

  MIDIKeyboard() {
     notesOctave = new boolean [16][12];
    notesMap = new int[MAX_CHANNELS][MAX_NOTES];
  }
  
  void noteOn(int channel, int pitch, int velocity) {
    notesMap[channel][pitch] = velocity;
    notesOctave[channel][pitch%12] = true;
  }
  
  void noteOff(int channel, int pitch) {
    notesMap[channel][pitch] = 0;
    notesOctave[channel][pitch%12] = false;
  }
  
  // TODO: Upgrade this to return all possible options.
  // This is because C6 = Am7, among many others
  Chord deduceChord(int channel) {
    Chord chord = new Chord();
    
    for(int i=0;i<12;++i) {
      if (notesOctave[channel][i]) {
        if (notesOctave[channel][(i+4)%12] && notesOctave[channel][(i+7)%12]) {
          chord.type = ChordType.major;
        }
        if (notesOctave[channel][(i+3)%12] && notesOctave[channel][(i+7)%12]) {
          chord.type = ChordType.minor;
        }
        
        // Look for additions
        if (chord.type != ChordType.unknown) {
          if (notesOctave[channel][(i+9)%12]) {
            chord.add6 = true;
          }
          if (notesOctave[channel][(i+10)%12]) {
            chord.add7 = true;
          }
          if (notesOctave[channel][(i+17)%12]) {
            chord.add11 = true;
          }
        }
        
        // Try the 'exotic' chords
         if (notesOctave[channel][(i+4)%12] && notesOctave[channel][(i+8)%12]) {
          chord.type = ChordType.augmented;
        }
        if (notesOctave[channel][(i+3)%12] && notesOctave[channel][(i+6)%12] && notesOctave[channel][(i+9)%12]) {
          chord.type = ChordType.diminished;
        }
        
        // reach a formal conclusion
        if (chord.type != ChordType.unknown) {
          chord.root = i;
          break;  // TODO: Store multiple
        }
     }
    }
    return chord;
  }
  
  
  
  private boolean[][] notesOctave;
  private int[][] notesMap;
  
  
}
