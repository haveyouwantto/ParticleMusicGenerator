package hywt.mc.redstone.music;

import hywt.math.Color3;
import hywt.math.Line2D;
import hywt.math.Mapper;
import hywt.math.Point2D;
import hywt.math.tensor.Vector2D;
import hywt.math.tensor.Vector3D;
import hywt.mc.mcpack.FunctionWriter;
import hywt.mc.mcpack.cmdgen.PianoRollMusicGenerator;
import hywt.mc.mcpack.cmdgen.ShapeGenerator;
import hywt.mc.particle.LineInfo;
import hywt.mc.particle.ParticleExpression;
import hywt.mc.particle.ParticleInfo;
import hywt.midi.KeyboardLayout;
import hywt.midi.Note;
import hywt.midi.NoteMap;

import javax.sound.midi.InvalidMidiDataException;
import java.io.File;
import java.io.IOException;

public class Test9 extends PianoRollMusicGenerator {
    private ShapeGenerator generator;
    private final Vector3D zp = new Vector3D(0, 0, 1);
    private int trackId;
    private double[][] pattern = {
            {0, 16}, {1, 15}, {-1, 15}, {1, 14}, {-1, 14}, {2, 13}, {-2, 13}, {3, 12}, {-3, 12}, {3, 11}, {-4, 11}, {4, 10}, {-4, 10}, {5, 9}, {0, 9}, {-5, 9}, {15, 8}, {14, 8}, {13, 8}, {12, 8}, {11, 8}, {10, 8}, {9, 8}, {8, 8}, {7, 8}, {6, 8}, {0, 8}, {-6, 8}, {-7, 8}, {-8, 8}, {-9, 8}, {-10, 8}, {-11, 8}, {-12, 8}, {-13, 8}, {-14, 8}, {-15, 8}, {14, 7}, {1, 7}, {-1, 7}, {-14, 7}, {14, 6}, {1, 6}, {-1, 6}, {-14, 6}, {13, 5}, {1, 5}, {-1, 5}, {-13, 5}, {12, 4}, {1, 4}, {-1, 4}, {-12, 4}, {12, 3}, {2, 3}, {-2, 3}, {-12, 3}, {11, 2}, {3, 2}, {2, 2}, {-2, 2}, {-3, 2}, {-11, 2}, {11, 1}, {6, 1}, {5, 1}, {4, 1}, {-4, 1}, {-5, 1}, {-6, 1}, {-11, 1}, {11, 0}, {8, 0}, {7, 0}, {-7, 0}, {-8, 0}, {-10, 0}, {11, -1}, {6, -1}, {5, -1}, {4, -1}, {-4, -1}, {-5, -1}, {-6, -1}, {-11, -1}, {12, -2}, {3, -2}, {2, -2}, {-2, -2}, {-3, -2}, {-12, -2}, {12, -3}, {2, -3}, {-2, -3}, {-12, -3}, {13, -4}, {1, -4}, {-1, -4}, {-13, -4}, {14, -5}, {1, -5}, {-1, -5}, {-14, -5}, {14, -6}, {1, -6}, {-1, -6}, {-14, -6}, {15, -7}, {14, -7}, {13, -7}, {12, -7}, {11, -7}, {10, -7}, {9, -7}, {8, -7}, {7, -7}, {6, -7}, {1, -7}, {-1, -7}, {-6, -7}, {-7, -7}, {-8, -7}, {-9, -7}, {-10, -7}, {-11, -7}, {-12, -7}, {-13, -7}, {-14, -7}, {-15, -7}, {5, -8}, {0, -8}, {-5, -8}, {4, -9}, {0, -9}, {-4, -9}, {3, -10}, {-3, -10}, {3, -11}, {-3, -11}, {2, -12}, {-2, -12}, {1, -13}, {-1, -13}, {1, -14}, {-1, -14}, {0, -15}
    };

    public Test9(double originX, double originY, double originZ, File midiFile, KeyboardLayout layout) throws InvalidMidiDataException, IOException {
        super(originX, originY, originZ, midiFile, layout);
    }

    public Test9(double originX, double originY, double originZ, NoteMap noteMap, KeyboardLayout layout) {
        super(originX, originY, originZ, noteMap, layout);
    }

    @Override
    protected void onInitialize() {
        generator = new ShapeGenerator(originX, originY, originZ);
        teleportGenerator.setHeight(25);
    }

    @Override
    protected void onTrackStart(int trackNum) {

    }

    @Override
    protected void onNote(long tick, Note note) {
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
    protected void onLineUp(long startTick, long endTick, Note startNote, Note endNote) {
        if (endTick - startTick < 200) {
            Line2D line = layout.getLine(startTick, endTick, startNote, endNote);
            Vector2D v = line.toVector();
            double splits = line.getLength() * 16;
            long tick = endTick - startTick;
            add(startTick, String.format("particleex tickparameter endRod %f %f %f 1 0 1 1 240 0 0 0 0 1 x=%.3f*t;z=%.3f*t;y=sin(t*3.14)*5 %f %d",
                    originX + line.start.x,
                    originY,
                    originZ + line.start.y,
                    v.x,
                    v.y,
                    1 / splits,
                    Math.round(splits/tick)
            ));
        }
    }

    @Override
    protected void onTick(long tick) {

    }

    public static void main(String[] args) throws Exception {
        try {
            FunctionWriter writer = new FunctionWriter("test9",
                    "Z:\\JavaMC\\.minecraft\\saves\\Command Music");
            Test9 t = new Test9(27.5, 86, 8.5,
                    new File("Toby Fox - Home.mid"),
                    new KeyboardLayout(2 / 3d, 1));
            t.writeTo(writer);
            writer.close();
        } catch (InvalidMidiDataException | IOException e) {
            e.printStackTrace();
        }
    }
}
