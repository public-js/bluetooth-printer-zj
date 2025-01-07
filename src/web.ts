import { WebPlugin } from '@capacitor/core';

import type { BluetoothPrinterZjPlugin } from './definitions';
import type { ConnectResponse } from './dto/ConnectResponse';
import type { DiscoverResponse } from './dto/DiscoverResponse';

export class BluetoothPrinterZjWeb extends WebPlugin implements BluetoothPrinterZjPlugin {
  async discover(): Promise<DiscoverResponse> {
    throw this.unimplemented('Unavailable on web');
  }

  async connect(): Promise<ConnectResponse> {
    throw this.unimplemented('Unavailable on web');
  }

  async disconnect(): Promise<void> {
    throw this.unimplemented('Unavailable on web');
  }

  async print(): Promise<void> {
    throw this.unimplemented('Unavailable on web');
  }
}
