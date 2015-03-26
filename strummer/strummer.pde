import themidibus.*;
import processing.serial.*;
import cc.arduino.*;

//
// Strummer
// Copyright 2015 - Steven Goodwin - @marquisdegeek
// http://www.marquisdegeek.com/making_guitar
// Released under the GPL, v2
//
// 1. Auto-strum mode : when a chord is played, the computer strums each note for you
// 2. Normal mode : uses Arduino input to determine which strings are played
class Config {
  Config() {
    bModeAutoStrum = false;
  }
  
  boolean bModeAutoStrum;
}

class DeviceState {
  // When using Arduino-based manual strumming
  long notesOffAt[] = {0,0,0,0,0,0};
}

DeviceState g_State = new DeviceState();
Config      g_Config = new Config();

Arduino arduino;
int pinStringBass = 3;
int pinStringTop = 8;
boolean[] pinLastState = new boolean[8];

MIDIKeyboard keyboard;
Guitar guitar;
GuitarTab lastGuitarTab;

void setup() {
  size(400, 400);
  background(0);
  
  MidiBus.list(); // List all available Midi devices on STDOUT. This will show each device's index and name.
  MidiBus myBus = new MidiBus(this, "SB Audigy 2 MIDI IO [CF80]", "SB Audigy 2 Synth A [CF80]");
  
  //
  keyboard = new MIDIKeyboard(myBus);
  keyboard.sendProgramChange(0, 25);// send guitar : http://www.midi.org/techspecs/gm1sound.php 
  //
  guitar = new Guitar();

  // Do arduino last, because it takes ages!
  String[] arduinoList = Arduino.list();
  println(arduinoList);
  if (arduinoList.length > 1) {
    arduino = new Arduino(this, arduinoList[1], 57600);
    for(int i=pinStringBass;i<=pinStringTop;++i) {
      arduino.pinMode(i, Arduino.INPUT);
      pinLastState[i-pinStringBass] = false;
    }
  }
}

void draw() {
  handleManualStrum(0);
}


void noteOn(int channel, int pitch, int velocity) {
  // Receive a noteOn
 
  keyboard.noteOn(channel, pitch, velocity);
  onKeyboardChanged(channel, velocity);
}

void noteOff(int channel, int pitch, int velocity) {
  // Receive a noteOff
  keyboard.noteOff(channel, pitch);
  onKeyboardChanged(channel, 0);
}

void controllerChange(int channel, int number, int value) {
  // Receive a controllerChange
}

void delay(int time) {
  int current = millis();
  while (millis () < current+time) Thread.yield();
}

void handleManualStrum(int channel) {
  if (g_Config.bModeAutoStrum || lastGuitarTab == null || arduino == null) {  
    return;
  }
  
  // Look for which strings should trigger which notes
  long t = millis();
  for(int s=0;s<6;++s) {  
    int pinState = arduino.digitalRead(pinStringBass + s);
     if (pinState == Arduino.HIGH && !pinLastState[s]) {
        
        int p = lastGuitarTab.getMIDIPitch(s);  // we could also use isStringMuted
        if (p != 0) {
          keyboard.sendNoteOn(channel, p, 120);
          g_State.notesOffAt[s] = t + 1500;
        } else {
          g_State.notesOffAt[s] = 0;
        }
     }
     pinLastState[s] = pinState == Arduino.HIGH;
  } 
  //
  clearAllOldNotes(channel);
}

void clearAllOldNotes(int channel) {
  if (lastGuitarTab == null) {
    return;
  }
  //
  long t = millis();
  for(int s=0;s<6;++s) {  
       int p = lastGuitarTab.getMIDIPitch(s);  // we could also use isStringMuted
       if (p != 0 && t > g_State.notesOffAt[s]) {
            keyboard.sendNoteOff(channel, p);
       }
  }
}


void onKeyboardChanged(int channel, int velocity) {
  // This is a simple handler, where a whole chord needs to be held down
  // for any notes to play. With work, this can be changed so that, for example
  // a C & E of C chord would play only the C/E notes from the guitar tab.
  // But for now, whenever a chord is changed, we release all the previous notes.
  if (lastGuitarTab != null) {
    for(int s=0;s<6;++s) {      
      int p = lastGuitarTab.getMIDIPitch(s);  // we could also use isStringMuted
      if (p != 0) {
        // If the notes don't sustain when 'off'd, then remove this.
        // (this is probably most devices)
        //keyboard.sendNoteOff(channel, p);
        // TODO: A better approach might be to only stop the note when another one on the same string is played
        // Currently we just wait a short time before stopping it
      }
    }
    //
    lastGuitarTab = null;
  }
      
  // Now see if there's a new chord
  Chord chord = keyboard.deduceChord(channel);
  
  if (chord.isChord()) {
    println(chord.asString(false));

    GuitarTab tab = guitar.buildChord(chord);

    if (g_Config.bModeAutoStrum) {
      int startAt = 0;
      int endAt = 6;
      int delta = 1;
      
      // In a random direction
      if (random(100) > 50) {
        startAt = 5;
        endAt = -1;
        delta = -1;
      }
      
      for(int s=startAt;s!=endAt;s+=delta) {      
        int p = tab.getMIDIPitch(s);
        if (p != 0) {
          keyboard.sendNoteOn(channel, p, velocity);
          delay((int)(25+random(20)));
        }
      }
    } else {  // use the arduino to play individual notes
      // new notes not handled here - see handleArduinoStrum() for code
    }
    
    lastGuitarTab = tab;
  }

}
