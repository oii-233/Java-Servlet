import { useEffect } from "react";

export function AlertDialog({
  open,
  title,
  description,
  confirmText,
  cancelText,
  onConfirm,
  onClose,
}: {
  open: boolean;
  title: string;
  description?: string;
  confirmText?: string;
  cancelText?: string;
  onConfirm: () => void;
  onClose: () => void;
}) {
  useEffect(() => {
    const onKey = (e: KeyboardEvent) => {
      if (e.key === "Escape") onClose();
    };
    document.addEventListener("keydown", onKey);
    return () => document.removeEventListener("keydown", onKey);
  }, [onClose]);

  if (!open) return null;
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40">
      <div className="w-full max-w-sm rounded-xl bg-white p-4 shadow-md">
        <h3 className="text-lg font-semibold">{title}</h3>
        {description ? (
          <p className="mt-1 text-sm text-neutral-700">{description}</p>
        ) : null}
        <div className="mt-4 flex justify-end gap-2">
          <button
            className="inline-flex items-center justify-center rounded-md border border-neutral-300 bg-white px-3 py-2 text-sm font-medium text-neutral-900"
            onClick={onClose}
          >
            {cancelText ?? "Cancel"}
          </button>
          <button
            className="inline-flex items-center justify-center rounded-md bg-red-600 px-3 py-2 text-sm font-medium text-white"
            onClick={onConfirm}
          >
            {confirmText ?? "Delete"}
          </button>
        </div>
      </div>
    </div>
  );
}
