import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(BluetoothPrinterZjPlugin)
public class BluetoothPrinterZjPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "BluetoothPrinterZjPlugin"
    public let jsName = "BluetoothPrinterZj"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "discover", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "connect", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "disconnect", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "print", returnType: CAPPluginReturnPromise)
    ]

    @objc func discover(_ call: CAPPluginCall) {
        call.unimplemented("Unavailable on iOS")
    }

    @objc func connect(_ call: CAPPluginCall) {
        call.unimplemented("Unavailable on iOS")
    }

    @objc func disconnect(_ call: CAPPluginCall) {
        call.unimplemented("Unavailable on iOS")
    }

    @objc func print(_ call: CAPPluginCall) {
        call.unimplemented("Unavailable on iOS")
    }

    @objc override public func checkPermissions(_ call: CAPPluginCall) {
        call.unimplemented("Unavailable on iOS")
    }

    @objc override public func requestPermissions(_ call: CAPPluginCall) {
        call.unimplemented("Unavailable on iOS")
    }
}
