package hywt.mc.particle;

@FunctionalInterface
public interface ParticleExpression {
    String generate(LineInfo lineInfo, ParticleInfo info);
}