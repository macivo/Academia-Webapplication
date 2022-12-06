import model from "../model.js";
import moduleRunData from "./modulesTeacher.js";
import service from "../service.js";
import util from "../util.js";

let studentsForThisModule = null;
let item;
let enrollmentsByStudentId = [];

/**
 * Get enrollment from service
 *
 * @param moduleId id from module
 */
function setEnrollment(moduleId) {
    service.getEnrollmentByModuleRunId(moduleId)
        .then(enrollments => {
            enrollments.forEach(enrollment => {
                //store by student_id
                enrollmentsByStudentId[enrollment.student_id] = enrollment;
            })
            return true;
        })
        .catch(jqXHR => {
            util.showOkModal("Error", "could not get enrollments of module id = " + moduleId);
        });
}

/**
 * Create a HTML Selections for grade-managing
 *
 * @param studentId need an id of the student
 * @returns {HTMLSelectElement} then add to running module
 */
function renderGradeManage(studentId) {
    const grade = ['*', 'A', 'B', 'C', 'D', 'E', 'F']
    const gradeOption = document.createElement('select');
    const optGroup = document.createElement('optgroup');
    grade.forEach(grade => {
        const option = document.createElement('option');
        option.setAttribute('value', grade);
        option.innerHTML = grade;
        if (enrollmentsByStudentId[studentId].grade === grade) option.selected = true;
        optGroup.append(option);
    })
    gradeOption.setAttribute('value', enrollmentsByStudentId[studentId]);
    gradeOption.appendChild(optGroup);
    gradeOption.addEventListener('change', function () {
        enrollmentsByStudentId[studentId].grade = gradeOption.value;
        service.setStudentGrade(enrollmentsByStudentId[studentId])
            .then(() => {
                util.showFastNotify("DONE !!!", "Grade was updated.");
            })
            .catch(jqXHR => {
                util.showOkModal("Error", "Set grades failed!!")
            })
    });
    return gradeOption;
}

/**
 * Getting student for a module
 *
 * @param $comp
 * @param studentsForThisModule
 */
function renderStudentsForThisModuleList($comp, studentsForThisModule) {

    let name = moduleRunData.getModuleRun().find(element => (element.id == item));
    const students = document.createElement('div');
    const title = document.createElement('h2');
    title.innerHTML = `Enrolled students`;
    let courseName = document.createElement('h3');
    courseName.classList.add("myModuleHeadline")
    courseName.innerHTML = ` ${name.moduleNumber} ${name.moduleName}: ${name.semester} ${name.year}`;
    $comp.append(title);
    $comp.append(courseName);

    studentsForThisModule.forEach(student => {
        const one = document.createElement('div');
        one.classList.add("one");
        const left = document.createElement('div');
        left.classList.add("left");
        let photo = document.createElement('img');
        photo.classList.add("photo");
        if (student.gender === 'female') {
            photo.src = '../../img/woman.png';
        }
        if (student.gender === 'male') {
            photo.src = '../../img/man.png';
        }
        if (student.gender === 'divers') {
            photo.src = '../../img/divers.png';
        }
        left.append(photo);
        const middle = document.createElement('div');
        middle.classList.add("middle");

        const studentName = document.createElement('h4');
        studentName.classList.add("studentName");
        studentName.innerHTML = student.firstname + " " + student.lastname;

        const infos = document.createElement('p');
        infos.classList.add("infos");
        infos.innerHTML = `<b>Matriculation No.:</b> ${student.matriculation_number} <br/>`;
        infos.innerHTML += `<b>Date of birth: </b>${student.date_of_birth} <br/>`;
        infos.innerHTML += `<b>Email:</b> ${student.email}<br/>`;

        middle.append(studentName);
        middle.append(infos);

        const right = document.createElement('div');
        right.classList.add("right");

        const gradeText = document.createElement('h4');
        gradeText.classList.add("grade");
        gradeText.innerHTML = "Grade: ";
        const gradeOption = renderGradeManage(student.student_id);
        right.append(gradeText, gradeOption);

        one.append(left);
        one.append(middle);
        one.append(right);
        $comp.append(one);
    });
}

/**
 *  Public interface
 *  */
export default {

    /**
     * Get title for template
     * @returns String with title
     */
    getTitle: function () {
        return "Students in running module";
    },

    /**
     * Render element for template tpl-studentInModule-view
     * @returns rendered component
     */
    render: function (moduleId) {
        setEnrollment(moduleId);
        const $comp = $($('#tpl-studentInModule-view').html());
        item = moduleId;
        model.getStudentsInRunningModule(moduleId)
            .then(studentsFroThisModuleData => {
                studentsForThisModule = studentsFroThisModuleData;
                renderStudentsForThisModuleList($comp, studentsForThisModule)
            })
            .catch(jqXHR => {
                $comp.append('<p class="error">Loading list of your modules failed!</p>');
                return $comp;
            });
        return $comp;
    }
};


