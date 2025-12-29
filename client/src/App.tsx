import { FormEvent, useEffect, useState } from 'react'
import './App.css'
import { useAuthStore } from './store/auth'
import { useTodoStore } from './store/todos'

function App() {
  const { user, token, status: authStatus, error: authError, register, login, logout } = useAuthStore()
  const { todos, status: todoStatus, error: todoError, fetchTodos, createTodo, updateTodo, deleteTodo } =
    useTodoStore()

  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [title, setTitle] = useState('')

  useEffect(() => {
    if (token) {
      fetchTodos().catch(() => {})
    }
  }, [token, fetchTodos])

  const onRegister = async (e: FormEvent) => {
    e.preventDefault()
    await register({ username, password })
  }

  const onLogin = async (e: FormEvent) => {
    e.preventDefault()
    await login({ username, password })
  }

  const onCreateTodo = async (e: FormEvent) => {
    e.preventDefault()
    if (!title.trim()) return
    await createTodo(title.trim())
    setTitle('')
  }

  return (
    <div className="page">
      <header className="header">
        <div>
          <h1 className="title">Todos</h1>
          <p className="muted">Java Servlet backend @ http://localhost:8080</p>
        </div>
        {user ? (
          <div className="pill">Signed in as {user.username}</div>
        ) : (
          <div className="pill">Not signed in</div>
        )}
      </header>

      <section className="card">
        <h2>Auth</h2>
        <form className="form" onSubmit={onLogin}>
          <input
            className="input"
            placeholder="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <input
            className="input"
            placeholder="password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <div className="actions">
            <button className="btn" type="button" onClick={onRegister} disabled={authStatus === 'loading'}>
              Register
            </button>
            <button className="btn" type="submit" disabled={authStatus === 'loading'}>
              Login
            </button>
            <button className="btn ghost" type="button" onClick={logout}>
              Logout
            </button>
          </div>
        </form>
        {authError && <p className="error">{authError}</p>}
      </section>

      <section className="card">
        <h2>Create Todo</h2>
        <form className="form" onSubmit={onCreateTodo}>
          <input
            className="input"
            placeholder="e.g., Sleep"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            disabled={!token}
          />
          <div className="actions">
            <button className="btn" type="submit" disabled={!token || todoStatus === 'loading'}>
              Add
            </button>
          </div>
        </form>
        {!token && <p className="muted">Login to manage todos.</p>}
        {todoError && <p className="error">{todoError}</p>}
      </section>

      <section className="card">
        <div className="list-header">
          <h2>Todos</h2>
          <button className="btn ghost" onClick={() => token && fetchTodos()} disabled={!token || todoStatus === 'loading'}>
            Refresh
          </button>
        </div>
        {todos.length === 0 ? (
          <p className="muted">No todos yet.</p>
        ) : (
          <ul className="todos">
            {todos.map((todo) => (
              <li key={todo.id} className="todo-item">
                <label className="todo-row">
                  <input
                    type="checkbox"
                    checked={todo.completed}
                    onChange={(e) => updateTodo(todo.id, e.target.checked)}
                  />
                  <span className={todo.completed ? 'done' : ''}>{todo.title}</span>
                </label>
                <button className="btn ghost" onClick={() => deleteTodo(todo.id)}>
                  Delete
                </button>
              </li>
            ))}
          </ul>
        )}
      </section>
    </div>
  )
}

export default App
