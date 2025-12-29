import { useEffect, useState } from "react";

export function navigate(path: string, replace = false) {
  if (replace) {
    history.replaceState({}, "", path);
  } else {
    history.pushState({}, "", path);
  }
  window.dispatchEvent(new PopStateEvent("popstate"));
}

export function usePath() {
  const [path, setPath] = useState<string>(location.pathname || "/");
  useEffect(() => {
    const handler = () => setPath(location.pathname || "/");
    window.addEventListener("popstate", handler);
    return () => window.removeEventListener("popstate", handler);
  }, []);
  return path;
}
