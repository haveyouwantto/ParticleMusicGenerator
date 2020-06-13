package hywt.mc.mcpack.cmdgen;

import hywt.math.Line2D;
import hywt.math.Vector2D;

import java.util.*;

public class ShapeGenerator extends CoorCommandGenerator {

    private ParticleExpression exp;

    public ShapeGenerator(double originX, double originY, double originZ) {
        super(originX, originY, originZ);
        this.exp = tick -> "particle endRod ~ ~ ~ 0 0 0 0 1 force";
    }

    public List<String> line(Line2D line, int splits) {
        List<String> result = new ArrayList<>();
        Vector2D vector2D = line.toVector();
        Vector2D step = vector2D.divide(splits);
        Vector2D v = (Vector2D) step.clone();
        for (int i = 0; i < splits; i++) {
            result.add(relativePos(v.x + line.start.x, 0, v.y + line.start.y) + exp.generate(i));
            v.add(step);
        }
        return result;
    }

    public Map<Long, Collection<String>> tickLine(Line2D line, int splits, long startTick, long endTick) {
        Map<Long, Collection<String>> result = new TreeMap<>();
        Vector2D vector2D = line.toVector();
        Vector2D step = vector2D.divide(splits);
        Vector2D v = (Vector2D) step.clone();
        for (int i = 0; i < splits; i++) {
            long tick = (long) (i * ((endTick - startTick) * 1d / splits));
            result.putIfAbsent(tick, new ArrayList<>());
            result.get(tick).add(relativePos(v.x + line.start.x, 0, v.y + line.start.y) + exp.generate(i));
            v.add(step);
        }
        return result;
    }

    @Override
    public Map<Long, Collection<String>> generate() {
        throw new UnsupportedOperationException("Method not supported.");
    }

    public ParticleExpression getExp() {
        return exp;
    }

    public void setExp(ParticleExpression exp) {
        this.exp = exp;
    }

}
