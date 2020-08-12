package hywt.mc.mcpack.cmdgen;

import hywt.mc.mcpack.FunctionWriter;

import java.io.File;
import java.io.IOException;

// TODO add bedrock edition
public class FunctionWriterBE  extends FunctionWriter {
    public FunctionWriterBE(String name, String rootDirPath) throws IOException {
        super(name, rootDirPath);
    }

    public FunctionWriterBE(String name, File rootDir) throws IOException {
        super(name, rootDir);
    }
}
