package hywt.mc.mcpack.cmdgen;

import hywt.mc.mcpack.FunctionWriter;
import hywt.midi.KeyboardLayout;
import hywt.midi.Note;
import hywt.midi.NoteMap;
import hywt.midi.SimpleParser;

import javax.sound.midi.InvalidMidiDataException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class PianoRollMusicGenerator extends CoorCommandGenerator {

    private final NoteMap noteMap;
    private final Map<Long, Collection<String>> map;
    protected final TeleportGenerator teleportGenerator;
    protected KeyboardLayout layout;
    protected boolean teleport;

    public PianoRollMusicGenerator(double originX, double originY, double originZ, File midiFile, KeyboardLayout layout)
        throws InvalidMidiDataException, IOException {
        this(originX, originY, originZ, new SimpleParser().toMCTick(midiFile), layout);
    }

    public PianoRollMusicGenerator(double originX, double originY, double originZ, NoteMap noteMap,
                                   KeyboardLayout layout) {
        super(originX, originY, originZ);
        this.noteMap = noteMap;
        this.layout = layout;
        map = new TreeMap<>();
        teleportGenerator = new TeleportGenerator(originX, originY, originZ, layout, noteMap.length());
        teleport = true;
    }

    public NoteMap getNoteMap() {
        return noteMap;
    }

    public KeyboardLayout getLayout() {
        return layout;
    }

    public void setLayout(KeyboardLayout layout) {
        this.layout = layout;
    }

    public TeleportGenerator getTeleportGenerator() {
        return teleportGenerator;
    }

    public void add(long tick, String command) {
        map.putIfAbsent(tick, new ArrayList<>());
        map.get(tick).add(command);
    }

    public void addAll(long tick, Collection<String> cmds) {
        map.putIfAbsent(tick, new ArrayList<>());
        map.get(tick).addAll(cmds);
    }

    public void addAll(long startTick, Map<Long, Collection<String>> cmds) {
        for (Map.Entry<Long, Collection<String>> entry : cmds.entrySet())
            addAll(entry.getKey() + startTick, entry.getValue());
    }

    public void addAll(long startTick, CommandGenerator generator) {
        addAll(startTick, generator.generate());
    }

    public void writeTo(FunctionWriter writer) throws IOException {
        generate();
        for (Map.Entry<Long, Collection<String>> entry : map.entrySet()) {
            if (writer.getTick() < entry.getKey())
                writer.skipTo(entry.getKey());
            for (String string : entry.getValue())
                writer.write(string);
        }
    }

    @Override
    public Map<Long, Collection<String>> generate() {
        onInitialize();
        addAll(0, new NoteGenerator(noteMap));
        if (teleport)
            addAll(0, teleportGenerator);
        for (int i = 0; i < noteMap.getNotes().size(); i++) {
            onTrackStart(i);
            long lastTick = 0;
            List<Note> lastNotes = new ArrayList<>();
            lastNotes.add(new Note(0, 0, 64, 0));
            for (Map.Entry<Long, List<Note>> entry : noteMap.getNotes().get(i).entrySet()) {
                long tick = entry.getKey();
                List<Note> notes = entry.getValue();

                Collections.sort(notes);
                for (int j = 0; j < notes.size(); j++) {

                    Note note = notes.get(j);
                    Note lastNote = lastNotes.get(j % lastNotes.size());

                    onNote(tick, note);
                    onLineUp(lastTick, tick, lastNote, note);
                }
                lastTick = tick;
                lastNotes = notes;
            }
        }

        for (int i = 0; i < noteMap.length(); i++)
            onTick(i);

        return map;
    }

    public abstract void onInitialize();

    public abstract void onTrackStart(int trackNum);

    public abstract void onNote(long tick, Note note);

    public abstract void onLineUp(long startTick, long endTick, Note startNote, Note endNote);

    public abstract void onTick(long tick);
}
