const taskManager = new TaskManager();
console.log(taskManager.tasks);

const form = document.querySelector('#taskForm');

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

    Swal.close();
});

document.querySelectorAll('.btn-toggle-complete').forEach(function (btn) {
    btn.addEventListener('click', function () {
        const card = this.closest('.card');
        const title = card.querySelector('h5');
        const completed = title.classList.toggle('text-decoration-line-through');
        card.classList.toggle('border-success');
        this.textContent = completed ? 'Marcar pendiente' : 'Completar';
    });
});
