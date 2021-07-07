package hywt.mc.redstone.music;

import hywt.math.Line2D;
import hywt.math.Mapper;
import hywt.math.tensor.Vector2D;
import hywt.math.tensor.Vector3D;
import hywt.mc.mcpack.FunctionWriter;
import hywt.mc.mcpack.cmdgen.CommandFormatter;
import hywt.mc.mcpack.cmdgen.ShapeGenerator;
import hywt.mc.mcpack.cmdgen.deprecated.CommandOutput;
import hywt.mc.mcpack.cmdgen.deprecated.LegacyParticleGenerator;

import java.io.IOException;

public class Test8 {
    public static void main(String[] args) throws IOException {
        FunctionWriter writer = new FunctionWriter("test8",
                "F:\\\u5b58\u6863\\Minecraft\\Java\u7248\\.minecraft\\saves\\Command Music");
        CommandOutput cmdOutput = new CommandOutput();
        LegacyParticleGenerator particleGenerator = new LegacyParticleGenerator(27.5, 80, 8.5);
        CommandFormatter cmdFormatter = new CommandFormatter(27.5, 80, 8.5);
        long t = System.nanoTime();
        Mapper mapper = new Mapper(0, 1, 0, Math.PI);
        for (int i = 0; i < 64000; i++) {
            long tick = i / 20;
            Vector3D v = Vector3D.fromPolar(i / 500d,
                    Math.cos(i / 47d) +
                            Math.cos(i / 71d) +
                            Math.cos(i / 113d) +
                            Math.cos(i / 151d) +
                            Math.cos(i / 193d) +
                            Math.cos(i / 227d),
                    Math.sin(i / 47d) +
                            Math.sin(i / 71d) +
                            Math.sin(i / 113d) +
                            Math.sin(i / 151d) +
                            Math.sin(i / 193d) +
                            Math.sin(i / 227d)
            );
            cmdOutput.add(tick, particleGenerator.generate(v.x, v.y, v.z));
            if (i > 5000) {
                for (int j = 0; j < i / 5000; j++) {
                    Vector3D v2 = Vector3D.fromPolar((i - 5000) / 1000d, mapper.map(Math.random()), i);
                    cmdOutput.add(tick, particleGenerator.generate(v2.x, v2.y, v2.z, "dripLava"));
                }
                if (i % 20 == 0) {
                    for (int j = 0; j < i / 5000; j++) {
                        Vector3D v3 = Vector3D.fromPolar(i / 500d, mapper.map(Math.random()), i);
                        Vector3D neg = v3.clone().multiply(-0.09);
                        cmdOutput.add(tick, cmdFormatter.generate(v3.x, v3.y, v3.z,
                                String.format("particleex fireworksSpark ~ ~ ~ normal %s 1 255 %f %f %f 0 0 0 1",
                                        Colors.LINE_COLORS[0].toCommandColor(),
                                        neg.x, neg.y, neg.z)));
                    }
                }
            }
        }
        cmdOutput.writeTo(writer);
        System.out.println((System.nanoTime() - t) / 1e6);  // 397.0237
        writer.close();
    }
}
