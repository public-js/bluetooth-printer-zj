package blue.muffin.btprinter.zj;

class Command {

    private static final byte ESC = 0x1B;
    private static final byte FS = 0x1C;
    private static final byte GS = 0x1D;
    private static final byte NL = 0x0A;

    // Printer Initialization [ESC_Init]
    public static byte[] Init = new byte[] { ESC, '@' };

    public static byte[] LF = new byte[] { NL };

    // Print and feed paper (last byte, supports: 0-255) [ESC_J]
    public static byte[] Feed = new byte[] { ESC, 'J', 0x00 };

    // Set Font Double Height and Double Width (last byte, supports: 0-3 each) [GS_ExclamationMark]
    public static byte[] FontWH = new byte[] { GS, '!', 0x00 };
    public static byte[] FontWidthOpts = new byte[] { 0x00, 0x10, 0x20, 0x30 };
    public static byte[] FontHeightOpts = new byte[] { 0x00, 0x01, 0x02, 0x03 };

    // Select Font Style (mainly for ASCII characters; last byte, supports: 0, 1, 48, 49) [ESC_M]
    public static byte[] FontStyle = new byte[] { ESC, 'M', 0x00 };

    // Character Mode [FS_dot]
    public static byte[] CharMode = new byte[] { FS, 46 };

    // Chinese Character Mode [FS_and]
    public static byte[] CharModeCn = new byte[] { FS, '&' };

    // Select Character Code Page (last byte, supports: 0-255) [ESC_t]
    public static byte[] CodePage = new byte[] { ESC, 't', 0x00 };

    // Set Alignment Mode (last byte, supports: 0=Left, 1=Center, 2=Right) [ESC_Align]
    public static byte[] Align = new byte[] { ESC, 'a', 0x00 };
}
