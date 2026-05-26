import { Router } from 'express';
import multer from 'multer';
import { CertificateController } from '../controllers/CertificateController';

const router = Router();
const controller = new CertificateController();

const upload = multer({ storage: multer.memoryStorage() });

const imageFields = upload.fields([
  { name: 'backgroundImage', maxCount: 1 },
  { name: 'signatureImage', maxCount: 1 },
]);

router.post('/generate', imageFields, (req, res) => controller.generate(req, res));

export default router;