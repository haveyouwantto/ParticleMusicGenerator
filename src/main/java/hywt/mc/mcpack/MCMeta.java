package hywt.mc.mcpack;

public class MCMeta {
    public int format;
    public String description;

    public MCMeta(int format, String description) {
        this.format = format;
        this.description = description;
    }

    @Override
    public String toString() {
        return "{\"pack\":{\"pack_format\":" + format + ",\"description\":\"" + description + "\"}}";
    }
}
