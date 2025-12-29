import type { InputHTMLAttributes } from "react";

export function Checkbox(props: InputHTMLAttributes<HTMLInputElement>) {
  return (
    <input
      type="checkbox"
      {...props}
      className={[
        "h-4 w-4 rounded border border-neutral-300 text-neutral-900",
        "focus:outline-none focus:ring-2 focus:ring-neutral-900",
        props.className || "",
      ].join(" ")}
    />
  );
}
