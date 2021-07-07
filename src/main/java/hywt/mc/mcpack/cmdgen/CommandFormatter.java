package hywt.mc.mcpack.cmdgen;

public class CommandFormatter {
    private double originX;
    private double originY;
    private double originZ;

    public CommandFormatter(double originX, double originY, double originZ) {
        this.originX = originX;
        this.originY = originY;
        this.originZ = originZ;
    }

    public String generate(double x, double y, double z, String cmd) {
        return getPrefix(x, y, z) + cmd;
    }

    protected String getPrefix(double x, double y, double z) {
        return String.format("execute @s %1$f %2$f %3$f ", originX + x, originY + y, originZ + z);
    }
}
