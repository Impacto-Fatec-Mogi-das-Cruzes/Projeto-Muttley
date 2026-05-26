import { PDFDocument, rgb, PDFFont, PDFPage } from 'pdf-lib';
import fontkit from '@pdf-lib/fontkit';
import * as fs from 'fs';
import * as path from 'path';
import sharp from 'sharp';
import { CertificateDTO } from '../dtos/CertificateDTO';

type RGB = { r: number; g: number; b: number };

const RED: RGB = { r: 0.698, g: 0.094, b: 0.122 };
const DARK: RGB = { r: 0.1, g: 0.1, b: 0.1 };

function drawCentred(
  page: PDFPage,
  text: string,
  font: PDFFont,
  fontSize: number,
  y: number,
  color: RGB = DARK
): void {
  const textWidth = font.widthOfTextAtSize(text, fontSize);
  page.drawText(text, {
    x: (page.getWidth() - textWidth) / 2,
    y,
    size: fontSize,
    font,
    color: rgb(color.r, color.g, color.b),
  });
}

function drawWrappedCentred(
  page: PDFPage,
  text: string,
  font: PDFFont,
  fontSize: number,
  startY: number,
  maxWidth: number,
  lineHeight: number,
  color: RGB = DARK
): number {
  const words = text.split(' ');
  const lines: string[] = [];
  let current = '';

  for (const word of words) {
    const candidate = current ? `${current} ${word}` : word;
    if (font.widthOfTextAtSize(candidate, fontSize) <= maxWidth) {
      current = candidate;
    } else {
      if (current) lines.push(current);
      current = word;
    }
  }
  if (current) lines.push(current);

  let y = startY;
  for (const line of lines) {
    drawCentred(page, line, font, fontSize, y, color);
    y -= lineHeight;
  }
  return y;
}

async function toPng(buffer: Buffer): Promise<Buffer> {
  return sharp(buffer).png().toBuffer();
}

function dateToText(dateStr: string): string {
  const [year, month, day] = dateStr.split('-').map(Number);
  const date = new Date(Date.UTC(year, month - 1, day));
  return new Intl.DateTimeFormat('pt-BR', {
    day: '2-digit',
    month: 'long',
    year: 'numeric',
    timeZone: 'UTC',
  }).format(date);
}

function todayText(): string {
  return new Intl.DateTimeFormat('pt-BR', {
    day: '2-digit',
    month: 'long',
    year: 'numeric',
  }).format(new Date());
}

export class PdfService {
  private static readonly PAGE_WIDTH = 841.89;
  private static readonly PAGE_HEIGHT = 595.28;
  private static readonly OUTPUT_DIR = path.resolve(__dirname, '..', 'pdfs');
  private static readonly FONT_DIR = path.resolve(__dirname, 'fonts');
  private static readonly ROBOTO_REGULAR = path.join(PdfService.FONT_DIR, 'Roboto.ttf');
  private static readonly ROBOTO_BOLD = path.join(PdfService.FONT_DIR, 'RobotoBold.ttf');

  constructor(private dto: CertificateDTO) { }

  async generate(): Promise<string> {
    const pdfDoc = await PDFDocument.create();
    pdfDoc.registerFontkit(fontkit);

    const page = pdfDoc.addPage([PdfService.PAGE_WIDTH, PdfService.PAGE_HEIGHT]);

    await this.drawBackground(pdfDoc, page);
    await this.drawContent(pdfDoc, page);

    const bytes = await pdfDoc.save();

    if (!fs.existsSync(PdfService.OUTPUT_DIR)) {
      fs.mkdirSync(PdfService.OUTPUT_DIR, { recursive: true });
    }

    const safeName = this.dto.name.replace(/\s+/g, '_');
    const fileName = `certificate_${safeName}_${Date.now()}.pdf`;
    const filePath = path.join(PdfService.OUTPUT_DIR, fileName);
    fs.writeFileSync(filePath, Buffer.from(bytes));

    return filePath;
  }

  private async drawBackground(pdfDoc: PDFDocument, page: PDFPage): Promise<void> {
    const png = await toPng(this.dto.backgroundImage);
    const img = await pdfDoc.embedPng(png);
    page.drawImage(img, {
      x: 0,
      y: 0,
      width: PdfService.PAGE_WIDTH,
      height: PdfService.PAGE_HEIGHT,
    });
  }

  private async drawContent(pdfDoc: PDFDocument, page: PDFPage): Promise<void> {
    const regularBytes = fs.readFileSync(PdfService.ROBOTO_REGULAR);
    const boldBytes = fs.readFileSync(PdfService.ROBOTO_BOLD);

    const regularFont = await pdfDoc.embedFont(regularBytes);
    const boldFont = await pdfDoc.embedFont(boldBytes);

    const W = PdfService.PAGE_WIDTH;
    const H = PdfService.PAGE_HEIGHT;
    const margin = 80;
    const textWidth = W - margin * 2;

    drawCentred(page, 'Certificamos que', regularFont, 14, H - 185, DARK);
    drawCentred(page, this.dto.name.toUpperCase(), boldFont, 22, H - 215, DARK);

    const eventDate = dateToText(this.dto.day);

    const segments: { text: string; bold: boolean }[] = [
      { text: 'Ministrou a apresentação de ', bold: false },
      { text: this.dto.presentation, bold: true },
      { text: ', durante o evento ', bold: false },
      { text: this.dto.event, bold: true },
      { text: `, realizada em ${eventDate} e promovida pela Fatec Zona Leste. Contabilizando carga horária total de ${this.dto.hours} hora(s).`, bold: false },
    ];

    const bodyEndY = this.drawJustifiedMixed(
      page,
      segments,
      regularFont,
      boldFont,
      18,
      H - 255,
      margin,
      textWidth,
      26
    );

    const dateY = bodyEndY - 24;
    drawCentred(page, `São Paulo, ${todayText()}.`, regularFont, 14, dateY, DARK);

    await this.drawSignatureBlock(pdfDoc, page, dateY - 16, regularFont, boldFont);
  }

  private drawJustifiedMixed(
    page: PDFPage,
    segments: { text: string; bold: boolean }[],
    regularFont: PDFFont,
    boldFont: PDFFont,
    fontSize: number,
    startY: number,
    startX: number,
    maxWidth: number,
    lineHeight: number
  ): number {
    type Token = { word: string; bold: boolean };
    const tokens: Token[] = [];

    for (const seg of segments) {
      const words = seg.text.split(/(\s+)/);
      for (const w of words) {
        if (w.trim()) tokens.push({ word: w.trim(), bold: seg.bold });
      }
    }

    const lines: Token[][] = [];
    let currentLine: Token[] = [];
    let currentWidth = 0;

    const tokenWidth = (t: Token): number =>
      (t.bold ? boldFont : regularFont).widthOfTextAtSize(t.word, fontSize);

    const SPACE = regularFont.widthOfTextAtSize(' ', fontSize);

    for (const token of tokens) {
      const tw = tokenWidth(token);
      const needed = currentLine.length === 0 ? tw : SPACE + tw;

      if (currentLine.length === 0 || currentWidth + needed <= maxWidth) {
        currentLine.push(token);
        currentWidth += needed;
      } else {
        lines.push(currentLine);
        currentLine = [token];
        currentWidth = tw;
      }
    }
    if (currentLine.length) lines.push(currentLine);

    let y = startY;
    for (let i = 0; i < lines.length; i++) {
      const line = lines[i];
      const isLast = i === lines.length - 1;

      if (isLast || line.length === 1) {
        const totalW = line.reduce(
          (acc, t, idx) => acc + tokenWidth(t) + (idx > 0 ? SPACE : 0),
          0
        );
        let x = startX + (maxWidth - totalW) / 2;
        for (let j = 0; j < line.length; j++) {
          const t = line[j];
          if (j > 0) x += SPACE;
          page.drawText(t.word, {
            x,
            y,
            size: fontSize,
            font: t.bold ? boldFont : regularFont,
            color: rgb(DARK.r, DARK.g, DARK.b),
          });
          x += tokenWidth(t);
        }
      } else {
        const totalWordW = line.reduce((acc, t) => acc + tokenWidth(t), 0);
        const gap = (maxWidth - totalWordW) / (line.length - 1);

        let x = startX;
        for (const t of line) {
          page.drawText(t.word, {
            x,
            y,
            size: fontSize,
            font: t.bold ? boldFont : regularFont,
            color: rgb(DARK.r, DARK.g, DARK.b),
          });
          x += tokenWidth(t) + gap;
        }
      }
      y -= lineHeight;
    }
    return y;
  }

  private async drawSignatureBlock(
    pdfDoc: PDFDocument,
    page: PDFPage,
    topY: number,
    regularFont: PDFFont,
    boldFont: PDFFont
  ): Promise<void> {
    const W = PdfService.PAGE_WIDTH;

    const sigPng = await toPng(this.dto.signatureImage);
    const sigImg = await pdfDoc.embedPng(sigPng);
    const sigMeta = sigImg.scale(1);
    const sigScale = Math.min(160 / sigMeta.width, 60 / sigMeta.height);
    const sigW = sigMeta.width * sigScale;
    const sigH = sigMeta.height * sigScale;
    const sigY = topY - sigH;

    page.drawImage(sigImg, {
      x: (W - sigW) / 2,
      y: sigY,
      width: sigW,
      height: sigH,
    });

    const lineW = 200;
    const lineY = sigY - 8;
    page.drawLine({
      start: { x: (W - lineW) / 2, y: lineY },
      end: { x: (W + lineW) / 2, y: lineY },
      thickness: 0.8,
      color: rgb(0.3, 0.3, 0.3),
    });

    drawCentred(page, this.dto.responsible.toUpperCase(), boldFont, 12, lineY - 16, RED);

    drawWrappedCentred(
      page,
      this.dto.responsibleDescription,
      regularFont,
      12,
      lineY - 32,
      300,
      16,
      DARK
    );
  }
}