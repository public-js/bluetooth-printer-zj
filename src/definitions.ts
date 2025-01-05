import type { ConnectRequest } from './dto/ConnectRequest';
import type { ConnectResponse } from './dto/ConnectResponse';
import type { DiscoverResponse } from './dto/DiscoverResponse';
import type { PermissionStatus } from './dto/PermissionStatus';
import type { PrintRequest } from './dto/PrintRequest';

export interface BluetoothPrinterZjPlugin {
  discover(): Promise<DiscoverResponse>;
  connect(data: ConnectRequest): Promise<ConnectResponse>;
  disconnect(): Promise<void>;
  print(data: PrintRequest): Promise<void>;
  checkPermissions(): Promise<PermissionStatus>;
  requestPermissions(): Promise<PermissionStatus>;
}

export type { ConnectRequest, ConnectResponse, DiscoverResponse, PermissionStatus, PrintRequest };
export type { PrintBlockLf } from './dto/PrintBlockLf';
export type { PrintBlockTextDraw } from './dto/PrintBlockTextDraw';
export type { PrintBlockTextQr } from './dto/PrintBlockTextQr';
export type { PrintBlockTextRaw } from './dto/PrintBlockTextRaw';
export type { PrintBlockType } from './dto/PrintBlockType';
