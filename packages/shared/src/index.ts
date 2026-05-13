export type Todo = {
  id: number;
  title: string;
  completed: boolean;
};

export type CreateTodoInput = {
  title: string;
};

export type UpdateTodoInput = {
  title?: string;
  completed?: boolean;
};

export class ApiError extends Error {
  constructor(
    message: string,
    readonly status: number,
  ) {
    super(message);
    this.name = "ApiError";
  }
}

type ClientOptions = {
  baseUrl?: string;
};

export function createTodoClient(options: ClientOptions = {}) {
  const baseUrl = options.baseUrl ?? "/api";

  async function request<T>(path: string, init?: RequestInit): Promise<T> {
    const response = await fetch(`${baseUrl}${path}`, {
      ...init,
      headers: {
        "Content-Type": "application/json",
        ...init?.headers,
      },
    });

    if (!response.ok) {
      const body = await response.json().catch(() => undefined);
      throw new ApiError(body?.message ?? "Request failed", response.status);
    }

    if (response.status === 204) {
      return undefined as T;
    }

    return response.json() as Promise<T>;
  }

  return {
    listTodos: () => request<Todo[]>("/todos"),
    createTodo: (input: CreateTodoInput) =>
      request<Todo>("/todos", {
        method: "POST",
        body: JSON.stringify(input),
      }),
    updateTodo: (id: number, input: UpdateTodoInput) =>
      request<Todo>(`/todos/${id}`, {
        method: "PATCH",
        body: JSON.stringify(input),
      }),
    deleteTodo: (id: number) =>
      request<void>(`/todos/${id}`, {
        method: "DELETE",
      }),
  };
}
