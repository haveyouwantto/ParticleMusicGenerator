package hywt.mc.redstone.music;

import hywt.math.Line2D;
import hywt.math.Mapper;
import hywt.mc.mcpack.FunctionWriter;
import hywt.mc.mcpack.cmdgen.PianoRollMusicGenerator;
import hywt.mc.mcpack.cmdgen.ShapeGenerator;
import hywt.midi.KeyboardLayout;
import hywt.midi.Note;
import hywt.midi.NoteMap;

import javax.sound.midi.InvalidMidiDataException;
import java.io.File;
import java.io.IOException;

public class Test7 extends PianoRollMusicGenerator {

    private final Mapper MAPPER = new Mapper(0, 127, 0, 4 * Math.PI);
    private ShapeGenerator generator;

    public static void main(String[] args) throws Exception {
        try {
            FunctionWriter writer = new FunctionWriter("test7",
                    ".");
            Test7 t = new Test7(127.5, 66, 8.5,
                    new File("C:\\Users\\havey\\OneDrive\\projects\\mcws\\midis\\Touhou Mix\\Green-Eyed Jealousy_Normal.mid"),
                    new KeyboardLayout(2 / 3d, 1));
            t.writeTo(writer);
            writer.close();
        } catch (InvalidMidiDataException | IOException e) {
            e.printStackTrace();
        }
    }

    public Test7(double originX, double originY, double originZ, File midiFile, KeyboardLayout layout) throws InvalidMidiDataException, IOException {
        super(originX, originY, originZ, midiFile, layout);
    }

    public Test7(double originX, double originY, double originZ, NoteMap noteMap, KeyboardLayout layout) {
        super(originX, originY, originZ, noteMap, layout);
    }

    @Override
    public void onInitialize() {
        layout.setzOffset(-64);
        teleport = false;
        generator = new ShapeGenerator(originX, originY, originZ);
        add(0, String.format("tp @s %f %f %f 90 90", originX, originY + 40, originZ));
    }

    @Override
    public void onTrackStart(int trackNum) {

    }

    @Override
    public void onNote(long tick, Note note) {
        add(tick, relativePos(0, 0, 0) + String.format("particleex fireworksSpark ~ ~ ~ function %s 1 255 %f 0 %f 0.5 0.5 0.5 (x>=0.5&y>=0.5)|(x>=0.5&y<=-0.5)|(x<=-0.5&y>=0.5)|(x<=-0.5&y<=-0.5)|(y>=0.5&z>=0.5)|(y>=0.5&z<=-0.5)|(y<=-0.5&z>=0.5)|(y<=-0.5&z<=-0.5)|(z>=0.5&x>=0.5)|(z>=0.5&x<=-0.5)|(z<=-0.5&x>=0.5)|(z<=-0.5&x<=-0.5) 0.1 40",
                Colors.COLORS[note.getTrack() % Colors.COLORS.length].toCommandColor(),
                Math.sin(MAPPER.map(note.getPitch())) * 2,
                Math.cos(MAPPER.map(note.getPitch())) * 2
        ));
    }

    @Override
    public void onLineUp(long startTick, long endTick, Note startNote, Note endNote) {
        if (endTick - startTick < 200) {
            Line2D line = new Line2D(0, 0, Math.sin(MAPPER.map(endNote.getPitch())) * 20,
                    Math.cos(MAPPER.map(endNote.getPitch())) * 20);
            addAll(endTick, generator.line(line, (int) (line.getLength() * 16),
                    (lineInfo, info) -> String.format("particleex fireworksSpark ~ ~ ~ normal %s 1 255 0 0 0 0 0 0 1",
                            Colors.LINE_COLORS[endNote.getTrack() % Colors.LINE_COLORS.length].toCommandColor())));
        }
    }

    @Override
    public void onTick(long tick) {
        add(tick, relativePos(0, 40, 0) + "particle endRod ~ ~ ~ 20 20 20 0.1 25");
        add(tick, relativePos(0, 0, 0) +
                "particleex endRod ~ ~ ~ parameter 1 1 1 1 240 0 0 0 0 20 x=cos(t)*20;z=sin(t)*20 0.025 5");
    }
}
