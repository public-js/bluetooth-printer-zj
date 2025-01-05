package blue.muffin.btprinter.zj.dto;

import org.json.JSONObject;

public final class PrintBlockTextQr extends PrintBlockText {

    private final int codeWidth;
    private final int mode;

    PrintBlockTextQr(JSONObject block, PrintBlockType type) {
        super(block, type);
        this.codeWidth = block.optInt("codeWidth");
        this.mode = block.optInt("mode", 0);
    }

    public boolean isInvalid() {
        return value == null || value.isEmpty() || codeWidth == 0;
    }

    public int getCodeWidth() {
        return codeWidth;
    }

    public int getMode() {
        return mode;
    }
}
