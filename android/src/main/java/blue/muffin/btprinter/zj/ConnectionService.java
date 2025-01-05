package blue.muffin.btprinter.zj;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import blue.muffin.btprinter.zj.dto.ConnectResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@SuppressLint("MissingPermission")
class ConnectionService {

    // Name for the SDP record when creating a server socket
    private static final String SDP_NAME = "ZJPrinter";
    // Well-known SPP to connect to a Bluetooth serial board
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter adapter;
    private Handler handler;
    private ConnectionState state;

    private AcceptThread acceptThread;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;

    public ConnectionService(BluetoothAdapter adapter, Handler handler) {
        this.adapter = adapter;
        this.handler = handler;
        this.state = ConnectionState.None;
    }

    public ConnectionState getState() {
        return state;
    }

    private synchronized void setState(ConnectionState state) {
        this.state = state;
        handler.obtainMessage(MessageWhat.StateChange.ordinal(), state.ordinal()).sendToTarget();
    }

    public synchronized void start() {
        // Cancel any thread attempting to make a connection
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        // Cancel any thread currently running a connection
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        // Start the thread to listen on a BluetoothServerSocket
        if (acceptThread == null) {
            acceptThread = new AcceptThread();
            acceptThread.start();
        }

        setState(ConnectionState.Listening);
    }

    public synchronized void connect(String address) {
        // Cancel any thread attempting to make a connection
        if (state == ConnectionState.Connecting && connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        // Cancel any thread currently running a connection
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        // Start the thread to connect with the given device
        BluetoothDevice device = adapter.getRemoteDevice(address);
        connectThread = new ConnectThread(device);
        connectThread.start();

        setState(ConnectionState.Connecting);
    }

    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        // Cancel the thread that completed the connection
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        // Cancel any thread currently running a connection
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        // Cancel the accept thread because we only want to connect to one device
        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        connectedThread = new ConnectedThread(socket);
        connectedThread.start();

        setState(ConnectionState.Ready);
        handler.obtainMessage(MessageWhat.Connected.ordinal(), new ConnectResponse(device)).sendToTarget();
    }

    public synchronized void stop() {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }

        setState(ConnectionState.None);
    }

    public void write(byte[] out) {
        ConnectedThread tmp;
        synchronized (this) {
            if (state != ConnectionState.Ready) {
                return;
            }
            tmp = connectedThread;
        }
        tmp.write(out);
    }

    private void connectionFailed() {
        setState(ConnectionState.Listening);
        handler.obtainMessage(MessageWhat.ConnectionError.ordinal(), null).sendToTarget();
    }

    private class AcceptThread extends Thread {

        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            try {
                tmp = adapter.listenUsingRfcommWithServiceRecord(SDP_NAME, SPP_UUID);
            } catch (IOException ignored) {}

            this.mmServerSocket = tmp;
        }

        @Override
        public void run() {
            setName("AcceptThread");

            BluetoothSocket socket;
            while (state != ConnectionState.Ready) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException ignored) {
                    break;
                }

                if (socket != null) {
                    synchronized (ConnectionService.this) {
                        switch (state) {
                            case Listening:
                            case Connecting:
                                connected(socket, socket.getRemoteDevice());
                                break;
                            case None:
                            case Ready:
                                try {
                                    socket.close();
                                } catch (IOException ignored) {}
                                break;
                        }
                    }
                }
            }
        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException ignored) {}
        }
    }

    private class ConnectThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;

            try {
                tmp = device.createRfcommSocketToServiceRecord(SPP_UUID);
            } catch (IOException ignored) {}

            this.mmSocket = tmp;
            this.mmDevice = device;
        }

        @Override
        public void run() {
            setName("ConnectThread");

            adapter.cancelDiscovery();

            try {
                mmSocket.connect();
            } catch (IOException ignored) {
                connectionFailed();

                try {
                    mmSocket.close();
                } catch (IOException ignored2) {}

                ConnectionService.this.start();
                return;
            }

            synchronized (ConnectionService.this) {
                connectThread = null;
            }

            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException ignored) {}
        }
    }

    private class ConnectedThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            OutputStream tmpOut = null;

            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException ignored) {}

            this.mmOutStream = tmpOut;
            this.mmSocket = socket;
        }

        @Override
        public void run() {
            setName("ConnectedThread");
        }

        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
                mmOutStream.flush();
                handler.obtainMessage(MessageWhat.WriteComplete.ordinal(), null).sendToTarget();
            } catch (IOException ignored) {
                handler.obtainMessage(MessageWhat.WriteError.ordinal(), null).sendToTarget();
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException ignored) {}
        }
    }

    public enum ConnectionState {
        None,
        Listening,
        Connecting,
        Ready
    }

    public enum MessageWhat {
        StateChange,
        Connected,
        ConnectionError,
        WriteComplete,
        WriteError
    }
}
