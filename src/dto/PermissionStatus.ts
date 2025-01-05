import type { PermissionState } from '@capacitor/core';

export interface PermissionStatus {
  connect: PermissionState;
  admin: PermissionState;
}
