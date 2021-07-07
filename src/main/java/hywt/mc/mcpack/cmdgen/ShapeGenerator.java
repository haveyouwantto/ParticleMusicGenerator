package hywt.mc.mcpack.cmdgen;

import hywt.math.Line2D;
import hywt.math.tensor.Vector2D;
import hywt.mc.particle.LineInfo;
import hywt.mc.particle.ParticleExpression;
import hywt.mc.particle.ParticleInfo;

import java.util.*;

public class ShapeGenerator extends CoorCommandGenerator {

    private static final ParticleExpression DEFAULT_EXPRESSION = (totalTicks, info) -> "particle endRod ~ ~ ~ 0 0 0 0 1 force";

    public ShapeGenerator(double originX, double originY, double originZ) {
        super(originX, originY, originZ);
    }

    public List<String> line(Line2D line, int splits) {
        return line(line, splits, DEFAULT_EXPRESSION);
    }

    public List<String> line(Line2D line, int splits, ParticleExpression exp) {
        List<String> result = new ArrayList<>();
        LineInfo lineInfo = new LineInfo(splits,line);
        Vector2D vector2D = line.toVector();
        Vector2D step = vector2D.multiply(1d / splits);
        Vector2D v = step.clone();
        for (int i = 0; i < splits; i++) {
            ParticleInfo info = new ParticleInfo(v.to3D(), 0, i, splits);
            result.add(relativePos(v.x + line.start.x, 0, v.y + line.start.y) + exp.generate(lineInfo, info));
            v.add(step);
        }
        return result;
    }

    public Map<Long, Collection<String>> tickLine(Line2D line, int splits, long startTick, long endTick) {
        return tickLine(line, splits, startTick, endTick, DEFAULT_EXPRESSION);
    }

    public Map<Long, Collection<String>> tickLine(Line2D line, int splits, long startTick, long endTick, ParticleExpression exp) {
        Map<Long, Collection<String>> result = new TreeMap<>();
        LineInfo lineInfo = new LineInfo(splits,line);
        Vector2D vector2D = line.toVector();
        Vector2D step = vector2D.multiply(1f / splits);
        Vector2D v = step.clone();
        for (int i = 0; i < splits; i++) {
            long tick = (long) (i * ((endTick - startTick) * 1d / splits));
            ParticleInfo info = new ParticleInfo(v.to3D(), tick, i, splits);
            result.putIfAbsent(tick, new ArrayList<>());
            result.get(tick).add(relativePos(v.x + line.start.x, 0, v.y + line.start.y) + exp.generate(lineInfo, info));
            v.add(step);
        }
        return result;
    }

    @Override
    public Map<Long, Collection<String>> generate() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Method not supported.");
    }

}
