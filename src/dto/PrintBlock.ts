import type { PrintBlockType } from './PrintBlockType';

export interface PrintBlock {
  /** @default lf */
  type: PrintBlockType;
}

export interface PrintBlockText extends PrintBlock {
  value: string;
}
