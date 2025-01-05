package blue.muffin.btprinter.zj.dto;

import com.getcapacitor.PluginCall;

public final class ConnectRequest {

    private final String address;

    public ConnectRequest(PluginCall call) {
        this.address = call.getString("address");
    }

    public String getAddress() {
        return address;
    }
}
