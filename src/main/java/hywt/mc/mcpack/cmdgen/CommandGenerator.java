package hywt.mc.mcpack.cmdgen;

import java.util.Collection;
import java.util.Map;

@FunctionalInterface
public interface CommandGenerator {
    Map<Long, Collection<String>> generate();
}
