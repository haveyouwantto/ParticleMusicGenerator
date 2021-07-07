package hywt.mc.mcpack.cmdgen;

import hywt.midi.Note;

import java.util.List;

public interface MusicEventHandler {
    List<String> onInitialize();

    List<String> onTrackStart(int trackNum);

    List<String> onNote(long tick, Note note);

    List<String> onLineUp(long startTick, long endTick, Note startNote, Note endNote);

    List<String> onTick(long tick);
}
