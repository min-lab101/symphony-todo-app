import { FormEvent, useEffect, useMemo, useState } from "react";
import { Check, Loader2, Plus, RefreshCw, Trash2 } from "lucide-react";
import { ApiError, Todo, createTodoClient } from "@symphony-todo/shared";

export function App() {
  const client = useMemo(() => createTodoClient(), []);
  const [todos, setTodos] = useState<Todo[]>([]);
  const [title, setTitle] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function loadTodos() {
    setIsLoading(true);
    setError(null);

    try {
      setTodos(await client.listTodos());
    } catch (caught) {
      setError(getErrorMessage(caught));
    } finally {
      setIsLoading(false);
    }
  }

  useEffect(() => {
    void loadTodos();
  }, []);

  async function handleCreate(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const trimmedTitle = title.trim();
    if (!trimmedTitle) {
      return;
    }

    setIsSaving(true);
    setError(null);

    try {
      const created = await client.createTodo({ title: trimmedTitle });
      setTodos((currentTodos) => [...currentTodos, created]);
      setTitle("");
    } catch (caught) {
      setError(getErrorMessage(caught));
    } finally {
      setIsSaving(false);
    }
  }

  async function toggleTodo(todo: Todo) {
    setError(null);

    try {
      const updated = await client.updateTodo(todo.id, {
        completed: !todo.completed,
      });
      setTodos((currentTodos) =>
        currentTodos.map((currentTodo) =>
          currentTodo.id === updated.id ? updated : currentTodo,
        ),
      );
    } catch (caught) {
      setError(getErrorMessage(caught));
    }
  }

  async function deleteTodo(todo: Todo) {
    setError(null);

    try {
      await client.deleteTodo(todo.id);
      setTodos((currentTodos) =>
        currentTodos.filter((currentTodo) => currentTodo.id !== todo.id),
      );
    } catch (caught) {
      setError(getErrorMessage(caught));
    }
  }

  const completedCount = todos.filter((todo) => todo.completed).length;

  return (
    <main className="app-shell">
      <section className="todo-panel" aria-labelledby="page-title">
        <header className="todo-header">
          <div>
            <p className="eyebrow">Symphony Test App</p>
            <h1 id="page-title">Todo</h1>
          </div>
          <button
            className="icon-button"
            type="button"
            onClick={() => void loadTodos()}
            aria-label="Refresh todos"
            title="Refresh todos"
          >
            {isLoading ? <Loader2 className="spin" /> : <RefreshCw />}
          </button>
        </header>

        <form className="todo-form" onSubmit={(event) => void handleCreate(event)}>
          <input
            value={title}
            onChange={(event) => setTitle(event.target.value)}
            placeholder="Add a todo"
            aria-label="Todo title"
            maxLength={120}
          />
          <button type="submit" disabled={isSaving || !title.trim()}>
            {isSaving ? <Loader2 className="spin" /> : <Plus />}
            <span>Add</span>
          </button>
        </form>

        {error ? <p className="error-message">{error}</p> : null}

        <div className="todo-summary" aria-live="polite">
          <span>{todos.length} total</span>
          <span>{completedCount} complete</span>
        </div>

        <ul className="todo-list">
          {isLoading && todos.length === 0 ? (
            <li className="empty-state">Loading todos...</li>
          ) : todos.length === 0 ? (
            <li className="empty-state">No todos yet.</li>
          ) : (
            todos.map((todo) => (
              <li className="todo-item" key={todo.id}>
                <button
                  className={`check-button ${todo.completed ? "is-complete" : ""}`}
                  type="button"
                  onClick={() => void toggleTodo(todo)}
                  aria-label={todo.completed ? "Mark incomplete" : "Mark complete"}
                  title={todo.completed ? "Mark incomplete" : "Mark complete"}
                >
                  {todo.completed ? <Check /> : null}
                </button>
                <span className={todo.completed ? "completed-title" : ""}>
                  {todo.title}
                </span>
                <button
                  className="icon-button danger"
                  type="button"
                  onClick={() => void deleteTodo(todo)}
                  aria-label="Delete todo"
                  title="Delete todo"
                >
                  <Trash2 />
                </button>
              </li>
            ))
          )}
        </ul>
      </section>
    </main>
  );
}

function getErrorMessage(error: unknown): string {
  if (error instanceof ApiError) {
    return error.message;
  }

  if (error instanceof Error) {
    return error.message;
  }

  return "Something went wrong";
}
