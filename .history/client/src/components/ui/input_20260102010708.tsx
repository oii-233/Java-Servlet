import type { InputHTMLAttributes } from "react";

export function Input(props: InputHTMLAttributes<HTMLInputElement>) {
  return (
    <input
      {...props}
      className={[
        "w-full rounded-md border border-neutral-300 px-3 py-2 text-sm",
        "focus:outline-none focus:ring-2 focus:ring-neutral-900",
        props.className || "",
      ].join(" ")}
    />
  );
}
