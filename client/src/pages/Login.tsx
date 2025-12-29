import { useState } from "react";
import { Button } from "../components/ui/button";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "../components/ui/card";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { navigate } from "../lib/router";
import { useAuthStore } from "../store/auth";

export default function LoginPage() {
  const { login, status, error, token } = useAuthStore();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [localError, setLocalError] = useState("");

  if (token) {
    navigate("/dashboard", true);
    return null;
  }

  const canSubmit =
    username.trim().length > 0 &&
    password.trim().length > 0 &&
    status !== "loading";

  return (
    <div className="mx-auto max-w-sm">
      <Card>
        <CardHeader>
          <CardTitle>Sign In</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            <div className="space-y-1">
              <Label htmlFor="username">Username</Label>
              <Input
                id="username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
            </div>
            <div className="space-y-1">
              <Label htmlFor="password">Password</Label>
              <Input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>
            {(localError || error) && (
              <p className="text-sm text-red-700">{localError || error}</p>
            )}
            <div className="flex items-center gap-2">
              <Button
                onClick={async () => {
                  if (!canSubmit) {
                    setLocalError("Enter username and password");
                    return;
                  }
                  setLocalError("");
                  await login({ username, password });
                  navigate("/dashboard");
                }}
                disabled={!canSubmit}
              >
                {status === "loading" ? "Signing in…" : "Sign In"}
              </Button>
              <Button variant="ghost" onClick={() => navigate("/register")}>
                Create account
              </Button>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
