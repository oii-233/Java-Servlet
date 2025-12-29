import "./App.css";
import { useAuthStore } from "./store/auth";
import { usePath, navigate } from "./lib/router";
import HomePage from "./pages/Home";
import LoginPage from "./pages/Login";
import RegisterPage from "./pages/Register";
import DashboardPage from "./pages/Dashboard";

function App() {
  const path = usePath();
  const { token } = useAuthStore();

  if (path === "/dashboard" && !token) {
    navigate("/login", true);
  }
  if ((path === "/login" || path === "/register") && token) {
    navigate("/dashboard", true);
  }

  if (path === "/login") return <LoginPage />;
  if (path === "/register") return <RegisterPage />;
  if (path === "/dashboard") return <DashboardPage />;
  return <HomePage />;
}

export default App;
