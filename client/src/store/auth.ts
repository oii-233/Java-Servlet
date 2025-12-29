import { create } from 'zustand'
import { api } from '../api/client'
import type  { LoginRequest, RegisterRequest, User } from '../api/types'

type AuthState = {
  user: User | null
  token: string | null
  status: 'idle' | 'loading'
  error: string | null
  register: (body: RegisterRequest) => Promise<void>
  login: (body: LoginRequest) => Promise<void>
  logout: () => void
}

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  token: null,
  status: 'idle',
  error: null,
  register: async (body) => {
    set({ status: 'loading', error: null })
    try {
      const res = await api.register(body)
      set({ user: { id: res.id, username: res.username }, status: 'idle' })
    } catch (err) {
      const message = (err as { message?: string }).message ?? 'Registration failed'
      set({ error: message, status: 'idle' })
      throw err
    }
  },
  login: async (body) => {
    set({ status: 'loading', error: null })
    try {
      const res = await api.login(body)
      set({
        user: { id: res.id, username: res.username },
        token: res.token,
        status: 'idle',
      })
    } catch (err) {
      const message = (err as { message?: string }).message ?? 'Login failed'
      set({ error: message, status: 'idle' })
      throw err
    }
  },
  logout: () => set({ user: null, token: null, error: null, status: 'idle' }),
}))
