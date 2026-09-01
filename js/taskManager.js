class TaskManager {
    constructor(currentId = 0) {
        this.tasks = [];
        this.currentId = currentId;
        this.load();
    }

    addTask(name, description, dueDate, status) {
        this.currentId += 1;
        this.tasks.push({
            id: this.currentId,
            name: name,
            description: description,
            dueDate: dueDate,
            status: 'PORHACER'
        });
    }

    deleteTask(taskId) {
        const newTasks = [];
        for (let task of this.tasks) {
            if (task.id !== taskId) {
                newTasks.push(task);
            }
        }
        this.tasks = newTasks;
    }

    save() {
        localStorage.setItem('tasks', JSON.stringify(this.tasks));
        localStorage.setItem('currentId', String(this.currentId));
    }

    load() {
        const tasks = localStorage.getItem('tasks');
        const currentId = localStorage.getItem('currentId');
        if (tasks) this.tasks = JSON.parse(tasks);
        if (currentId) this.currentId = Number(currentId);
    }

    createTaskHtml(task) {
        return `
            <div class="col">
                <div class="card shadow-sm h-100" data-task-id="${task.id}">
                    <div class="card-header d-flex justify-content-between align-items-center bg-white py-3">
                        <h5 class="mb-0 fw-semibold">${task.name}</h5>
                        <span class="badge rounded-pill bg-secondary">${task.status}</span>
                    </div>
                    <div class="card-body py-3">
                        <p class="card-text text-muted mb-0">${task.description}</p>
                    </div>
                    <div class="card-footer bg-white text-muted small py-2 d-flex justify-content-between align-items-center">
                        Fecha: ${task.dueDate}
                        <div class="d-flex gap-2">
                            <button type="button" class="btn btn-sm btn-outline-success btn-toggle-complete">Completar</button>
                            <button type="button" class="delete-button btn btn-danger btn-sm">Eliminar</button>
                        </div>
                    </div>
                </div>
            </div>
        `;
    }

    render() {
        document.querySelector('#taskList').innerHTML = this.tasks.map((task) => this.createTaskHtml(task)).join('');
    }
}
