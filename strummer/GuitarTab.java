
class GuitarTab {
  GuitarTab() {
    fretPosition = new int[6];
  }
  
  int getFretPosition(int stringIndex) {
    return fretPosition[stringIndex];
  }
  
  int[]  fretPosition;
  
  boolean changedFrom(GuitarTab other) {
    if (other == null) {
      return true;
    }
    
    for(int i=0;i<6;++i) {
      if (other.fretPosition[i] != fretPosition[i]) {
        return true;
      }
    }
    return false;
  }
}
