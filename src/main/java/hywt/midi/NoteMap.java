package hywt.midi;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeMap;

public class NoteMap {

    private final List<TreeMap<Long, List<Note>>> notes;
    private long length;

    public NoteMap(List<TreeMap<Long, List<Note>>> notes) {
        this.notes = notes;
        length = 0;
        for(TreeMap<Long, List<Note>> noteMap : notes){
            try {
                length = Math.max(length, noteMap.lastKey());
            } catch (NoSuchElementException ignored) {
            }
        }
    }

    public int getTrackNum() {
        return notes.size();
    }

    public List<TreeMap<Long, List<Note>>> getNotes() {
        return notes;
    }

    public long length(){
        return length;
    }

    @Override
    public String toString() {
        return "NoteMap{" +
                "notes=" + notes +
                '}';
    }
}
