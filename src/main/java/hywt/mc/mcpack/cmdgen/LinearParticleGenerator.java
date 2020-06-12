package hywt.mc.mcpack.cmdgen;

import hywt.math.Line2D;
import hywt.math.Vector2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LinearParticleGenerator extends CoorCommandGenerator {
    private Map<Long, Collection<String>> out;

    public LinearParticleGenerator(double originX, double originY, double originZ) {
        super(originX, originY, originZ);
        out = new HashMap<>();
    }

    public void add(long tick ,Line2D line, int splits) {
        out.putIfAbsent(tick, new ArrayList<>());
        Vector2D vector2D = line.toVector();
        Vector2D step = vector2D.divide(splits);
        Vector2D v = (Vector2D) step.clone();
        for (int i = 0; i < splits; i++) {
            out.get(0L).add(relativePos(v.x, 0, v.y));
            System.out.println(v);
            v.add(step);
        }
    }

    @Override
    protected String relativePos(double x, double y, double z) {
        return String.format("particle fireworksSpark %1$f %2$f %3$f 0 0 0 0 1", originX + x, originY + y, originZ + z);
    }

    @Override
    public Map<Long, Collection<String>> generate() {
        return null;
    }

}
