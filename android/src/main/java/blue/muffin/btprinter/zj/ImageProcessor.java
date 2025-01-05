package blue.muffin.btprinter.zj;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

class ImageProcessor {

    public static Bitmap textToQR(String source, int width, int height) {
        BitMatrix result;

        try {
            result = new MultiFormatWriter().encode(source, BarcodeFormat.QR_CODE, width, height, null);
        } catch (IllegalArgumentException | WriterException e) {
            // Unsupported format
            return null;
        }

        final int w = result.getWidth();
        final int h = result.getHeight();
        final int[] pixels = new int[w * h];

        for (int y = 0; y < h; y++) {
            final int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }

        final Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, w, h);

        return bitmap;
    }

    public static byte[] imageAsPrintable(Bitmap bitmap, int mode, int pageWidth) {
        int width = ((pageWidth + 7) / 8) * 8;
        int height = (bitmap.getHeight() * width) / bitmap.getWidth();
        height = ((height + 7) / 8) * 8;

        Bitmap rszBitmap = bitmap;
        if (bitmap.getWidth() != width) {
            rszBitmap = resizeBitmap(bitmap, width, height);
        }

        Bitmap grayBitmap = toGrayscale(rszBitmap);
        byte[] dithered = ditherBitmap(grayBitmap);
        return imageBytesToCommands(dithered, width, mode);
    }

    private static Bitmap resizeBitmap(Bitmap bitmap, int width, int height) {
        int bmpWidth = bitmap.getWidth();
        int bmpHeight = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale((float) width / (float) bmpWidth, (float) height / (float) bmpHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, true);
    }

    private static Bitmap toGrayscale(Bitmap bitmapSrc) {
        int height = bitmapSrc.getHeight();
        int width = bitmapSrc.getWidth();
        Bitmap grayed = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(grayed);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.0F);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        canvas.drawBitmap(bitmapSrc, 0.0F, 0.0F, paint);
        return grayed;
    }

    // [thresholdToBWPic]
    private static byte[] ditherBitmap(Bitmap bitmap) {
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        byte[] data = new byte[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        formatKThreshold(pixels, bitmap.getWidth(), bitmap.getHeight(), data);
        return data;
    }

    // [format_K_threshold]
    private static void formatKThreshold(int[] pixelsSrc, int width, int height, byte[] pixelsDst) {
        int grayTotal = 0;
        int k = 0;

        int i;
        int j;
        int gray;
        for (i = 0; i < height; ++i) {
            for (j = 0; j < width; ++j) {
                gray = pixelsSrc[k] & 255;
                grayTotal += gray;
                ++k;
            }
        }

        int grayAvg = grayTotal / height / width;
        k = 0;

        for (i = 0; i < height; ++i) {
            for (j = 0; j < width; ++j) {
                gray = pixelsSrc[k] & 255;
                pixelsDst[k] = (byte) (gray > grayAvg ? 0 : 1);
                ++k;
            }
        }
    }

    private static final int[] p0 = new int[] { 0, 128 };
    private static final int[] p1 = new int[] { 0, 64 };
    private static final int[] p2 = new int[] { 0, 32 };
    private static final int[] p3 = new int[] { 0, 16 };
    private static final int[] p4 = new int[] { 0, 8 };
    private static final int[] p5 = new int[] { 0, 4 };
    private static final int[] p6 = new int[] { 0, 2 };

    // [eachLinePixToCmd]
    private static byte[] imageBytesToCommands(byte[] src, int nWidth, int nMode) {
        int nHeight = src.length / nWidth;
        int nBytesPerLine = nWidth / 8;
        byte[] data = new byte[nHeight * (8 + nBytesPerLine)];
        int k = 0;

        for (int i = 0; i < nHeight; ++i) {
            int offset = i * (8 + nBytesPerLine);
            data[offset + 0] = 29;
            data[offset + 1] = 118;
            data[offset + 2] = 48;
            data[offset + 3] = (byte) (nMode & 1);
            data[offset + 4] = (byte) (nBytesPerLine % 256);
            data[offset + 5] = (byte) (nBytesPerLine / 256);
            data[offset + 6] = 1;
            data[offset + 7] = 0;

            for (int j = 0; j < nBytesPerLine; ++j) {
                data[offset + 8 + j] = (byte) (p0[src[k + 0]] +
                    p1[src[k + 1]] +
                    p2[src[k + 2]] +
                    p3[src[k + 3]] +
                    p4[src[k + 4]] +
                    p5[src[k + 5]] +
                    p6[src[k + 6]] +
                    src[k + 7]);
                k += 8;
            }
        }

        return data;
    }
}
