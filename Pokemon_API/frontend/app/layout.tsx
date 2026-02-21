import React from "react";
import "./globals.css";

export const metadata = {
  title: "Pokémon Battle - Conviértete en el mejor entrenador",
  description: "Juego de batallas Pokémon con los 151 de Gen 1"
};

export default function RootLayout({
  children
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="es">
      <body>{children}</body>
    </html>
  );
}
