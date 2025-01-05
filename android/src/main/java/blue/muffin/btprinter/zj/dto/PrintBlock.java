package blue.muffin.btprinter.zj.dto;

import org.json.JSONObject;

public abstract class PrintBlock {

    protected final PrintBlockType type;

    PrintBlock(PrintBlockType type) {
        this.type = type;
    }

    public PrintBlockType getType() {
        return type;
    }

    public static PrintBlockType getBlockType(JSONObject block) {
        String type = block.optString("type", PrintBlockType.lf.toString());
        return PrintBlockType.valueOf(type);
    }
}
