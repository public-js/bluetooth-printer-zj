package blue.muffin.btprinter.zj;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import blue.muffin.btprinter.zj.dto.ConnectRequest;
import blue.muffin.btprinter.zj.dto.ConnectResponse;
import blue.muffin.btprinter.zj.dto.DiscoverResponse;
import blue.muffin.btprinter.zj.dto.PrintBlock;
import blue.muffin.btprinter.zj.dto.PrintBlockTextDraw;
import blue.muffin.btprinter.zj.dto.PrintBlockTextQr;
import blue.muffin.btprinter.zj.dto.PrintBlockTextRaw;
import blue.muffin.btprinter.zj.dto.PrintRequest;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

@CapacitorPlugin(
    name = "BluetoothPrinterZj",
    permissions = {
        @Permission(alias = BluetoothPrinterZjPlugin.BLUETOOTH_CONNECT, strings = { Manifest.permission.BLUETOOTH_CONNECT }),
        @Permission(alias = BluetoothPrinterZjPlugin.BLUETOOTH_ADMIN, strings = { Manifest.permission.BLUETOOTH_ADMIN })
    }
)
public class BluetoothPrinterZjPlugin extends Plugin {

    static final String BLUETOOTH_CONNECT = "connect";
    static final String BLUETOOTH_ADMIN = "admin";

    private BluetoothAdapter bluetoothAdapter;
    private ConnectionService connectionService;
    private String connectionCallbackId;
    private String writerCallbackId;

    @Override
    public void load() {
        this.bluetoothAdapter = ((BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
    }

    @PluginMethod
    public void discover(PluginCall call) {
        if (canDoBluetoothOrConnect()) {
            discoverDo(call);
            return;
        }
        requestPermissionForAlias(BLUETOOTH_CONNECT, call, "discoverDo");
    }

    @PluginMethod
    public void connect(PluginCall call) {
        if (canDoBluetoothAdmin()) {
            connectDo(call);
            return;
        }
        requestPermissionForAlias(BLUETOOTH_ADMIN, call, "connectDo");
    }

    @PluginMethod
    public void disconnect(PluginCall call) {
        disconnectDo();
        call.resolve();
    }

    @PluginMethod
    public void print(PluginCall call) {
        if (canDoBluetoothOrConnect()) {
            printDo(call);
            return;
        }
        requestPermissionForAlias(BLUETOOTH_CONNECT, call, "printDo");
    }

    private boolean canDoBluetoothOrConnect() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.R
            ? getPermissionState(BLUETOOTH_CONNECT) == PermissionState.GRANTED
            : isPermissionDeclared(BLUETOOTH_CONNECT);
    }

    private boolean canDoBluetoothAdmin() {
        return getPermissionState(BLUETOOTH_ADMIN) == PermissionState.GRANTED;
    }

    @Override
    protected void handleOnStart() {
        super.handleOnStart();
        if (connectionService == null) {
            connectionService = new ConnectionService(bluetoothAdapter, handler);
        }
    }

    @Override
    protected void handleOnResume() {
        super.handleOnResume();
        if (connectionService != null && connectionService.getState() == ConnectionService.ConnectionState.None) {
            connectionService.start();
        }
    }

    @Override
    protected void handleOnDestroy() {
        super.handleOnDestroy();
        disconnectDo();
    }

    private final Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (ConnectionService.MessageWhat.values()[msg.what]) {
                case Connected: {
                    if (connectionCallbackId != null) {
                        var call = bridge.getSavedCall(connectionCallbackId);
                        var ret = ((ConnectResponse) msg.obj).serialize();
                        call.resolve(ret);
                    }
                    break;
                }
                case ConnectionError: {
                    if (connectionCallbackId != null) {
                        bridge.getSavedCall(connectionCallbackId).reject((String) msg.obj);
                        bridge.releaseCall(connectionCallbackId);
                        connectionCallbackId = null;
                    }
                    break;
                }
                case WriteComplete: {
                    if (writerCallbackId != null) {
                        bridge.getSavedCall(writerCallbackId).resolve();
                        bridge.releaseCall(writerCallbackId);
                        writerCallbackId = null;
                    }
                    break;
                }
                case WriteError: {
                    if (writerCallbackId != null) {
                        bridge.getSavedCall(writerCallbackId).reject((String) msg.obj);
                        bridge.releaseCall(writerCallbackId);
                        writerCallbackId = null;
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @PermissionCallback
    @SuppressLint("MissingPermission")
    private void discoverDo(PluginCall call) {
        var devices = bluetoothAdapter.getBondedDevices();
        var ret = new DiscoverResponse(devices).serialize();
        call.resolve(ret);
    }

    @PermissionCallback
    private void connectDo(PluginCall call) {
        ConnectRequest request = new ConnectRequest(call);
        if (!BluetoothAdapter.checkBluetoothAddress(request.getAddress())) {
            call.reject("invalid address");
            return;
        }

        if (connectionService == null) {
            call.reject("service failure");
            return;
        }

        call.setKeepAlive(true);
        connectionCallbackId = call.getCallbackId();
        bridge.saveCall(call);

        connectionService.connect(request.getAddress());
    }

    private void disconnectDo() {
        if (connectionCallbackId != null) {
            bridge.releaseCall(connectionCallbackId);
            connectionCallbackId = null;
        }

        if (writerCallbackId != null) {
            bridge.releaseCall(writerCallbackId);
            writerCallbackId = null;
        }

        if (connectionService != null) {
            connectionService.stop();
            connectionService = null;
        }
    }

    @PermissionCallback
    private void printDo(PluginCall call) {
        PrintRequest request = new PrintRequest(call);
        if (request.isInvalid()) {
            call.resolve();
            return;
        }

        if (connectionCallbackId == null) {
            call.reject("connection failure");
            return;
        }

        if (connectionService == null) {
            call.reject("service failure");
            return;
        }

        writerCallbackId = call.getCallbackId();
        bridge.saveCall(call);

        connectionService.write(Command.Init);
        for (int i = 0; i < request.getBlocks().length(); i++) {
            PrintBlock block = request.getBlockAt(i);
            if (block == null) {
                continue;
            }

            connectionService.write(Command.LF);
            switch (block.getType()) {
                case textRaw:
                    textRaw((PrintBlockTextRaw) block, request.getPageWidth());
                    break;
                case textDraw:
                    textDraw((PrintBlockTextDraw) block, request.getPageWidth());
                    break;
                case textQr:
                    textQr((PrintBlockTextQr) block, request.getPageWidth());
                    break;
                default:
                    break;
            }
        }

        connectionService.write(Utility.feedPaper(request.getFeed()));
    }

    private void textRaw(PrintBlockTextRaw block, int ignoredPageWidth) {
        if (block.isInvalid()) {
            return;
        }

        byte[] data = TextProcessor.textAsPrintable(
            block.getValue(),
            block.getEncoding(),
            block.getCodepage(),
            block.getScaleWidth(),
            block.getScaleHeight(),
            block.getFontStyle(),
            block.getAlign()
        );
        connectionService.write(data);
    }

    private void textDraw(PrintBlockTextDraw block, int pageWidth) {
        if (block.isInvalid()) {
            return;
        }

        Bitmap rendered = TextProcessor.drawText(
            block.getValue(),
            block.getFontSize(),
            pageWidth,
            block.getLineCount() * block.getFontSize() * 1.5
        );
        byte[] data = ImageProcessor.imageAsPrintable(rendered, block.getMode(), pageWidth);
        connectionService.write(data);
    }

    private void textQr(PrintBlockTextQr block, int pageWidth) {
        if (block.isInvalid()) {
            return;
        }

        Bitmap rendered = ImageProcessor.textToQR(block.getValue(), block.getCodeWidth(), block.getCodeWidth());
        if (rendered == null) {
            return;
        }

        byte[] data = ImageProcessor.imageAsPrintable(rendered, block.getMode(), pageWidth);
        connectionService.write(data);
    }
}
