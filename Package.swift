// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "PublicJsBluetoothPrinterZj",
    platforms: [.iOS(.v13)],
    products: [
        .library(
            name: "PublicJsBluetoothPrinterZj",
            targets: ["BluetoothPrinterZjPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", branch: "main")
    ],
    targets: [
        .target(
            name: "BluetoothPrinterZjPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/BluetoothPrinterZjPlugin"),
        .testTarget(
            name: "BluetoothPrinterZjPluginTests",
            dependencies: ["BluetoothPrinterZjPlugin"],
            path: "ios/Tests/BluetoothPrinterZjPluginTests")
    ]
)
