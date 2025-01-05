import { registerPlugin } from '@capacitor/core';

import type { BluetoothPrinterZjPlugin } from './definitions';

const BluetoothPrinterZj = registerPlugin<BluetoothPrinterZjPlugin>('BluetoothPrinterZj', {
  web: () => import('./web').then((m) => new m.BluetoothPrinterZjWeb()),
});

export * from './definitions';
export { BluetoothPrinterZj };
