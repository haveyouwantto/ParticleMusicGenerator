package hywt.midi;

public class TempoChange implements MIDIEvent {
    private int tempo;

    public TempoChange(int tempo) {
        this.tempo = tempo;
    }

    public int getTempo() {
        return tempo;
    }

    @Override
    public String toString() {
        return "TempoChange{" +
                "tempo=" + tempo +
                '}';
    }
}
