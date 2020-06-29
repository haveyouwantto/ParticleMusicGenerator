package hywt.midi;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SimpleParser {
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final int SET_TEMPO = 0x51;

    int tempo;
    int ppq;

    public NoteMap toMCTick(File file) throws InvalidMidiDataException, IOException {
        List<Map<Long, List<MIDIEvent>>> trackList = load(file);
        List<TreeMap<Long, List<Note>>> out = new ArrayList<>(trackList.size());
        /*long key = (long) (getTimeMillis(event.getTick()) / 50);*/
        for (int i = 0; i < trackList.size(); i++) {
            out.add(new TreeMap<>());
            Map<Long, List<MIDIEvent>> map = trackList.get(i);
            for (Map.Entry<Long, List<MIDIEvent>> entry : map.entrySet()) {
                List<MIDIEvent> events = entry.getValue();
                for (MIDIEvent event : events) {
                    if (event instanceof TempoChange) tempo = ((TempoChange) event).getTempo();
                    else if (event instanceof Note) {
                        long key = (long) (getTimeMillis(entry.getKey()) / 50);
                        out.get(i).putIfAbsent(key, new ArrayList<>());
                        out.get(i).get(key).add((Note) event);
                    }
                }
            }
        }
        return new NoteMap(out);
    }

    private List<Map<Long, List<MIDIEvent>>> load(File file) throws InvalidMidiDataException, IOException {
        boolean setTempo = false;
        Sequence seq = MidiSystem.getSequence(file);
        int trackNumber = seq.getTracks().length;
        List<Map<Long, List<MIDIEvent>>> midiMap = new ArrayList<>();
        for (int i = 0; i < trackNumber; i++) {
            midiMap.add(new TreeMap<>());
        }
        int tracknum = -1;
        ppq = seq.getResolution();
        for (Track track : seq.getTracks()) {
            tracknum++;
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();

                if (message instanceof MetaMessage) {
                    MetaMessage mm = (MetaMessage) message;
                    if (mm.getType() == SET_TEMPO && !setTempo) {
                        byte[] data = mm.getData();
                        int tempo = (data[0] & 0xff) << 16 | (data[1] & 0xff) << 8 | (data[2] & 0xff);
                        this.tempo = tempo;
                        midiMap.get(tracknum).putIfAbsent(event.getTick(), new ArrayList<>());
                        midiMap.get(tracknum).get(event.getTick()).add(new TempoChange(tempo));
                        setTempo = true;
                    }
                }
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() == NOTE_ON && sm.getData2() != 0) {
                        midiMap.get(tracknum).putIfAbsent(event.getTick(), new ArrayList<>());
                        midiMap.get(tracknum).get(event.getTick()).add(new Note(
                            tracknum,
                            0,
                            sm.getData1(),
                            sm.getData2()
                        ));
                    }
                }
            }
        }
        return midiMap;
    }

    // FIXME return correct time
    private double getTimeMillis(long tick) {
        return tick * (tempo * 1d / ppq) / 1000;
    }
}
