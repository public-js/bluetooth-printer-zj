package blue.muffin.btprinter.zj.dto;

import org.json.JSONObject;

public final class PrintBlockTextRaw extends PrintBlockText {

    private final String encoding;
    private final int codepage;
    private final int scaleWidth;
    private final int scaleHeight;
    private final int fontStyle;
    private final int align;

    public PrintBlockTextRaw(JSONObject block, PrintBlockType type) {
        super(block, type);
        this.encoding = block.optString("encoding", "GBK");
        this.codepage = block.optInt("codepage", 0);
        this.scaleWidth = block.optInt("scaleWidth", 0);
        this.scaleHeight = block.optInt("scaleHeight", 0);
        this.fontStyle = block.optInt("fontStyle", 0);
        this.align = block.optInt("align", 0);
    }

    public boolean isInvalid() {
        return value == null || value.isEmpty();
    }

    public String getEncoding() {
        return encoding;
    }

    public int getCodepage() {
        return codepage;
    }

    public int getScaleWidth() {
        return scaleWidth;
    }

    public int getScaleHeight() {
        return scaleHeight;
    }

    public int getFontStyle() {
        return fontStyle;
    }

    public int getAlign() {
        return align;
    }
}
