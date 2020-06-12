package hywt.mc.mcpack.cmdgen.deprecated;

@Deprecated
public class LegacyParticleGenerator {
    private double originX;
    private double originY;
    private double originZ;

    public LegacyParticleGenerator(double originX, double originY, double originZ) {
        this.originX = originX;
        this.originY = originY;
        this.originZ = originZ;
    }

    public String generate(double x, double y, double z) {
        return generate(x, y, z, "fireworksSpark");
    }

    public String generate(double x, double y, double z, String particleName) {
        return getPrefix(x, y, z) + String.format("particle %1$s ~ ~ ~ 0.0001 0.0001 0.0001 0 1 force", particleName);
    }

    public String generateBig(double x, double y, double z, String particleName) {
        return getPrefix(x, y, z) + String.format("particle %1$s ~ ~ ~ 0.1 5 0.1 0.1 50 force", particleName);
    }

    protected String getPrefix(double x, double y, double z) {
        return String.format("execute @s %1$f %2$f %3$f ", originX + x, originY + y, originZ + z);
    }
}
