
class Guitar {
  // Pattersn from http://howmusicreallyworks.com/CompleteGuitarChordPoster_WayneChase_FreeEdition.pdf
  static final int majorPatterns[][] = { {3,3,2,0,1,0}, {1,4,3,1,2,1}, {2,0,0,2,3,2}, {3,6,5,3,4,3}, /*E*/{0,2,2,1,0,0}, {1,3,3,2,1,1}, {2,4,4,3,2,2}, {3,2,0,0,0,3}, {4,3,1,1,1,4}, /*A*/ {0,0,2,2,2,0}, {1,1,3,3,3,1}, {2,2,4,4,4,2} };
  static final int minorPatterns[][] = { {-1,-1,1,0,1,3}, {-1,-1,2,1,2,0}, {1,0,0,2,3,1}, {-1,1,1,3,4,2}, {0,2,2,0,0,0}, {1,3,3,1,1,1}, {2,4,4,2,2,2}, /*Gm*/{3,5,5,3,3,3}, {4,6,6,4,4,4}, {0,0,2,2,1,0}, {1,1,3,3,2,1}, {2,2,4,4,3,2}};
  
  Guitar(SoundGenerator output_) {
    int[] tuning = {40/*E2*/, 45/*A2*/, 50/*D3*/, 55/*G4*/, 59/*B4*/, 64/*E5*/};
    
    gitStrings = new GuitarString[6];  // 0 = bass string
    for(int i=0;i<6;++i) {
      gitStrings[i] = new GuitarString(tuning[i]);
    }
    
    output = output_;
  }
  
  void positionFinger(int onString, int atFret) {
    gitStrings[onString].muteString(output);
    gitStrings[onString].positionFinger(atFret);
  }
  
  void playString(int index) {
    gitStrings[index].playString(output);
  }
  
  void muteString(int index) {
    gitStrings[index].muteString(output);
  }
  
  void update() {
  }
  
  // TODO: Move to GuitarHelper
  GuitarTab buildChord(Chord chord) {
    GuitarTab tab = new GuitarTab();
    
    if (!chord.isChord()) {
      return tab;
    }
    
    // TODO: Handle inversions
    if (chord.type == ChordType.major) {
      for(int i=0;i<6;++i) {
        tab.fretPosition[i] =  majorPatterns[chord.root][i];
      } 
 
     } else if (chord.type == ChordType.minor) {
      for(int i=0;i<6;++i) {
        tab.fretPosition[i] = minorPatterns[chord.root][i];
      } 
    }
    
    // TODO: aug/dim/6/7/9/11
 
    return tab;
  }
  
  
  SoundGenerator  output;
  GuitarString[]  gitStrings;

}
