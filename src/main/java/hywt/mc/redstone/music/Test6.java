package hywt.mc.redstone.music;

import hywt.math.Line2D;
import hywt.math.Mapper;
import hywt.math.Point2D;
import hywt.mc.mcpack.FunctionWriter;
import hywt.mc.particle.ParticleExpression;
import hywt.mc.mcpack.cmdgen.PianoRollMusicGenerator;
import hywt.mc.mcpack.cmdgen.ShapeGenerator;
import hywt.midi.KeyboardLayout;
import hywt.midi.Note;
import hywt.midi.NoteMap;

import javax.sound.midi.InvalidMidiDataException;
import java.io.File;
import java.io.IOException;

public class Test6 extends PianoRollMusicGenerator {

    private ShapeGenerator generator;
    private ParticleExpression[] expressions;
    private int trackId;

    public Test6(double originX, double originY, double originZ, NoteMap noteMap, KeyboardLayout layout) {
        super(originX, originY, originZ, noteMap, layout);
    }

    public Test6(double originX, double originY, double originZ, File mid, KeyboardLayout layout)
            throws InvalidMidiDataException, IOException {
        super(originX, originY, originZ, mid, layout);
    }

    public static void main(String[] args) throws Exception {
        try {
            FunctionWriter writer = new FunctionWriter("test6",
                    "Z:\\JavaMC\\.minecraft\\saves\\Command Music");
            Test6 t = new Test6(27.5, 86, 8.5,
                    new File("Toby Fox - Home.mid"),
                    new KeyboardLayout(2 / 3d, 1));
            t.writeTo(writer);
            writer.close();
        } catch (InvalidMidiDataException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTrackStart(int trackNum) {
        trackId = trackNum % expressions.length;
    }

    @Override
    public void onInitialize() {
        generator = new ShapeGenerator(originX, originY, originZ);
        Mapper height = new Mapper(0, 1, 0, Math.PI);
        expressions = new ParticleExpression[]{
                (lineInfo, info) -> {
                    double distance = lineInfo.getLine().getLength();
                    Mapper m = new Mapper(0, 1, 0, distance * 2 * Math.PI);
                    double zFactor = m.map(info.getProgress());
                    return String.format("particleex normal fireworksSpark ~ ~%f ~%f %s 1 255 0 0 0 0 0 0 1",
                            Math.sin(zFactor) / 2 + Math.sin(height.map(info.getProgress())) * distance / 2,
                            Math.cos(zFactor) / 2,
                            Colors.LINE_COLORS[0].toCommandColor());
                },
                (lineInfo, info) -> {
                    double distance = lineInfo.getLine().getLength();
                    Mapper m = new Mapper(0, 1, 0, distance / 2 * Math.PI);
                    double zFactor = m.map(info.getProgress());
                    return String.format("particleex normal fireworksSpark ~ ~%f ~ %s 1 255 0 %f 0 0 0 0 1",
                            Math.sin(height.map(info.getProgress())) * distance / 2,
                            Colors.LINE_COLORS[1].toCommandColor(),
                            Math.sin(zFactor) / 8
                    );
                },
                (lineInfo, info) -> {
                    double distance = lineInfo.getLine().getLength();
                    return String.format("particleex normal fireworksSpark ~ ~%f ~ %s 1 255 0 0 0 0 0 0 1",
                            Math.sin(height.map(info.getProgress())) * distance / 2,
                            Colors.LINE_COLORS[2].toCommandColor());
                },
                (lineInfo, info) -> String.format("particleex normal fireworksSpark ~ ~ ~ %s 1 255 0 0 0 0 0 0 1",
                        Colors.LINE_COLORS[3].toCommandColor()),
                (lineInfo, info) -> {
                    int splits = lineInfo.getSplits();
                    Mapper m = new Mapper(0, splits / 2d, 0, 3d / 4 * Math.PI);
                    return String.format("particleex normal fireworksSpark ~ ~%f ~ %s 1 255 0 0 0 0 0 0 1",
                            Math.sin(Math.abs(m.map(info.getTick() > splits / 2d ? splits - info.getTick() : info.getTick()))) * (splits / 32),
                            Colors.LINE_COLORS[4].toCommandColor());
                },
        };
        add(0, "tp @s ~ ~ ~ -90 46");
    }

    @Override
    public void onNote(long tick, Note note) {
        Point2D point = layout.getPoint(tick, note);
        int id = note.getTrack() % 2 == 0 ? 1 : 13;
        String setblock = String.format("setblock ~ ~ ~ color_block:color_block 0 replace {Color:%d}", Colors.LINE_COLORS[trackId].toInteger());
        if (tick < 180)
            add(1, relativePos(point.x, 0, point.y) + setblock);
        else
            add(tick - 180, relativePos(point.x, 0, point.y) + setblock);
        add(tick, relativePos(point.x, 0, point.y) + "setblock ~ ~ ~ air");
        add(tick, relativePos(point.x, 0, point.y)
                + String.format("particleex conditional fireworksSpark ~ ~ ~ %s 1 240 0 0.5 0 0.5 0.5 0.5 ",
                Colors.COLORS[trackId].toCommandColor())
                + "(x>=0.5&y>=0.5)|(x>=0.5&y<=-0.5)|(x<=-0.5&y>=0.5)|(x<=-0.5&y<=-0.5)|(y>=0.5&z>=0.5)|(y>=0.5&z<=-0.5)|(y<=-0.5&z>=0.5)|(y<=-0.5&z<=-0.5)|(z>=0.5&x>=0.5)|(z>=0.5&x<=-0.5)|(z<=-0.5&x>=0.5)|(z<=-0.5&x<=-0.5)"
                + " 0.1 40");
        add(tick, relativePos(point.x, 0, point.y) +
                String.format("particleex parameter fireworksSpark ~ ~ ~ %s 1 240 0 0 0 0 100 x=sin(t)/2;z=cos(t)/2 0.5 60 vx=sin(x/20);vz=sin(z/20)",
                        Colors.LINE_COLORS[trackId].toCommandColor()));
    }

    @Override
    public void onLineUp(long startTick, long endTick, Note startNote, Note endNote) {
        if (endTick - startTick < 200) {
            Line2D line = layout.getLine(startTick, endTick, startNote, endNote);
            addAll(startTick, generator.tickLine(line, (int) (line.getLength() * 16), startTick, endTick,
                    expressions[trackId]));
        }
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
