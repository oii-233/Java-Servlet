import { useEffect, useState } from "react";
import { Badge } from "../components/ui/badge";
import { Button } from "../components/ui/button";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "../components/ui/card";
import { Checkbox } from "../components/ui/checkbox";
import { Input } from "../components/ui/input";
import { Skeleton } from "../components/ui/skeleton";
import { AlertDialog } from "../components/ui/alert-dialog";
import { navigate } from "../lib/router-utils";
import { useAuthStore } from "../store/auth";
import { useTodoStore } from "../store/todos";

export default function DashboardPage() {
  const { user, token, logout } = useAuthStore();
  const {
    todos,
    status,
    error,
    fetchTodos,
    createTodo,
    updateTodo,
    deleteTodo,
  } = useTodoStore();

  const [title, setTitle] = useState("");
  const [confirmId, setConfirmId] = useState<number | null>(null);

  useEffect(() => {
    if (!token) {
      navigate("/login", true);
      return;
    }
    fetchTodos().catch(() => {});
  }, [token, fetchTodos]);

  return (
    <main className="min-h-screen px-4 py-8">
      <div className="mx-auto w-full max-w-2xl space-y-8">
        {/* Header */}
        <header className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h1 className="text-2xl text-white text-shadow-violet-100 font-semibold tracking-tight">Dashboard</h1>
            <p className="text-sm text-white text-shadow-blue-100">Manage your tasks</p>
          </div>

          <div className="flex items-center gap-2">
            <Badge variant="secondary">{user?.username}</Badge>
            <Button
              size="sm"
              variant="ghost"
              onClick={() => {
                logout();
                navigate("/");
              }}
            >
              Logout
            </Button>
          </div>
        </header>

        {/* Add Todo */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base">Add todo</CardTitle>
          </CardHeader>
          <CardContent className="space-y-2">
            <div className="flex flex-col gap-2 sm:flex-row">
              <Input
                placeholder="What needs to be done?"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
              />
              <Button
                className="sm:w-auto"
                disabled={!title.trim() || status === "loading"}
                onClick={async () => {
                  await createTodo(title.trim());
                  setTitle("");
                }}
              >
                Add
              </Button>
            </div>

            {error && <p className="text-sm text-red-600">{error}</p>}
          </CardContent>
        </Card>

        {/* Todos */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base">Todos</CardTitle>
          </CardHeader>
          <CardContent>
            {status === "loading" ? (
              <div className="space-y-2">
                <Skeleton className="h-10 w-full" />
                <Skeleton className="h-10 w-full" />
              </div>
            ) : todos.length === 0 ? (
              <p className="text-sm text-neutral-600">No todos yet</p>
            ) : (
              <ul className="space-y-2">
                {todos.map((todo) => (
                  <li
                    key={todo.id}
                    className="flex items-center justify-between rounded-md border p-3"
                  >
                    <label className="flex items-center gap-3">
                      <Checkbox
                        checked={todo.completed}
                        onCheckedChange={(checked) =>
                          updateTodo(todo.id, Boolean(checked))
                        }
                      />
                      <span
                        className={`text-2xl md:text-3xl font-beauty ${
                          todo.completed
                            ? "text-neutral-400 line-through"
                            : "text-white"
                        } text-shadow`}
                      >
                        {todo.title}
                      </span>
                    </label>

                    <Button
                      size="sm"
                      variant="ghost"
                      onClick={() => setConfirmId(todo.id)}
                    >
                      Delete
                    </Button>
                  </li>
                ))}
              </ul>
            )}
          </CardContent>
        </Card>

        {/* Confirm delete */}
        <AlertDialog
          open={confirmId !== null}
          title="Delete todo?"
          description="This action cannot be undone."
          onClose={() => setConfirmId(null)}
          onConfirm={async () => {
            if (confirmId !== null) {
              await deleteTodo(confirmId);
              setConfirmId(null);
            }
          }}
        />
      </div>
    </main>
  );
}
