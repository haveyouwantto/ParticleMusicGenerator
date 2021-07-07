package hywt.mc.redstone.music;

import hywt.math.Color3;
import hywt.math.Line2D;
import hywt.math.Point2D;
import hywt.mc.mcpack.FunctionWriter;
import hywt.mc.mcpack.cmdgen.PianoRollMusicGenerator;
import hywt.mc.mcpack.cmdgen.ShapeGenerator;
import hywt.midi.KeyboardLayout;
import hywt.midi.Note;
import hywt.midi.NoteMap;

import javax.sound.midi.InvalidMidiDataException;
import java.io.File;
import java.io.IOException;

public class Test5 extends PianoRollMusicGenerator {

    private final ShapeGenerator generator;
    Color3 color;

    public Test5(double originX, double originY, double originZ, NoteMap noteMap, KeyboardLayout layout) {
        super(originX, originY, originZ, noteMap, layout);
        generator = new ShapeGenerator(originX, originY, originZ);
    }

    public Test5(double originX, double originY, double originZ, File mid, KeyboardLayout layout)
        throws InvalidMidiDataException, IOException {
        super(originX, originY, originZ, mid, layout);
        generator = new ShapeGenerator(originX, originY, originZ);
    }

    public static void main(String[] args) throws Exception {
        try {
            FunctionWriter writer = new FunctionWriter("test5",
                    "Z:\\JavaMC\\.minecraft\\saves\\Command Music");
            Test5 t = new Test5(27.5, 66, 8.5, new File("Toby Fox - Home.mid"),
                    new KeyboardLayout(2 / 3d, 1));
            t.writeTo(writer);
            writer.close();
        } catch (InvalidMidiDataException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNote(long tick, Note note) {
        Point2D point = layout.getPoint(tick, note);
        int id = note.getTrack() % 2 == 0 ? 1 : 13;
        if (tick < 180)
            add(1, relativePos(point.x, 0, point.y) + "setblock ~ ~ ~ concrete " + id);
        else
            add(tick - 180, relativePos(point.x, 0, point.y) + "setblock ~ ~ ~ concrete " + id);
        add(tick, relativePos(point.x, 0, point.y) + "setblock ~ ~ ~ air");
        add(tick, relativePos(point.x, 0, point.y)
            + String.format("particleex conditional fireworksSpark ~ ~ ~ %s 1 240 0 0.5 0 0.5 0.5 0.5 ", color.toCommandColor())
            + "(x>=0.5&y>=0.5)|(x>=0.5&y<=-0.5)|(x<=-0.5&y>=0.5)|(x<=-0.5&y<=-0.5)|(y>=0.5&z>=0.5)|(y>=0.5&z<=-0.5)|(y<=-0.5&z>=0.5)|(y<=-0.5&z<=-0.5)|(z>=0.5&x>=0.5)|(z>=0.5&x<=-0.5)|(z<=-0.5&x>=0.5)|(z<=-0.5&x<=-0.5)"
            + " 0.1 40");
        // "particleex endRod ~ ~ ~ function 1 0.4 0.65 1 240 0 0 0 1 1 1
        // (abs(x)+abs(y)+abs(z)<1) 0.1 40");
    }

    @Override
    public void onLineUp(long startTick, long endTick, Note startNote, Note endNote) {
        if (endTick - startTick < 200) {
            Line2D line = layout.getLine(startTick, endTick, startNote, endNote);
            addAll(startTick, generator.tickLine(line, (int) (line.getLength() * 8), startTick, endTick));
        }
    }

    @Override
    public void onTrackStart(int trackNum) {
        color = (trackNum % 2 == 0) ? new Color3(1, 0.4, 0.65) : new Color3(0.21, 1, 0.83);
    }

    @Override
    public void onInitialize() {
        layout.setzOffset(-64);
        teleportGenerator.setHeight(25);
        teleportGenerator.setDistance(-30);
        add(0, "tp @s ~ ~ ~ -90 46");
    }

    @Override
    public void onTick(long tick) {
        if (tick < 180)
            add(0, relativePos(layout.getX(tick) + 2, 0, layout.getZ(0)) + "fill ~ ~ ~ ~ ~ ~127 air");
        else
            add(tick - 180, relativePos(layout.getX(tick) + 2, 0, layout.getZ(0)) + "fill ~ ~ ~ ~ ~ ~127 air");
    }

    @Override
    protected String relativePos(double x, double y, double z) {
        return String.format("execute @s %f %f %f ", originX + x, originY + y, originZ + z);
    }
}
