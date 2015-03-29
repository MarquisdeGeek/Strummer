import themidibus.*;

class SoundGeneratorMIDI extends SoundGenerator {
  SoundGeneratorMIDI(MidiBus bus_, int channel_) {
    bus  = bus_;
    channel = channel_;
  }
  
  
  void sendNoteOn(int pitch, int velocity) {
    bus.sendNoteOn(channel, pitch, velocity); 
  }
  
  void sendNoteOff(int pitch) {
    bus.sendNoteOn(channel, pitch, 0); 
  }
  
  void sendProgramChange(int instrument) {
    bus.sendMessage(0xC0, instrument, 0);
  }
  
  private MidiBus bus;
  private int channel;
}


