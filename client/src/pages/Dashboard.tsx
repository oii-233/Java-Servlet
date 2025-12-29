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
    <div className="mx-auto max-w-2xl space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-semibold text-neutral-900">Todo List</h1>
          <p className="text-sm text-neutral-700">Java Servlet backend</p>
        </div>
        <div className="flex items-center gap-2">
          <Badge>{user?.username}</Badge>
          <Button
            variant="ghost"
            onClick={() => {
              logout();
              navigate("/");
            }}
          >
            Logout
          </Button>
        </div>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Add Todo</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="flex items-center gap-2">
            <Input
              placeholder="e.g., Sleep"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />
            <Button
              onClick={async () => {
                if (!title.trim()) return;
                await createTodo(title.trim());
                setTitle("");
              }}
              disabled={status === "loading"}
            >
              Add
            </Button>
          </div>
          {error && <p className="mt-2 text-sm text-red-700">{error}</p>}
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Todos</CardTitle>
        </CardHeader>
        <CardContent>
          {status === "loading" ? (
            <div className="space-y-2">
              <Skeleton className="h-9 w-full" />
              <Skeleton className="h-9 w-full" />
            </div>
          ) : todos.length === 0 ? (
            <p className="text-sm text-neutral-700">No todos yet.</p>
          ) : (
            <div className="space-y-2">
              {todos.map((todo) => (
                <div
                  key={todo.id}
                  className="flex items-center justify-between rounded-md border border-neutral-200 p-2"
                >
                  <label className="flex items-center gap-2">
                    <Checkbox
                      checked={todo.completed}
                      onChange={async (e) => {
                        await updateTodo(
                          todo.id,
                          (e.target as HTMLInputElement).checked
                        );
                      }}
                    />
                    <span
                      className={
                        todo.completed
                          ? "text-neutral-500 line-through"
                          : "text-neutral-900"
                      }
                    >
                      {todo.title}
                    </span>
                  </label>
                  <div className="flex items-center gap-2">
                    <Button
                      variant="ghost"
                      onClick={() => setConfirmId(todo.id)}
                    >
                      Delete
                    </Button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </CardContent>
      </Card>

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
  );
}
