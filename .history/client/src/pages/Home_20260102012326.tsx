import { Button } from "../components/ui/button";
import { Link } from "../lib/router";
import { navigate } from "../lib/router-utils";

export default function HomePage() {
  return (
    <main className="min-h-screen flex items-center justify-center px-4">
      <div className="w-full max-w-2xl space-y-10">
        {/* Hero */}
        <section className="space-y-4 text-center">
          <h1 className="text-4xl sm:text-5xl md:text-6xl font-beauty text-white text-shadow-silver font-semibold tracking-tight">
            Todo list
          </h1>
          <p className="text-sm text-white text-shadow">Simple. Fast. Reliable.</p>

          <div className="flex flex-col gap-3 sm:flex-row sm:justify-center">
            <Button
              className="w-full sm:w-auto"
              onClick={() => navigate("/register")}
            >
              Get started
            </Button>

            <Button asChild variant="ghost" className="w-full sm:w-auto">
              <Link to="/login">Sign in</Link>
            </Button>
          </div>
        </section>
      </div>
    </main>
  );
}
