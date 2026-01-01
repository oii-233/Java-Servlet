import type { ReactNode } from "react";

export function Card({ children }: { children: ReactNode }) {
  return (
    <div className="border border-neutral-200 rounded-xl bg-white shadow-sm p-4">
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
  return <h2 className="text-lg font-semibold text-neutral-900">{children}</h2>;
}

export function CardContent({ children }: { children: ReactNode }) {
  return <div className="space-y-2">{children}</div>;
}
