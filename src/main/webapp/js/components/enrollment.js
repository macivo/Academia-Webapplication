import router from "../router.js";
import model from '../model.js';
import data from '../data.js';


let enrolledModuleRuns = null;
let allModuleRunsForEnrollment = null;

/**
 *  Public interface
 *  */
export default {

    /**
     * Get title for template
     * @returns String with title
     */
    getTitle: function() {
        return "List of running modules to enroll";
    },

    /**
     * Render element for template tpl-enrollment-view
     * @returns rendered component
     */
    render: function() {
        const $comp = $($('#tpl-enrollment-view').html());
        const userId = data.getUserId();
        model.getAllModuleRunsForEnrollment()
            .then(getAllModuleRunsForEnrollmentData => {
                allModuleRunsForEnrollment = getAllModuleRunsForEnrollmentData;
                model.getEnrolledModuleRunForStudent(userId)
                    .then(enrolledModulesData => {
                        enrolledModuleRuns = enrolledModulesData;
                        renderEnrolledModulesList($comp, enrolledModuleRuns)
                    })
            })
            .catch(jqXHR => {
                $comp.append('<p class="error">Loading list of your modules failed!</p>');
                return $comp;
            });
        return $comp;
    },

};

/**
 * Render the list for enrollments
 * @param $comp -  this template
 * @param enrolledModuleRuns - the enrolled moduleruns
 */
function renderEnrolledModulesList($comp, enrolledModuleRuns) {

    const elementsRunning = document.createElement('div');
    const elementsNotRunning = document.createElement('div');
    elementsRunning.setAttribute('class', 'content-div');
    elementsNotRunning.setAttribute('class', 'content-div');
    const title1 = document.createElement('h2');
    title1.innerHTML = 'You are enrolled to these modules';
    const title2 = document.createElement('h2');
    title2.innerHTML = 'You can enroll to these modules';

    enrolledModuleRuns.forEach(module => {
            const details = document.createElement('details');
            const summary = document.createElement('summary');
            const ul = document.createElement('ul');
            summary.innerText = module.module_number + " " + module.name;
            ul.innerHTML += `<li>Semester: ${module.semester}</li>`;
            ul.innerHTML += `<li>Year: ${module.year}</li>`;
            ul.innerHTML += `<li>Ects: ${module.ects}</li>`;
            ul.innerHTML += `<li>Module id: ${module.id}</li>`;
            ul.innerHTML += `<li>Module run id: ${module.module_run_id}</li>`;
            ul.innerHTML += `<li>Module running: ${module.running}</li>`;
            details.append(summary);
            details.append(ul);
            elementsRunning.append(details);
        })

    allModuleRunsForEnrollment.forEach(module => {
        let isEnrolled = isThisModuleEnrolled(module);

        if (isEnrolled == false) {
            const details = document.createElement('details');
            const summary = document.createElement('summary');
            //enroll-button
            const enrollButton = renderButton("enroll to module");
            enrollButton.addEventListener('click', function() {
                enrollStudent(data.getStudentId(),module.modulerunId);
            });
            const ul = document.createElement('ul');
            summary.innerText = module.moduleNumber + " " + module.name;
            ul.innerHTML += `<li>Semester: ${module.semester}</li>`;
            ul.innerHTML += `<li>Year: ${module.year}</li>`;
            ul.innerHTML += `<li>Ects: ${module.ects}</li>`;
            ul.innerHTML += `<li>Module id: ${module.moduleId}</li>`;
            ul.innerHTML += `<li>Module run id: ${module.modulerunId}</li>`;
            ul.innerHTML += `<li>Module running: ${module.running}</li>`;

            ul.append(enrollButton);
            details.append(summary);
            details.append(ul);
            elementsNotRunning.append(details);
        }
    })
    $comp.append(title1);
    $comp.append(elementsRunning);
    $comp.append(title2);
    $comp.append(elementsNotRunning);
}

/**
 * Render the button
 * @param text - for the button
 * @returns {HTMLButtonElement}
 */
function renderButton(text) {
    const button = document.createElement('button');
    button.innerHTML = text;
    return button;
}

/**
 * Check if student is enrolled to this modulerun
 * @param module -  the modulerun
 * @returns true if student is enrolled otherwise false
 */
function isThisModuleEnrolled(module) {
    let returnValue = false;
    enrolledModuleRuns.forEach(enrolledModule => {
        if (enrolledModule.module_run_id === module.modulerunId){
            returnValue = true;
        }

    })
    return returnValue;
}

/**
 * Enroll student to a modulerun
 * @param studentId - the student id
 * @param moduleRunId - the modulerun id
 */
function enrollStudent(studentId, moduleRunId){
    let enrollment = model.createEnrollment(studentId, moduleRunId);
    model.enrollStudent(enrollment)
        .then(() => router.go('/enrollment', moduleRunId))
        .catch(jqXHR => {
            let message = jqXHR.status === 409 ? 'Student already exists' : 'Unexpected error(' + jqXHR.status + ')';
            $('#message').text(message);
        });
}

