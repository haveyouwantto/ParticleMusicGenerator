package hywt.mc.mcpack.cmdgen;

import hywt.midi.Note;
import hywt.midi.NoteMap;

import java.util.*;

public class NoteGenerator implements CommandGenerator {
    protected NoteMap noteMap;

    public NoteGenerator(NoteMap noteMap) {
        this.noteMap = noteMap;
    }

    @Override
    public Map<Long, Collection<String>> generate() {
        Map<Long, Collection<String>> map = new TreeMap<>();
        for (int i = 0; i < noteMap.getNotes().size(); i++) {
            TreeMap<Long, List<Note>> inMap = noteMap.getNotes().get(i);
            for (Map.Entry<Long, List<Note>> entry : inMap.entrySet()) {
                map.putIfAbsent(entry.getKey(), new ArrayList<>());
                for (Note note : entry.getValue()) {
                    map.get(entry.getKey()).add(format(note));
                }
            }
        }
        return map;
    }

    protected String format(Note note) {
        return String.format("playsound %d.%d record @s ~ ~ ~ %f", 1, note.getPitch(), note.getVolume() / 128f);
    }
}
