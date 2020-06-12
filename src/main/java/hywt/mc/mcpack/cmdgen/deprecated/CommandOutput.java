package hywt.mc.mcpack.cmdgen.deprecated;

import hywt.mc.mcpack.FunctionWriter;
import hywt.mc.mcpack.cmdgen.CommandGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

@Deprecated
public class CommandOutput {
    private TreeMap<Long, Collection<String>> cmds;

    public CommandOutput() {
        this.cmds = new TreeMap<>();
    }

    public void add(long tick, String command) {
        this.cmds.putIfAbsent(tick, new ArrayList<>());
        this.cmds.get(tick).add(command);
    }

    public void addAll(long tick, Collection<String> cmds) {
        this.cmds.putIfAbsent(tick, new ArrayList<>());
        this.cmds.get(tick).addAll(cmds);
    }

    public void addAll(long startTick, Map<Long, Collection<String>> cmds) {
        for (Map.Entry<Long, Collection<String>> entry : cmds.entrySet())
            addAll(entry.getKey() + startTick, entry.getValue());
    }

    public void writeTo(FunctionWriter writer) throws IOException {
        for (Map.Entry<Long, Collection<String>> entry : cmds.entrySet()) {
            if (writer.getTick() < entry.getKey()) writer.skipTo(entry.getKey());
            for (String string : entry.getValue()) writer.write(string);
        }
    }

    public void addAll(long startTick, CommandGenerator generator) {
            addAll(startTick, generator.generate());
    }

    public void setCmds(TreeMap<Long, Collection<String>> cmds) {
        this.cmds = cmds;
    }

}
