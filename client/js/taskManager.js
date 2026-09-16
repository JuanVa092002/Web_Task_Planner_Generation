const API_URL = `${window.APP_API_BASE || 'http://localhost:8080'}/api/tasks`;

class TaskManager {
    constructor(currentId = 0) {
        this.tasks = [];
        this.currentId = currentId;
    }

    async addTask(name, description, dueDate, status) {
        const response = await fetch(API_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                name: name,
                description: description,
                dueDate: dueDate,
                status: status
            })
        });

        if (!response.ok) {
            throw new Error('No se pudo crear la tarea');
        }

        const task = await response.json();
        this.tasks.push(task);
        return task;
    }

    async deleteTask(taskId) {
        const response = await fetch(`${API_URL}/${taskId}`, { method: 'DELETE' });
        if (!response.ok && response.status !== 204) {
            throw new Error('No se pudo eliminar la tarea');
        }

        const newTasks = [];
        for (let task of this.tasks) {
            if (task.id !== taskId) {
                newTasks.push(task);
            }
        }
        this.tasks = newTasks;
    }

    async save() {
        return Promise.resolve();
    }

    async load() {
        const response = await fetch(API_URL);
        if (!response.ok) {
            throw new Error('No se pudieron cargar las tareas');
        }
        this.tasks = await response.json();
    }

    getTaskById(taskId) {
        let foundTask;
        for (let task of this.tasks) {
            if (task.id === taskId) {
                foundTask = task;
            }
        }
        return foundTask;
    }

    async updateTask(task) {
        const response = await fetch(`${API_URL}/${task.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(task)
        });

        if (!response.ok) {
            throw new Error('No se pudo actualizar la tarea');
        }

        return response.json();
    }

    createTaskHtml(id, name, description, dueDate, status) {
        const safeName = this.escapeHtml(name);
        const safeDescription = this.escapeHtml(description || 'Sin descripción');
        const statusClass = `status-${status}`;
        const doneClass = status === 'DONE' ? 'is-done' : '';
        const dateText = this.formatDate(dueDate);

        return `
            <article class="task-card ${doneClass}">
                <div class="task-card-top">
                    <h3>${safeName}</h3>
                    <span class="status-pill ${statusClass}">${this.statusLabel(status)}</span>
                </div>
                <p>${safeDescription}</p>
                <div class="task-card-actions" data-task-id="${id}">
                    <span class="task-date">${dateText}</span>
                    <button class="done-button btn btn-success">Mark As Done</button>
                    <button type="button" class="delete-button btn btn-danger btn-sm">Eliminar</button>
                </div>
            </article>
        `;
    }

    escapeHtml(value) {
        return String(value)
            .replaceAll('&', '&amp;')
            .replaceAll('<', '&lt;')
            .replaceAll('>', '&gt;')
            .replaceAll('"', '&quot;');
    }

    formatDate(dueDate) {
        if (!dueDate) return 'Sin fecha';
        const date = new Date(`${dueDate}T00:00:00`);
        return date.toLocaleDateString('es-CO', {
            day: '2-digit',
            month: 'short',
            year: 'numeric'
        });
    }

    statusLabel(status) {
        if (status === 'IN_PROGRESS') return 'En progreso';
        if (status === 'DONE') return 'Hecho';
        return 'Pendiente';
    }

    render() {
        const taskList = document.querySelector('#taskList');
        const count = document.querySelector('#taskCount');
        if (count) {
            count.textContent = `${this.tasks.length} ${this.tasks.length === 1 ? 'tarea' : 'tareas'}`;
        }

        if (this.tasks.length === 0) {
            taskList.innerHTML = `
                <div class="empty-state">
                    <strong>Nada por aquí todavía</strong>
                    <p class="mb-0 mt-2">Crea la primera tarea desde el formulario.</p>
                </div>
            `;
            return;
        }

        taskList.innerHTML = this.tasks.map((task) => this.createTaskHtml(task.id, task.name, task.description, task.dueDate, task.status)).join('');
    }
}
