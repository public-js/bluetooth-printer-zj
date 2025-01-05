package blue.muffin.btprinter.zj.dto;

import android.bluetooth.BluetoothDevice;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import java.util.Set;

public final class DiscoverResponse {

    private final Set<BluetoothDevice> devices;

    public DiscoverResponse(Set<BluetoothDevice> devices) {
        this.devices = devices;
    }

    public JSObject serialize() {
        JSArray _devices = new JSArray();
        for (BluetoothDevice device : devices) {
            _devices.put(new DiscoveredDevice(device).serialize());
        }

        return (new JSObject()).put("devices", _devices);
    }
}
