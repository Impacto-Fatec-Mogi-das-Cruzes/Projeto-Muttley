import express, { Request, Response } from 'express';
import CertificateRouter from './routers/CertificateRouter';

const app = express();
const port = 1336;

app.use(express.json());

app.use('/api/certificate', CertificateRouter);

app.listen(port, () => {
  console.log(`[server]: Server is running at http://localhost:${port}`);
});