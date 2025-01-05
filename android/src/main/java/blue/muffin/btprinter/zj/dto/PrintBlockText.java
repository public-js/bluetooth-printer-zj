package blue.muffin.btprinter.zj.dto;

import org.json.JSONObject;

abstract class PrintBlockText extends PrintBlock {

    protected final String value;

    PrintBlockText(JSONObject block, PrintBlockType type) {
        super(type);
        this.value = block.optString("value");
    }

    public String getValue() {
        return value;
    }
}
