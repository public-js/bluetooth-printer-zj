import type { PrintBlockText } from './PrintBlock';

export interface PrintBlockTextDraw extends PrintBlockText {
  type: 'textDraw';
  lineCount: number;
  /** @default 16 */
  fontSize?: number;
  /** @default 0 */
  mode?: number;
}
