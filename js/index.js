const taskManager = new TaskManager();
taskManager.load();
console.log(taskManager.tasks);

const form = document.querySelector('#taskForm');
const taskList = document.querySelector('#taskList');

function validFormFieldInput(data) {
    const name = data.name.trim();
    const description = data.description.trim();
    const date = data.date.trim();
    const status = data.status.trim();

    if (name === '' || description === '' || date === '' || status === '') {
        return false;
    }

    return true;
}

form.addEventListener('submit', function (event) {
    event.preventDefault();

    const newTaskNameInput = document.querySelector('#newTaskNameInput');
    const newTaskDescriptionInput = document.querySelector('#newTaskDescriptionInput');
    const newTaskDateInput = document.querySelector('#newTaskDateInput');
    const newTaskStatusInput = document.querySelector('#newTaskStatusInput');

    const name = newTaskNameInput.value;
    const description = newTaskDescriptionInput.value;
    const date = newTaskDateInput.value;
    const status = newTaskStatusInput.value;

    console.log({
        name: name,
        description: description,
        date: date,
        status: status
    });

    const data = { name, description, date, status };

    if (!validFormFieldInput(data)) {
        Swal.fire({
            icon: 'error',
            title: 'Datos inválidos',
            text: 'Por favor completa todos los campos: Nombre, Descripción, Fecha de entrega y Estado.'
        });
        return;
    }

    taskManager.addTask(name, description, date, status);
    taskManager.save();
    taskManager.render();
    form.reset();
});

taskList.addEventListener('click', (event) => {
    if (event.target.classList.contains('done-button')) {
        const parentTask = event.target.parentElement;
        const taskId = Number(parentTask.dataset.taskId);
        const task = taskManager.getTaskById(taskId);

        task.status = 'DONE';
        taskManager.save();
        taskManager.render();
    }

    if (event.target.classList.contains('delete-button')) {
        const parentTask = event.target.parentElement;
        const taskId = Number(parentTask.dataset.taskId);

        taskManager.deleteTask(taskId);
        taskManager.save();
        taskManager.render();
    }
});

taskManager.render();
