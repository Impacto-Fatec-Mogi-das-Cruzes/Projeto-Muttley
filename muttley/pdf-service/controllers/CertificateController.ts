import { Request, Response } from 'express';
import { PdfService } from '../services/PdfService';
import { CertificateDTO } from '../dtos/CertificateDTO';
import { CertificateCustomDTO } from '../dtos/CertificateCustomDTO';

export class CertificateController {

  async generate(
    req: Request,
    res: Response
  ): Promise<void> {
    return this.handleGenerate(req, res, false);
  }

  async generateCustom(
    req: Request,
    res: Response
  ): Promise<void> {
    return this.handleGenerate(req, res, true);
  }

  private async handleGenerate(
    req: Request,
    res: Response,
    isCustom: boolean
  ): Promise<void> {

    try {

      const {
        name,
        presentation,
        event,
        day,
        hours,
        description,
        responsible,
        responsibleDescription,
        backgroundImageUrl,
        signatureImageUrl,
        certificateCode
      } = req.body;

      const commonValidation =
        !name ||
        !responsible ||
        !responsibleDescription ||
        !backgroundImageUrl ||
        !signatureImageUrl
      !certificateCode;

      if (commonValidation) {
        res.status(400).json({
          error:
            'Required: name, responsible, responsibleDescription, backgroundImageUrl, signatureImageUrl, certificateCode.',
        });

        return;
      }

      let dto: CertificateDTO | CertificateCustomDTO;

      if (isCustom) {

        if (!description) {

          res.status(400).json({
            error:
              'Required: description.',
          });

          return;
        }

        dto = {
          name,
          description,
          responsible,
          responsibleDescription,
          backgroundImageUrl,
          signatureImageUrl,
          certificateCode
        };

      } else {

        if (
          !presentation ||
          !event ||
          !day ||
          hours === undefined
        ) {

          res.status(400).json({
            error:
              'Required: presentation, event, day, hours.',
          });

          return;
        }

        dto = {
          name,
          presentation,
          event,
          day,
          hours: Number(hours),
          responsible,
          responsibleDescription,
          backgroundImageUrl,
          signatureImageUrl,
          certificateCode
        };
      }

      const pdfBuffer =
        await new PdfService(dto)
          .generate();

      const fileName =
        `certificate_${name.replace(/\s+/g, '_')}.pdf`;

      res.setHeader(
        'Content-Type',
        'application/pdf'
      );

      res.setHeader(
        'Content-Disposition',
        `attachment; filename="${fileName}"`
      );

      res.setHeader(
        'Content-Length',
        pdfBuffer.length
      );

      res.send(pdfBuffer);

    } catch (err) {

      console.error(
        '[CertificateController] Error generating certificate:',
        err
      );

      res.status(500).json({
        error:
          'Failed to generate certificate.',
      });
    }
  }
}