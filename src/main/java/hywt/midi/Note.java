package hywt.midi;

public class Note implements MIDIEvent, Comparable<Note> {
    private final int track;
    private final int channel;
    private final int pitch;
    private final int volume;

    public Note(int track, int channel, int pitch, int volume) {
        this.track = track;
        this.channel = channel;
        this.pitch = pitch;
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "Note{" +
            "track=" + track +
            ", channel=" + channel +
            ", pitch=" + pitch +
            ", volume=" + volume +
            '}';
    }

    public int getTrack() {
        return track;
    }

    public int getChannel() {
        return channel;
    }

    public int getPitch() {
        return pitch;
    }

    public int getVolume() {
        return volume;
    }

    /**
     * Compare notes by pitch
     *
     * @param o another note
     * @return comparison result
     */
    @Override
    public int compareTo(Note o) {
        return Integer.compare(this.pitch, o.pitch);
    }
}
