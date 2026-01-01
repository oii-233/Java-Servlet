import type { ReactNode } from "react";

export function Card({ children }: { children: ReactNode }) {
  return (
    <div className="border border-neutral-700 rounded-xl bg-black/50 shadow-sm p-4">
      {children}
    </div>
  );
}

export function CardHeader({ children }: { children: ReactNode }) {
  return (
    <div className="mb-2 flex items-center justify-between">{children}</div>
  );
}

export function CardTitle({ children }: { children: ReactNode }) {
  return (
    <h2 className="text-lg font-semibold text-white font-beauty">{children}</h2>
  );
}

export function CardContent({ children }: { children: ReactNode }) {
  return <div className="space-y-2">{children}</div>;
}
