
class GuitarTab {
  GuitarTab() {
    // tuning[0] = bass E, bottom string
    // from // http://www.contrabass.com/pages/frequency.html
    int[] tuning = {40/*E2*/, 45/*A2*/, 50/*D3*/, 55/*G4*/, 59/*B4*/, 64/*E5*/};
    
    gitStrings = new GuitarString[6];  // 0 = bass string
    for(int i=0;i<6;++i) {
      gitStrings[i] = new GuitarString(tuning[i]);
    }
   }
  
  boolean isStringMuted(int stringIndex) {
    return gitStrings[stringIndex].isStringMuted();
  }
  
  int getMIDIPitch(int stringIndex) {
    return gitStrings[stringIndex].getMIDIPitch();
  }
  
  GuitarString[] gitStrings;
}
