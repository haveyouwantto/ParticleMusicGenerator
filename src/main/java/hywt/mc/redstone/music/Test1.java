package hywt.mc.redstone.music;

import hywt.math.Vector2D;
import hywt.mc.mcpack.FunctionWriter;
import hywt.mc.mcpack.cmdgen.deprecated.LegacyParticleGenerator;

import java.io.IOException;

@Deprecated
public class Test1 {
    public static void main(String[] args) {
        try {
            FunctionWriter functionWriter = new FunctionWriter("test1", ".");
            System.out.println(functionWriter.getMcMeta());
            LegacyParticleGenerator particleGenerator = new LegacyParticleGenerator(27.5, 66, 8.5);
            for (int i = 0; i < 10000; i++) {
                Vector2D v = Vector2D.fromPolar(i / 100d, Math.toRadians(i));
                functionWriter.write(particleGenerator.generate(v.x, 0, v.y));
                functionWriter.write(particleGenerator.generate(-v.x, 0, v.y));
                functionWriter.write(particleGenerator.generate(v.x, 0, -v.y));
                functionWriter.write(particleGenerator.generate(-v.x, 0, -v.y));
                Vector2D v2 = Vector2D.fromPolar(Math.sqrt(i) / 5, Math.toRadians(Math.pow(i, 1.5)));
                functionWriter.write(particleGenerator.generate(v2.x, 0, v2.y, "totem"));
                functionWriter.write(particleGenerator.generate(-v2.x, 0, -v2.y, "totem"));
                functionWriter.write(particleGenerator.generate(v.x, i / 500d, v.y, "lava"));
                functionWriter.write(particleGenerator.generate(v2.x, i / 500d, v2.y, "endRod"));
                if (i % 10 == 0) functionWriter.next();
            }
            functionWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
