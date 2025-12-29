package com.example.todo.service;

import com.example.todo.model.Todo;
import com.example.todo.repository.TodoRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * TodoService contains business rules for todos but no SQL.
 */
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService() {
        this.todoRepository = new TodoRepository();
    }

    public Todo createTodo(String title, int userId) throws SQLException {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        return todoRepository.createTodo(title.trim(), userId);
    }

    public List<Todo> listTodos(int userId) throws SQLException {
        return todoRepository.getTodosByUser(userId);
    }

    public boolean setCompleted(int todoId, boolean completed, int userId) throws SQLException {
        return todoRepository.updateTodoCompletion(todoId, completed, userId);
    }

    public boolean delete(int todoId, int userId) throws SQLException {
        return todoRepository.deleteTodo(todoId, userId);
    }
}
