package hywt.mc.mcpack.cmdgen;

public abstract class CoorCommandGenerator implements CommandGenerator {
    protected double originX;
    protected double originY;
    protected double originZ;

    public CoorCommandGenerator(double originX, double originY, double originZ) {
        this.originX = originX;
        this.originY = originY;
        this.originZ = originZ;
    }

    protected String relativePos(double x, double y, double z) {
        return String.format("execute @s %f %f %f ", originX + x, originY + y, originZ + z);
    }
}
