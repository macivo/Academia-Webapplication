import service from "../service.js";
import router from "../router.js";
import util from "../util.js";

/**
 * Generator add students as html table to page
 *
 * @param $comp - template
 * @param students - list of students
 */
function renderStudentsList($comp, students) {

    const elements = document.createElement('div');
    elements.setAttribute('class', 'content-div');
    //button to create a new student
    const createButton = document.createElement('button');
    createButton.innerHTML = 'create new student';
    createButton.addEventListener('click', function() {
        router.go('/studentCreate');
    });

    students.forEach(student => {
        const details = document.createElement('details');
        const summary = document.createElement('summary');

        //edit-button to edit dataset of a student
        const editButton = renderButton("edit");
        editButton.addEventListener('click', function() {
            router.go('/studentEdit', student.student_id);
        });

        //delete-button to delete a student
        const deleteButton = renderButton("delete");
        deleteButton.onclick = function(event) {
            removeStudent(student.student_id);
        }
        const ul = document.createElement('ul');
        summary.innerText = student.firstname + "  " + student.lastname;
        ul.innerHTML = `<li>ID: ${student.student_id}</li>`;
        ul.innerHTML += `<li>Gender: ${student.gender}</li>`;
        ul.innerHTML += `<li>Firstname: ${student.firstname}</li>`;
        ul.innerHTML += `<li>Lastname: ${student.lastname}</li>`;
        ul.innerHTML += `<li>Matriculation No.: ${student.matriculation_number}</li>`;
        ul.innerHTML += `<li>Date of birth: ${student.date_of_birth}</li>`;
        ul.innerHTML += `<li>Username: ${student.username}</li>`;
        ul.innerHTML += `<li>E-Mail: ${student.email}</li>`;
        ul.innerHTML += `<li>Role: ${student.role}</li>`;
        const status = (student.status)? 'active': 'inactive';
        ul.innerHTML += `<li>Status: ${status}</li>`;
        deleteButton.innerText = "delete";
        details.append(summary);
        details.append(ul);
        details.append(deleteButton);
        details.append(editButton);
        elements.append(details);
    });
    $comp.append(elements);
    //show button create new Student
    $comp.append(createButton);
}

/**
 * Render a button
 * @param text
 * @returns {HTMLButtonElement}
 */
function renderButton(text) {
    const button = document.createElement('button');
    button.innerHTML = text;
    return button;
}

/**
 * Delete a student
 *
 * @param student_id
 */
function removeStudent(student_id) {
    let error;
    service.deleteStudent(student_id)
        .catch(jqXHR => {
            if (jqXHR.status === 500){
                error = true;
                util.showOkModal("Error", "This student could not be deleted, because he/she may already enrolled one or more courses.");
            } else {
                error = true;
                util.showOkModal("Error", "Error code is: " + jqXHR.status);
            }
        });
    setTimeout(function(){ if(!error)router.go('/students'); }, 1000);
}


/**
 *  Public interface
 *  */
export default {

    /**
     * Get title for template
     * @returns String with title
     */
    getTitle: function() {
        return "List of Students";
    },

    /**
     * Render element for template tpl-student-view
     * @returns rendered component
     */
    render: function() {
        const $comp = $($('#tpl-student-view').html());
        service.getStudents()
            .then(students => renderStudentsList($comp, students))
            .catch(jqXHR => {
                $comp.append('<p class="error">Loading list of students failed!</p>');
                return $comp;
            });
        return $comp;
    }
};