package hywt.mc.mcpack;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FunctionWriter implements Closeable {

    private static final int MAX_COMMAND_LENGTH = 1024;
    private final String name;
    private final MCMeta mcMeta;
    private final File rootDir;
    private long tick;
    private int count;
    private PrintWriter writer;
    private PrintWriter mainWriter;
    private File functionRootDir;
    private File partsDir;
    private final List<Long> parts;
    private final List<String> initCmds;

    public FunctionWriter(String name, String rootDirPath) throws IOException {
        this(name, new File(rootDirPath));
    }

    public FunctionWriter(String name, File rootDir) throws IOException {
        this.name = name;
        this.mcMeta = new MCMeta(3, name);
        this.tick = 0;
        this.count = 0;
        this.rootDir = rootDir;
        this.parts = new ArrayList<>();
        this.initCmds = new ArrayList<>();
        initialize();
    }

    private void initialize() throws IOException {
        functionRootDir = new File(rootDir, "data/functions/" + name + "/");
        partsDir = new File(functionRootDir, "parts/");
        mkdirs(partsDir);

        mainWriter = new PrintWriter(new File(functionRootDir, "main.mcfunction"));
        writer = new PrintWriter(new File(partsDir, String.format("part%03d.mcfunction", 0)));

        initCmds.add(String.format("scoreboard objectives add %s dummy", name));
    }

    private void initFunction() throws FileNotFoundException {
        File initFunc = new File(functionRootDir, "init.mcfunction");
        PrintWriter w = new PrintWriter(initFunc);
        for (String initCmd : initCmds) w.println(initCmd);
        w.close();
    }

    private void mkdirs(File file) {
        if (!file.exists())
            file.mkdirs();
    }

    public void write(String command) throws IOException {
        if (count % MAX_COMMAND_LENGTH == 0 && count != 0) {
            parts.add(tick);
            writer.close();
            writer = new PrintWriter(new File(partsDir, String.format("part%03d.mcfunction", count / MAX_COMMAND_LENGTH)));
        }
        writer.println(
                String.format(
                        /*
                        %1: name of the score objective
                        %2: tick of this command
                        %3: command to execute
                         */
                        "execute @a[score_%1$s_min=%2$d,score_%1$s=%2$d] ~ ~ ~ %3$s", name, tick, command
                ));
        count++;
    }

    public void skip(long ticks) {
        this.tick += ticks;
    }

    public void skipTo(long tick) {
        if (tick < this.tick) throw new IllegalArgumentException("tick must be bigger than current tick");
        this.tick = tick;
    }

    public void addInit(String command) {
        initCmds.add(command);
    }

    public void next() {
        ++tick;
    }

    @Override
    public void close() throws IOException {
        writer.close();
        initFunction();
        parts.add(tick);
        for (int i = 0; i < parts.size(); i++) {
            long splitStart = i == 0 ? 0 : parts.get(i - 1);
            long splitEnd = parts.get(i);
            mainWriter.println(
                    /*
                        %1: name of the score objective
                        %2: minimum tick
                        %3: maximum tick
                        %4: function name
                     */
                    String.format("execute @a[score_%1$s_min=%2$d,score_%1$s=%3$d] ~ ~ ~ function %1$s:parts/part%4$03d", name, splitStart, splitEnd, i)
            );
        }
        mainWriter.println(String.format("scoreboard players add @a[score_%1$s_min=%2$d,score_%1$s=%3$d] %1$s 1", name, 0, parts.get(parts.size() - 1)));
        mainWriter.close();
    }

    public MCMeta getMcMeta() {
        return mcMeta;
    }

    public File getRootDir() {
        return rootDir;
    }

    public long getTick() {
        return tick;
    }
}
