package hywt.mc.mcpack;

import java.io.Closeable;

public interface CommandWriter extends Closeable {
    void write(String command) throws Exception;

    void skip(long ticks);

    void skipTo(long tick);

    void next();

    long getTick();
}
