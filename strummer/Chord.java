

class Chord {
  int  root;
  ChordType  type;
  boolean    add6;
  boolean    add7;
  boolean    add9;
  boolean    add11;
  
  Chord() {
    root = -1;  // do not use this to determine a valid chord; it's defensive
    type = ChordType.unknown;
    add6 = add7 = add9 = add11 = false;
  }
  
  boolean isChord() {
    return type == ChordType.unknown ? false : true;
  }
  
  String asString(boolean inKey) { 
    if (type == ChordType.unknown) {
      return "Unkown";
    }
    // TODO: Use 'inKey' to switch these to the correct names for the given key
    String[] nameList = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
    String name = nameList[root];
    
    if (type == ChordType.minor) {
          name += "m";
    } else if (type == ChordType.augmented) {
          name += "aug";
    } else if (type == ChordType.diminished) {
          name += "dim";
    }  
    
    if (add6) {
      name += "6";
    }
    
    if (add7) {
      name += "7";
    }
    
    if (add7 && add9) {
      name += "9";
    }
    if (!add7 && add9) {
      name += "add9";
    }
    if (add11) {
      name += "add11";
    }
    
    return name;
  }
}
