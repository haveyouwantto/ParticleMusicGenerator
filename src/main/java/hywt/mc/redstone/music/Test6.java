package hywt.mc.redstone.music;

import hywt.math.Line2D;
import hywt.math.Mapper;
import hywt.math.Point2D;
import hywt.mc.mcpack.FunctionWriter;
import hywt.mc.mcpack.cmdgen.ParticleExpression;
import hywt.mc.mcpack.cmdgen.PianoRollMusicGenerator;
import hywt.mc.mcpack.cmdgen.ShapeGenerator;
import hywt.midi.KeyboardLayout;
import hywt.midi.Note;
import hywt.midi.NoteMap;

import javax.sound.midi.InvalidMidiDataException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test6 extends PianoRollMusicGenerator {

    private final ShapeGenerator generator;
    private ParticleExpression[] expressions;
    private String[] colors;
    private String[] lineColors;
    private int trackId;

    public Test6(double originX, double originY, double originZ, NoteMap noteMap, KeyboardLayout layout) {
        super(originX, originY, originZ, noteMap, layout);
        generator = new ShapeGenerator(originX, originY, originZ);
    }

    public Test6(double originX, double originY, double originZ, File mid, KeyboardLayout layout)
            throws InvalidMidiDataException, IOException {
        super(originX, originY, originZ, mid, layout);
        generator = new ShapeGenerator(originX, originY, originZ);
    }

    public static void main(String[] args) {
        try {
            FunctionWriter writer = new FunctionWriter("test6",
                    "I:\\Minecraft\\Java版\\.minecraft\\saves\\Command Music");
            Test6 t = new Test6(27.5, 66, 8.5,
                    new File("C:\\Users\\havey\\OneDrive\\projects\\mcws\\midis\\Touhou Mix\\Green-Eyed Jealousy_Normal.mid"),
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
        layout.setzOffset(-64);

        teleportGenerator.setHeight(25);
        teleportGenerator.setDistance(-30);
        colors = new String[]{"1 0.4 0.65", "0.21 1 0.83", "0.1 0.31 1", "0.81 0.91 0.61", "1 0.3 1"};
        lineColors = new String[]{"1 0.1 0.31", "0.1 1 0.31", "0 0 1", "0.81 0.91 0.31", "1 0.15 1"};
        expressions = new ParticleExpression[]{
                (splits, tick) -> {
                    Mapper m = new Mapper(0, 1, 0, (int) (splits / 16) * 2 * Math.PI);
                    Mapper height = new Mapper(0, splits, 0, Math.PI);
                    double zFactor = m.map(tick * 1d / splits);
                    return String.format("particleex fireworksSpark ~ ~%f ~%f normal %s 1 255 0 0 0 0 0 0 1",
                            Math.sin(zFactor) / 2 + Math.sin(height.map(tick)) * (splits / 32d),
                            Math.cos(zFactor) / 2,
                            lineColors[0]);
                },
                (splits, tick) -> {
                    Mapper m = new Mapper(0, 1, 0, (int) (splits / 32) * 2 * Math.PI);
                    Mapper height = new Mapper(0, splits, 0, Math.PI);
                    double zFactor = m.map(tick * 1d / splits);
                    return String.format("particleex fireworksSpark ~ ~%f ~ normal %s 1 255 0 %f 0 0 0 0 1",
                            Math.sin(height.map(tick)) * (splits / 16),
                            lineColors[1],
                            Math.sin(zFactor) / 8);
                },
                (splits, tick) -> {
                    Mapper height = new Mapper(0, splits, 0, Math.PI);
                    return String.format("particleex fireworksSpark ~ ~%f ~ normal %s 1 255 0 0 0 0 0 0 1",
                            Math.sin(height.map(tick)) * (splits / 16) / 2,
                            lineColors[2]);
                },
                (splits, tick) -> String.format("particleex fireworksSpark ~ ~ ~ normal %s 1 255 0 0 0 0 0 0 1",
                        lineColors[3]),
                (splits, tick) -> {
                    Mapper m = new Mapper(0, splits / 2d, 0, 3d / 4 * Math.PI);
                    Mapper height = new Mapper(0, splits, 0, Math.PI);
                    return String.format("particleex fireworksSpark ~ ~%f ~ normal %s 1 255 0 0 0 0 0 0 1",
                            Math.sin(Math.abs(m.map(tick > splits / 2d ? splits - tick : tick))) * (splits / 32),
                            lineColors[4]);
                },
        };
        add(0, "tp @s ~ ~ ~ -90 46");
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
                + String.format("particleex fireworksSpark ~ ~ ~ function %s 1 240 0 0.5 0 0.5 0.5 0.5 ",
                colors[trackId])
                + "(x>=0.5&y>=0.5)|(x>=0.5&y<=-0.5)|(x<=-0.5&y>=0.5)|(x<=-0.5&y<=-0.5)|(y>=0.5&z>=0.5)|(y>=0.5&z<=-0.5)|(y<=-0.5&z>=0.5)|(y<=-0.5&z<=-0.5)|(z>=0.5&x>=0.5)|(z>=0.5&x<=-0.5)|(z<=-0.5&x>=0.5)|(z<=-0.5&x<=-0.5)"
                + " 0.1 40");
        add(tick, relativePos(point.x, 0, point.y) +
                String.format("particleex fireworksSpark ~ ~ ~ parameter %s 1 240 0 0 0 0 100 x=sin(t)/2;z=cos(t)/2 0.5 60 vx=sin(x/20);vz=sin(z/20)",
                        lineColors[trackId]));
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
