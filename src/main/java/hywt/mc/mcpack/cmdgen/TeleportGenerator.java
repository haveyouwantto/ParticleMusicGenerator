package hywt.mc.mcpack.cmdgen;

import hywt.midi.KeyboardLayout;

import java.util.*;

public class TeleportGenerator extends CoorCommandGenerator {
    private KeyboardLayout layout;
    private double distance;
    private double height;
    private long length;

    public TeleportGenerator(double originX, double originY, double originZ, KeyboardLayout layout, long length) {
        super(originX, originY, originZ);
        this.layout = layout;
        this.distance = 40 / layout.getKeyRatio();
        this.length = length;
        this.height = 0;
    }

    public KeyboardLayout getLayout() {
        return layout;
    }

    public TeleportGenerator setLayout(KeyboardLayout layout) {
        this.layout = layout;
        return this;
    }

    public double getDistance() {
        return distance;
    }

    public TeleportGenerator setDistance(double distance) {
        this.distance = distance;
        return this;
    }

    public long getLength() {
        return length;
    }

    public TeleportGenerator setLength(long length) {
        this.length = length;
        return this;
    }

    public double getHeight() {
        return height;
    }

    public TeleportGenerator setHeight(double height) {
        this.height = height;
        return this;
    }

    @Override
    public Map<Long, Collection<String>> generate() {
        Map<Long, Collection<String>> map = new HashMap<>();
        for (int i = 0; i < length; i++) {
            List<String> strings = new ArrayList<>();
            strings.add(relativePos(layout.getX(i) + distance, height, layout.getZ(64)));
            map.put((long) i, strings);
        }
        return map;
    }

    @Override
    protected String relativePos(double x, double y, double z) {
        return String.format("tp @s %f %f %f", originX + x, originY + y, originZ + z);
    }
}
