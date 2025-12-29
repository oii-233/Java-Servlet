import type { ReactNode, LabelHTMLAttributes } from "react";

export function Label({
  children,
  className,
  ...props
}: LabelHTMLAttributes<HTMLLabelElement> & { children: ReactNode }) {
  return (
    <label
      {...props}
      className={["text-sm font-medium text-neutral-800", className || ""].join(
        " "
      )}
    >
      {children}
    </label>
  );
}
