import type { ReactNode, LabelHTMLAttributes } from "react";

export function Label({
  children,
  className,
  ...props
}: LabelHTMLAttributes<HTMLLabelElement> & { children: ReactNode }) {
  return (
    <label
      {...props}
      className={[
        "inline-block text-sm font-medium text-white px-2 py-1 rounded",
        "bg-blur",
        className || "",
      ].join(" ")}
    >
      {children}
    </label>
  );
}
