import { Request, Response } from 'express';
import { PdfService } from '../services/PdfService';
import { CertificateDTO } from '../dtos/CertificateDTO';

export class CertificateController {
  async generate(req: Request, res: Response): Promise<void> {
    try {
      const files = req.files as Record<string, Express.Multer.File[]>;

      const backgroundImage = files['backgroundImage']?.[0]?.buffer;
      const signatureImage = files['signatureImage']?.[0]?.buffer;

      if (!backgroundImage || !signatureImage) {
        res.status(400).json({ error: 'backgroundImage and signatureImage are required.' });
        return;
      }

      const { name, presentation, event, day, hours, responsible, responsibleDescription } = req.body;

      if (!name || !presentation || !event || !day || !hours || !responsible || !responsibleDescription) {
        res.status(400).json({
          error: 'All text fields are required: name, presentation, event, day, hours, responsible, responsibleDescription.',
        });
        return;
      }

      const dto: CertificateDTO = {
        name,
        presentation,
        event,
        day,
        hours: Number(hours),
        backgroundImage,
        signatureImage,
        responsible,
        responsibleDescription,
      };

      const service = new PdfService(dto);
      const filePath = await service.generate();

      res.download(filePath, `certificate_${name.replace(/\s+/g, '_')}.pdf`);
    } catch (err) {
      console.error('[CertificateController] Error generating certificate:', err);
      res.status(500).json({ error: 'Failed to generate certificate.' });
    }
  }
}