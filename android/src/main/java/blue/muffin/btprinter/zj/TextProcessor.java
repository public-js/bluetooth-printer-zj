package blue.muffin.btprinter.zj;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import java.io.UnsupportedEncodingException;

class TextProcessor {

    public static Bitmap drawText(String txt, float size, double width, double height) {
        Bitmap canvasBitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
        int bitmapWidth = canvasBitmap.getWidth();
        Canvas canvas = new Canvas(canvasBitmap);
        canvas.setBitmap(canvasBitmap);
        canvas.drawColor(-1);
        TextPaint paint = new TextPaint();
        paint.setColor(-16777216);
        paint.setTextSize(size);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setFakeBoldText(false);
        StaticLayout layout = new StaticLayout(
            txt,
            0,
            txt.length(),
            paint,
            bitmapWidth,
            Layout.Alignment.ALIGN_NORMAL,
            1.1F,
            0.0F,
            true,
            TextUtils.TruncateAt.END,
            bitmapWidth
        );
        canvas.translate(0.0F, 5.0F);
        layout.draw(canvas);
        canvas.save();
        canvas.restore();
        return canvasBitmap;
    }

    // [POS_Print_Text]
    public static byte[] textAsPrintable(
        String string,
        String encoding,
        int codepage,
        int scaleWidth,
        int scaleHeight,
        int fontStyleI,
        int align
    ) {
        byte[] stringAsBytes;
        try {
            stringAsBytes = string.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            return null;
        }

        byte[] fontWH = Command.FontWH.clone();
        fontWH[2] = scaleWidth > 0 || scaleHeight > 0
            ? (byte) (Command.FontWidthOpts[scaleWidth] + Command.FontHeightOpts[scaleHeight])
            : fontWH[2];

        byte[] codePage = Command.CodePage.clone();
        codePage[2] = (byte) codepage;

        byte[] charMode = codepage == 0 ? Command.CharModeCn : Command.CharMode;

        byte[] fontStyle = Command.FontStyle.clone();
        fontStyle[2] = (byte) fontStyleI;

        byte[] alignment = Command.Align.clone();
        alignment[2] = align > 0 ? (byte) align : alignment[2];

        return Utility.flatByteArray(new byte[][] { fontWH, codePage, charMode, fontStyle, alignment, stringAsBytes, Command.Align });
    }
}
