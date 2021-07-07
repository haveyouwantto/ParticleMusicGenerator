package hywt.mc.redstone.music;

import hywt.mc.mcpack.FunctionWriter;
import hywt.mc.mcpack.cmdgen.PianoRollMusicGenerator;
import hywt.midi.KeyboardLayout;
import hywt.midi.Note;

import javax.sound.midi.InvalidMidiDataException;
import java.io.File;
import java.io.IOException;

public class Tutorial extends PianoRollMusicGenerator {

    public static void main(String[] args) {
        try {
            FunctionWriter writer = new FunctionWriter("tutorial", ".");
            Tutorial tutorial = new Tutorial(27.5, 66, 8.5, new File("Toby Fox - Home.mid"), new KeyboardLayout(2 / 3d, 1));
            tutorial.writeTo(writer);
            writer.close();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Tutorial(double originX, double originY, double originZ, File midiFile, KeyboardLayout layout) throws InvalidMidiDataException, IOException {
        super(originX, originY, originZ, midiFile, layout);
    }

    @Override
    protected void onInitialize() {

    }

    @Override
    protected void onTrackStart(int trackNum) {

    }

    @Override
    protected void onNote(long tick, Note note) {
        add(tick, String.format("say %s", note));
    }

    @Override
    protected void onLineUp(long startTick, long endTick, Note startNote, Note endNote) {

    }

    @Override
    protected void onTick(long tick) {

    }
}
