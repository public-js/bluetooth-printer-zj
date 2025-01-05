import type { PrintBlockText } from './PrintBlock';

export interface PrintBlockTextRaw extends PrintBlockText {
  type: 'textRaw';
  /** @default GBK */
  encoding: string;
  /** @default 0 */
  codepage: number;
  /** @default 0 */
  scaleWidth: number;
  /** @default 0 */
  scaleHeight: number;
  /** @default 0 */
  fontStyle: number;
  /**
   * @enum { Left = 0, Center = 1, Right = 2 }
   * @default 0
   */
  align: number;
}
