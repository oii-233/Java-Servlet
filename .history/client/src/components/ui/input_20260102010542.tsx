import type { InputHTMLAttributes } from "react";

export function Input(props: InputHTMLAttributes<HTMLInputElement>) {
  return (
    <input
      {...props}
      className={[
        "w-full rounded-md border border-neutral-600 bg-transparent px-3 py-2 text-sm text-white placeholder:text-neutral-400",
        "focus:outline-none focus:ring-2 focus:ring-neutral-500",
        props.className || "",
      ].join(" ")}
    />
  );
}
