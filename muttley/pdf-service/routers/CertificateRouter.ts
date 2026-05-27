import { Router } from 'express';
import { CertificateController } from '../controllers/CertificateController';

const router = Router();
const controller = new CertificateController();

router.post('/generate', (req, res) =>
  controller.generate(req, res)
);

router.post('/generate-custom', (req, res) =>
  controller.generateCustom(req, res)
);

export default router;