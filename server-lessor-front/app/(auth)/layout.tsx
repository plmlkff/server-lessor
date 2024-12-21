import type { Metadata } from "next";
import { Geist } from "next/font/google";
import "@/app/globals.css";
import Starter from "@/app/components/starter";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "ИС Курсовая"
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={`${geistSans.variable}`}>
        {children}
        <Starter/>
      </body>
    </html>
  );
}
