import type {
  ApiError,
  CreateTodoRequest,
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  RegisterResponse,
  SuccessResponse,
  Todo,
  UpdateTodoRequest,
} from './types'

const API_BASE = (import.meta.env.VITE_API_BASE as string | undefined) ?? 'http://localhost:8080'

type HttpMethod = 'GET' | 'POST' | 'PUT' | 'DELETE'

async function request<T>(
  path: string,
  options: {
    method?: HttpMethod
    body?: unknown
    token?: string | null
  } = {},
): Promise<T> {
  const headers = new Headers()
  headers.set('Accept', 'application/json')
  if (options.body !== undefined) {
    headers.set('Content-Type', 'application/json')
  }
  if (options.token) {
    headers.set('Authorization', `Bearer ${options.token}`)
  }

  const resp = await fetch(`${API_BASE}${path}`, {
    method: options.method ?? 'GET',
    headers,
    body: options.body !== undefined ? JSON.stringify(options.body) : undefined,
  })

  if (!resp.ok) {
    const message = await safeErrorMessage(resp)
    const err: ApiError = { status: resp.status, message }
    throw err
  }

  if (resp.status === 204) {
    return undefined as T
  }

  const text = await resp.text()
  if (!text) return undefined as T
  return JSON.parse(text) as T
}

async function safeErrorMessage(resp: Response): Promise<string> {
  try {
    const text = await resp.text()
    if (!text) return resp.statusText
    const json = JSON.parse(text) as { error?: string; message?: string }
    return json.error ?? json.message ?? resp.statusText
  } catch {
    return resp.statusText
  }
}

export const api = {
  register: (body: RegisterRequest) =>
    request<RegisterResponse>('/api/auth/register', { method: 'POST', body }),
  login: (body: LoginRequest) =>
    request<LoginResponse>('/api/auth/login', { method: 'POST', body }),
  createTodo: (body: CreateTodoRequest, token: string | null) =>
    request<Todo>('/api/todos', { method: 'POST', body, token }),
  listTodos: (token: string | null) =>
    request<Todo[]>('/api/todos', { method: 'GET', token }),
  updateTodo: (id: number, body: UpdateTodoRequest, token: string | null) =>
    request<SuccessResponse>(`/api/todos/${id}`, { method: 'PUT', body, token }),
  deleteTodo: (id: number, token: string | null) =>
    request<void>(`/api/todos/${id}`, { method: 'DELETE', token }),
}
