package blue.muffin.btprinter.zj.dto;

import com.getcapacitor.JSArray;
import com.getcapacitor.PluginCall;
import org.json.JSONObject;

public final class PrintRequest {

    private final JSArray blocks;
    private final Integer pageWidth;
    private final Integer feed;

    public PrintRequest(PluginCall call) {
        this.blocks = call.getArray("blocks");
        this.pageWidth = call.getInt("pageWidth", 0);
        this.feed = call.getInt("feed", 48);
    }

    public boolean isInvalid() {
        return blocks == null || blocks.length() == 0 || pageWidth == 0;
    }

    public JSArray getBlocks() {
        return blocks;
    }

    public int getPageWidth() {
        return pageWidth.intValue();
    }

    public int getFeed() {
        return feed.intValue();
    }

    public PrintBlock getBlockAt(int index) {
        try {
            JSONObject block = (JSONObject) blocks.get(index);
            PrintBlockType type = PrintBlock.getBlockType(block);
            return switch (type) {
                case textRaw -> new PrintBlockTextRaw(block, type);
                case textDraw -> new PrintBlockTextDraw(block, type);
                case textQr -> new PrintBlockTextQr(block, type);
                default -> new PrintBlockLf(block, type);
            };
        } catch (Exception ignored) {}
        return null;
    }
}
