import type { InputHTMLAttributes } from "react";

export function Input(props: InputHTMLAttributes<HTMLInputElement>) {
  return (
    <input
      {...props}
      className={[
        "w-full rounded-md px-3 py-2 text-sm bg-transparent text-white placeholder:text-neutral-400",
        "bg-blur",
        "focus:outline-none focus:ring-2 focus:ring-white/30",
        props.className || "",
      ].join(" ")}
    />
  );
}
