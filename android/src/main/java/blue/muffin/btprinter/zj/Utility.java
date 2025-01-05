package blue.muffin.btprinter.zj;

class Utility {

    // [POS_Set_PrtAndFeedPaper]
    public static byte[] feedPaper(int feedI) {
        byte[] feed = Command.Feed.clone();
        feed[2] = (byte) feedI;
        return feed;
    }

    // [byteArraysToBytes]
    public static byte[] flatByteArray(byte[][] data) {
        int length = 0;
        for (byte[] datum : data) {
            length += datum.length;
        }
        byte[] send = new byte[length];
        int k = 0;
        for (byte[] datum : data) {
            for (byte b : datum) {
                send[k++] = b;
            }
        }
        return send;
    }
}
