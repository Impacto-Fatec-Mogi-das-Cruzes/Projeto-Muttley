import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { Toaster } from "@/components/ui/toaster";
import { TooltipProvider } from "@/components/ui/tooltip";
import Login from "./pages/Login";
import Eventos from "./pages/Eventos";
import NovoEvento from "./pages/NovoEvento";
import EventoDetalhes from "./pages/EventoDetalhes";
import Participantes from "./pages/Participantes";
import HistoricoParticipante from "./pages/HistoricoParticipante";
import CertificadoPublico from "./pages/CertificadoPublico";
import EventRegistration from "./pages/EventRegistration";
import EventPresence from "./pages/EventPresence";
import { AppLayout } from "./components/AppLayout";
import NotFound from "./pages/NotFound";

const queryClient = new QueryClient();

const App = () => (
  <QueryClientProvider client={queryClient}>
    <TooltipProvider>
      <Toaster />
      <Sonner />
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/certificado/:id" element={<CertificadoPublico />} />
          <Route path="/certificate/:id" element={<CertificadoPublico />} />
          <Route path="/events/:eventId/registration" element={<EventRegistration />} />
          <Route path="/events/:eventId/presence" element={<EventPresence />} />

          <Route element={<AppLayout />}>
            <Route path="/eventos" element={<Eventos />} />
            <Route path="/eventos/novo" element={<NovoEvento />} />
            <Route path="/eventos/:id" element={<EventoDetalhes />} />
            <Route path="/participantes" element={<Participantes />} />
            <Route path="/participantes/:id/historico" element={<HistoricoParticipante />} />
          </Route>
          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </TooltipProvider>
  </QueryClientProvider>
);

export default App;
