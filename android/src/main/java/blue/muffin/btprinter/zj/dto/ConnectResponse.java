package blue.muffin.btprinter.zj.dto;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import com.getcapacitor.JSObject;

public final class ConnectResponse {

    private final String device;

    @SuppressLint("MissingPermission")
    public ConnectResponse(BluetoothDevice device) {
        this.device = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ? device.getAlias() : device.getName();
    }

    public JSObject serialize() {
        return (new JSObject()).put("device", device);
    }
}
