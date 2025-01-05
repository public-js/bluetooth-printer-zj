package blue.muffin.btprinter.zj.dto;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import com.getcapacitor.JSObject;
import org.json.JSONObject;

final class DiscoveredDevice {

    private final String address;
    private final String name;
    private final boolean bonded;

    @SuppressLint("MissingPermission")
    public DiscoveredDevice(BluetoothDevice device) {
        this.address = device.getAddress();
        this.name = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ? device.getAlias() : device.getName();
        this.bonded = device.getBondState() == BluetoothDevice.BOND_BONDED;
    }

    public JSONObject serialize() {
        return (new JSObject()).put("address", address).put("name", name).put("bonded", bonded);
    }
}
