package hywt.mc.particle;

import hywt.math.tensor.Vector3D;

public class ParticleInfo {
    private final Vector3D pos;
    private final long tick;
    private final int number;
    private final int splits;

    public ParticleInfo(Vector3D pos, long tick, int number, int splits) {
        this.pos = pos;
        this.tick = tick;
        this.number = number;
        this.splits = splits;
    }

    public Vector3D getPos() {
        return pos.clone();
    }

    public long getTick() {
        return tick;
    }

    public int getNumber() {
        return number;
    }

    public double getProgress() {
        return number * 1d / splits;
    }
}
