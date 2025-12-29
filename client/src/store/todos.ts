import { create } from 'zustand'
import { api } from '../api/client'
import { Todo } from '../api/types'
import { useAuthStore } from './auth'

type TodoState = {
  todos: Todo[]
  status: 'idle' | 'loading'
  error: string | null
  fetchTodos: () => Promise<void>
  createTodo: (title: string) => Promise<void>
  updateTodo: (id: number, completed: boolean) => Promise<void>
  deleteTodo: (id: number) => Promise<void>
}

export const useTodoStore = create<TodoState>((set, get) => ({
  todos: [],
  status: 'idle',
  error: null,
  fetchTodos: async () => {
    const token = useAuthStore.getState().token
    if (!token) {
      set({ error: 'Not authenticated' })
      return
    }
    set({ status: 'loading', error: null })
    try {
      const todos = await api.listTodos(token)
      set({ todos, status: 'idle' })
    } catch (err) {
      const message = (err as { message?: string }).message ?? 'Failed to load todos'
      set({ error: message, status: 'idle' })
      throw err
    }
  },
  createTodo: async (title) => {
    const token = useAuthStore.getState().token
    if (!token) {
      set({ error: 'Not authenticated' })
      return
    }
    set({ status: 'loading', error: null })
    try {
      const todo = await api.createTodo({ title }, token)
      set({ todos: [...get().todos, todo], status: 'idle' })
    } catch (err) {
      const message = (err as { message?: string }).message ?? 'Failed to create todo'
      set({ error: message, status: 'idle' })
      throw err
    }
  },
  updateTodo: async (id, completed) => {
    const token = useAuthStore.getState().token
    if (!token) {
      set({ error: 'Not authenticated' })
      return
    }
    set({ status: 'loading', error: null })
    try {
      await api.updateTodo(id, { completed }, token)
      set({
        todos: get().todos.map((t) => (t.id === id ? { ...t, completed } : t)),
        status: 'idle',
      })
    } catch (err) {
      const message = (err as { message?: string }).message ?? 'Failed to update todo'
      set({ error: message, status: 'idle' })
      throw err
    }
  },
  deleteTodo: async (id) => {
    const token = useAuthStore.getState().token
    if (!token) {
      set({ error: 'Not authenticated' })
      return
    }
    set({ status: 'loading', error: null })
    try {
      await api.deleteTodo(id, token)
      set({ todos: get().todos.filter((t) => t.id !== id), status: 'idle' })
    } catch (err) {
      const message = (err as { message?: string }).message ?? 'Failed to delete todo'
      set({ error: message, status: 'idle' })
      throw err
    }
  },
}))
