export type User = {
  id: number
  username: string
}

export type RegisterRequest = {
  username: string
  password: string
}

export type RegisterResponse = {
  id: number
  username: string
}

export type LoginRequest = {
  username: string
  password: string
}

export type LoginResponse = {
  id: number
  username: string
  token: string
  message: string
}

export type Todo = {
  id: number
  title: string
  completed: boolean
  userId: number
}

export type CreateTodoRequest = {
  title: string
}

export type UpdateTodoRequest = {
  completed: boolean
}

export type SuccessResponse = {
  success: true
}

export type ApiError = {
  status: number
  message: string
}
