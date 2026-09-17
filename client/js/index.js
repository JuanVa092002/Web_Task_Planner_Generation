function showAlert(icon, title, text) {
    Swal.fire({
        icon,
        title,
        text,
        confirmButtonColor: '#171412'
    });
}

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

window.startAuthenticatedApp = function startAuthenticatedApp(getToken) {
    if (window.__taskAppStarted) {
        return;
    }
    window.__taskAppStarted = true;

    const taskManager = new TaskManager(0, getToken);
    const form = document.querySelector('#taskForm');
    const taskList = document.querySelector('#taskList');

    form.addEventListener('submit', async function (event) {
        event.preventDefault();

        const name = document.querySelector('#newTaskNameInput').value;
        const description = document.querySelector('#newTaskDescriptionInput').value;
        const date = document.querySelector('#newTaskDateInput').value;
        const status = document.querySelector('#newTaskStatusInput').value;

        const data = { name, description, date, status };

        if (!validFormFieldInput(data)) {
            showAlert('error', 'Datos inválidos', 'Por favor completa todos los campos: Nombre, Descripción, Fecha de entrega y Estado.');
            return;
        }

        try {
            await taskManager.addTask(name, description, date, status);
            taskManager.render();
            form.reset();
        } catch (error) {
            showAlert('error', 'Error de conexión', 'No se pudo guardar la tarea. Verifica que el API esté disponible.');
        }
    });

    taskList.addEventListener('click', async (event) => {
        if (event.target.classList.contains('done-button')) {
            const parentTask = event.target.parentElement;
            const taskId = Number(parentTask.dataset.taskId);
            const task = taskManager.getTaskById(taskId);

            task.status = 'DONE';
            try {
                await taskManager.updateTask(task);
                taskManager.render();
            } catch (error) {
                showAlert('error', 'Error de conexión', 'No se pudo actualizar la tarea.');
            }
        }

        if (event.target.classList.contains('delete-button')) {
            const parentTask = event.target.parentElement;
            const taskId = Number(parentTask.dataset.taskId);

            try {
                await taskManager.deleteTask(taskId);
                taskManager.render();
            } catch (error) {
                showAlert('error', 'Error de conexión', 'No se pudo eliminar la tarea.');
            }
        }
    });

    taskManager.load()
        .then(() => {
            taskManager.render();
        })
        .catch(() => {
            showAlert('error', 'Backend no disponible', 'No se pudo conectar con el API. Recarga en unos segundos o verifica el servicio.');
        });
};
