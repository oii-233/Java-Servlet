import { Badge } from "../components/ui/badge";
import { Button } from "../components/ui/button";
import { Card, CardContent } from "../components/ui/card";
import { Separator } from "../components/ui/separator";
import { Link, navigate } from "../lib/router";

export default function HomePage() {
  return (
    <div className="mx-auto max-w-2xl space-y-6">
      <div className="space-y-3 text-center">
        <h1 className="text-3xl font-semibold text-neutral-900">Todo List</h1>
        <p className="text-neutral-700">
          Simple, fast, and reliable task tracking.
        </p>
        <div className="flex items-center justify-center gap-3">
          <Button onClick={() => navigate("/register")}>Get Started</Button>
          <Link to="/login">
            <Button variant="ghost">Sign In</Button>
          </Link>
        </div>
      </div>
      <Separator />
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-3">
        <Card>
          <CardContent>
            <div className="flex items-center justify-between">
              <span className="text-sm font-medium text-neutral-900">
                Secure Auth
              </span>
              <Badge>JWT</Badge>
            </div>
            <p className="mt-1 text-sm text-neutral-700">
              Sign in securely with token-based auth.
            </p>
          </CardContent>
        </Card>
        <Card>
          <CardContent>
            <div className="flex items-center justify-between">
              <span className="text-sm font-medium text-neutral-900">
                Fast CRUD
              </span>
              <Badge>REST</Badge>
            </div>
            <p className="mt-1 text-sm text-neutral-700">
              Add, complete, and delete tasks smoothly.
            </p>
          </CardContent>
        </Card>
        <Card>
          <CardContent>
            <div className="flex items-center justify-between">
              <span className="text-sm font-medium text-neutral-900">
                Responsive UI
              </span>
              <Badge>shadcn</Badge>
            </div>
            <p className="mt-1 text-sm text-neutral-700">
              Clean components with subtle interactions.
            </p>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
