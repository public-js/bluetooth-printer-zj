import type { PrintBlockLf } from './PrintBlockLf';
import type { PrintBlockTextDraw } from './PrintBlockTextDraw';
import type { PrintBlockTextQr } from './PrintBlockTextQr';
import type { PrintBlockTextRaw } from './PrintBlockTextRaw';

type PrintBlock = PrintBlockLf | PrintBlockTextRaw | PrintBlockTextDraw | PrintBlockTextQr;

export interface PrintRequest {
  blocks: PrintBlock[];
  pageWidth: number;
  feed: number;
}
