import type { PrintBlockText } from './PrintBlock';

export interface PrintBlockTextQr extends PrintBlockText {
  type: 'textQr';
  codeWidth: number;
  /** @default 0 */
  mode: number;
}
