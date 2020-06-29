package hywt.mc.mcpack.cmdgen;

import hywt.math.Line2D;
import hywt.math.Vector2D;

import java.util.*;

public class ShapeGenerator extends CoorCommandGenerator {

    private static final ParticleExpression DEFAULT_EXPRESSION = (totalTicks, tick) -> "particle endRod ~ ~ ~ 0 0 0 0 1 force";

    public ShapeGenerator(double originX, double originY, double originZ) {
        super(originX, originY, originZ);
    }

    public List<String> line(Line2D line, int splits) {
        return line(line, splits, DEFAULT_EXPRESSION);
    }

    public List<String> line(Line2D line, int splits, ParticleExpression exp) {
        List<String> result = new ArrayList<>();
        Vector2D vector2D = line.toVector();
        Vector2D step = vector2D.divide(splits);
        Vector2D v = (Vector2D) step.clone();
        for (int i = 0; i < splits; i++) {
            result.add(relativePos(v.x + line.start.x, 0, v.y + line.start.y) + exp.generate(1, i));
            v.add(step);
        }
        return result;
    }

    public Map<Long, Collection<String>> tickLine(Line2D line, int splits, long startTick, long endTick) {
        return tickLine(line, splits, startTick, endTick, DEFAULT_EXPRESSION);
    }

    public Map<Long, Collection<String>> tickLine(Line2D line, int splits, long startTick, long endTick, ParticleExpression exp) {
        Map<Long, Collection<String>> result = new TreeMap<>();
        Vector2D vector2D = line.toVector();
        Vector2D step = vector2D.divide(splits);
        Vector2D v = (Vector2D) step.clone();
        for (int i = 0; i < splits; i++) {
            long tick = (long) (i * ((endTick - startTick) * 1d / splits));
            result.putIfAbsent(tick, new ArrayList<>());
            result.get(tick).add(relativePos(v.x + line.start.x, 0, v.y + line.start.y) + exp.generate(splits, i));
            v.add(step);
        }
        return result;
    }

    @Override
    public Map<Long, Collection<String>> generate() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Method not supported.");
    }

}
