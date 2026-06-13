import { Link, useLocation, useNavigate } from "react-router-dom";
import { Users, Calendar, LogOut } from "lucide-react";
import { Button } from "@/components/ui/button";

const navItems = [
  { label: "Participantes", path: "/participantes", icon: Users },
  { label: "Eventos", path: "/eventos", icon: Calendar },
];

export function Navbar() {
  const location = useLocation();
  const navigate = useNavigate();

  return (
    <header className="sticky top-0 z-50 border-b border-border bg-card shadow-sm">
      <div className="container flex h-14 items-center justify-between">
        <img src="/Fatec-zl.jpeg" alt="Fatec Zona Leste" className="h-9 w-auto" />

        <nav className="flex items-center gap-1">
          {navItems.map((item) => {
            const active = location.pathname.startsWith(item.path);
            return (
              <Link
                key={item.path}
                to={item.path}
                className={`flex items-center gap-1.5 rounded-md px-3 py-2 text-sm font-medium transition-colors ${
                  active
                    ? "bg-primary text-primary-foreground"
                    : "text-muted-foreground hover:bg-accent hover:text-accent-foreground"
                }`}
              >
                <item.icon className="h-4 w-4" />
                <span className="hidden md:inline">{item.label}</span>
              </Link>
            );
          })}
        </nav>

        <Button variant="ghost" size="sm" className="text-muted-foreground hover:text-destructive" onClick={() => navigate("/")}>
          <LogOut className="mr-1 h-4 w-4" />
          <span className="hidden sm:inline">Sair</span>
        </Button>
      </div>
    </header>
  );
}
