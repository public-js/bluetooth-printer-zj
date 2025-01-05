import { WebPlugin } from '@capacitor/core';

import type { BluetoothPrinterZjPlugin } from './definitions';

export class BluetoothPrinterZjWeb extends WebPlugin implements BluetoothPrinterZjPlugin {
  async echo(): Promise<void> {
    throw this.unimplemented('Unavailable on web');
  }
}
