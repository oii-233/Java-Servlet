import type { ButtonHTMLAttributes, ReactNode } from "react";

type Props = ButtonHTMLAttributes<HTMLButtonElement> & {
  variant?: "default" | "ghost" | "destructive";
  children: ReactNode;
};

export function Button({
  variant = "default",
  className,
  children,
  ...props
}: Props) {
  const base =
    "inline-flex items-center justify-center rounded-md text-sm font-medium transition-colors focus:outline-none disabled:opacity-60 disabled:cursor-not-allowed";
  const variants: Record<string, string> = {
    default: "bg-neutral-900 text-white hover:bg-neutral-800 px-3 py-2",
    ghost:
      "bg-white border border-neutral-300 text-neutral-900 hover:bg-neutral-50 px-3 py-2",
    destructive: "bg-red-600 text-white hover:bg-red-700 px-3 py-2",
  };
  return (
    <button
      {...props}
      className={[base, variants[variant], className || ""].join(" ").trim()}
    >
      {children}
    </button>
  );
}
