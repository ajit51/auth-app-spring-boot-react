import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.tsx";
import { BrowserRouter, Routes, Route } from "react-router";
import Login from "./pages/Login.tsx";
import About from "./pages/About.tsx";
import Singnup from "./pages/Singnup.tsx";
import Services from "./pages/Services.tsx";

createRoot(document.getElementById("root")!).render(
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<App />} />
      <Route path="/login" element={<Login />} />
      <Route path="/about" element={<About />} />
      <Route path="/signup" element={<Singnup />} />
      <Route path="/services" element={<Services />} />
    </Routes>
  </BrowserRouter>,
);
