package hywt.mc.mcpack.cmdgen.deprecated;

import hywt.mc.mcpack.cmdgen.CommandGenerator;

import java.util.*;

@Deprecated
public class TestCommandGenerator implements CommandGenerator {
    private final int count;

    public TestCommandGenerator(int count) {
        this.count = count;
    }

    @Override
    public Map<Long, Collection<String>> generate() {
        Map<Long, Collection<String>> map = new HashMap<>();
        for (int i = 0; i < count; i++) {
            List<String> strings = new ArrayList<>();
            strings.add("say " + i);
            map.put((long) i, strings);
        }
        return map;
    }


}
