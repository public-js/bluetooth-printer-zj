package blue.muffin.btprinter.zj.dto;

import org.json.JSONObject;

public final class PrintBlockTextDraw extends PrintBlockText {

    private final int lineCount;
    private final int fontSize;
    private final int mode;

    PrintBlockTextDraw(JSONObject block, PrintBlockType type) {
        super(block, type);
        this.lineCount = block.optInt("lineCount");
        this.fontSize = block.optInt("fontSize", 16);
        this.mode = block.optInt("mode", 0);
    }

    public boolean isInvalid() {
        return value == null || value.isEmpty() || lineCount == 0 || fontSize == 0;
    }

    public int getLineCount() {
        return lineCount;
    }

    public int getFontSize() {
        return fontSize;
    }

    public int getMode() {
        return mode;
    }
}
