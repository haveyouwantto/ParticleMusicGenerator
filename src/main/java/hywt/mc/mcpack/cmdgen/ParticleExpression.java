package hywt.mc.mcpack.cmdgen;

@FunctionalInterface
public interface ParticleExpression {
    String generate(long tick);
}