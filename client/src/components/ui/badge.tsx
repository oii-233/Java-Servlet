import type { ReactNode } from "react";

export function Badge({ children }: { children: ReactNode }) {
  return (
    <span className="inline-flex items-center rounded-full bg-neutral-200 px-2.5 py-0.5 text-xs font-medium text-neutral-800">
      {children}
    </span>
  );
}
