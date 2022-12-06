import service from "../service.js";
import model from "../model.js";
import router from "../router.js";

/**
 * Public interface for getting title and rendering student view
 */
export default {

    /**
     * Get title for template
     * @returns String with title
     */
    getTitle: function() {
        return "Edit Student";
    },

    /**
     * Render element for template tpl-student-view
     * @param student-id
     * @returns rendered component
     */
    render: function(student_id) {
        const $comp = $($('#tpl-student-view').html());
        service.getStudent(student_id)
            .then(student => renderStudentEdit($comp, student))
            .catch(() => {
                $comp.append('<p class="error">Editing failed!</p>');
                return $comp;
            });
        return $comp;
    }
};

/**
 * Component for editing a students credentials
 */
/* Shows dataset of one student as html table with all editable attributes*/
let elements;
let passwordCheck;

/**
 * Renders the edit-form
 * @param $comp rendered view
 * @param student
 */
function renderStudentEdit($comp, student) {
    //all attributes for this student
    const student_id = student.student_id;
    const gender = student.gender;
    const firstName = student.firstname;
    const lastName = student.lastname;
    const matriculationNumber = student.matriculation_number;
    const dateOfBirth = student.date_of_birth;
    const username = student.username;
    const password = student.password;
    const email = student.email;
    const role = student.role;
    const status = student.status;
    passwordCheck = student.password;

    elements = document.createElement('div');
    elements.setAttribute('class', 'content-div');
    const title = document.createElement('h2');
    title.innerHTML = 'Edit Student';

    //save button to save new attributes
    const saveButton = renderSaveButton();

    //back button to get back to students overview
    const backButton = renderBackButton();

    const table = document.createElement('table');
    const row_ID = document.createElement('tr');
    const fixCellStudId =  document.createElement('td');
    fixCellStudId.innerHTML = "Student ID: ";
    const valCellStudId = document.createElement('td');
    valCellStudId.innerHTML = student_id;
    valCellStudId.setAttribute('id', "row_ID");
    row_ID.append(fixCellStudId, valCellStudId);
    const rowGender = renderEditField("Gender: ", "rowGender", gender);
    const rowFirstName = renderEditField("First Name: ", "rowFirstName", firstName);
    const rowLastName = renderEditField("Last Name: ", "rowLastName", lastName);
    const rowMatrNo = renderEditField("Matriculation-Number: ", "rowMatrNo", matriculationNumber);
    const rowBirthDate = renderEditField("Date of birth: ", "rowBirthDate", dateOfBirth);
    const rowUsername = renderEditField("Username: ", "rowUsername", username);
    const rowPassword = renderEditField("Password: ", "rowPassword", password);
    const rowMail = renderEditField("E-Mail: ", "rowMail", email);
    const rowRole = renderEditField("Role: ", "rowRole", role);
    const rowStatus = renderEditField("Status:", "rowStatus", status);
    table.append(row_ID, rowGender, rowFirstName, rowLastName, rowMatrNo, rowBirthDate, rowUsername, rowPassword, rowMail, rowRole, rowStatus);
    elements.append(table);
    elements.append(backButton);
    elements.append(saveButton);

    $comp.append(title);
    $comp.append(elements);


}

/**
 * Render a cancel-button to get back to students overview
 * @returns Backbutton
 */
function renderBackButton() {
    const backButton = document.createElement('button');
    backButton.innerHTML = 'cancel';
    backButton.setAttribute('id', 'backButton');
    backButton.setAttribute('class', 'secondary');
    backButton.addEventListener('click', function () {
        router.go("/students");
    });
    return backButton;
}

/**
 * Render a Button to save the newly entered credentials with an event listener
 * @returns Savebutton
 */
function renderSaveButton() {
    const saveButton = document.createElement('button');
    saveButton.innerHTML = 'save';
    saveButton.setAttribute('id', 'save_Button');
    saveButton.addEventListener('click', saveButtonListener);
    return saveButton;
}

/**
 * Add an event listener to save-button
 */

function saveButtonListener() {
    const student_id = document.getElementById("row_ID").innerText;

    // Get all elements, save in local variables
    const gender_new = document.getElementById("rowGender").value;
    document.getElementById("rowGender").setAttribute('disabled', 'true');
    const firstName_new = document.getElementById("rowFirstName").value;
    document.getElementById("rowFirstName").setAttribute('disabled', 'true');
    const lastName_new = document.getElementById("rowLastName").value;
    document.getElementById("rowLastName").setAttribute('disabled', 'true');
    const matriculationNumber_new = document.getElementById("rowMatrNo").value;
    document.getElementById("rowMatrNo").setAttribute('disabled', 'true');
    const dateOfBirth_new = document.getElementById("rowBirthDate").value;
    document.getElementById("rowBirthDate").setAttribute('disabled', 'true');
    const username_new = document.getElementById("rowUsername").value;
    document.getElementById("rowUsername").setAttribute('disabled', 'true');
    const password_new = document.getElementById("rowPassword").value;
    document.getElementById("rowPassword").setAttribute('disabled', 'true');
    const email_new = document.getElementById("rowMail").value;
    document.getElementById("rowMail").setAttribute('disabled', 'true');
    const role_new = document.getElementById("rowRole").value;
    document.getElementById("rowRole").setAttribute('disabled', 'true');
    const status_new = document.getElementById("rowStatus").value;
    document.getElementById("rowStatus").setAttribute('disabled', 'true');

    //Remove old buttons
    const removableButton = document.getElementById('save_Button');
    const backButton = document.getElementById('backButton');
    removableButton.remove();
    backButton.remove();

    //check if a new password was inserted. A new password must be encrypted with md5
    passwordCheck = (passwordCheck === password_new)? passwordCheck : md5(password_new);

    //send new data through model & service to backend put-method
    const studentUpdated = model.createStudentWithParam(student_id, gender_new, firstName_new, lastName_new, dateOfBirth_new, email_new, username_new, passwordCheck, matriculationNumber_new, role_new, status_new);

    model.updateStudent(studentUpdated, student_id)
        .then(() => elements.append(' Changes saved successfully !'))
        .catch(jqXHR => {
            if(jqXHR.status === 406){
                elements.append(' Special characters are not allowed [<>$()=;/]');
            }
            else{
                elements.append(' Unexpected error(' + jqXHR.status + ')');
            }
        });

    //render Success-text and back-button
    const newBackButton = renderBackButton();
    newBackButton.innerHTML = "back";
    elements.append(`\n \n`);
    elements.append(newBackButton);

}

/**
 * Render an edit field
 * @param name of the edit field
 * @param rowId id for edit field
 * @param rowValue the input value already set for this row
 * @returns rendered row
 */
function renderEditField(name, rowId, rowValue) {
    //create whole row
    const row = document.createElement('tr')

    //Show a fix title
    const fixCell = document.createElement('td');
    fixCell.innerHTML = name;

    //Show editable cell with current value
    const editCell = document.createElement('td');
    let editForm;

    //Checks, which rowId and creates the corresponding selector
    if(rowId === "rowGender") {
        editForm = createGenderSelector(rowValue);
    } else if (rowId === "rowBirthDate") {
        editForm = createDateSelector(rowValue);
    } else if (rowId === "rowMatrNo") {
        editForm = createNumberInput(rowValue);
    } else if (rowId === "rowUsername") {
        editForm = createUsernameInput(rowValue);
    } else if (rowId === "rowPassword") {
        editForm = createPasswordInput(rowValue);
    } else if (rowId === "rowFirstName" || rowId === "rowLastName") {
        editForm = createNameInput(rowValue);
    } else if (rowId === "rowMail") {
        editForm = createMailInput(rowValue);
    } else if (rowId === "rowRole") {
        editForm = createRoleSelector(rowValue);
    } else if (rowId === "rowStatus") {
        editForm = createStatusSelector(rowValue);
    }else {
        editForm = document.createElement('input');
        editForm.setAttribute('value', rowValue);
    }
    editForm.setAttribute('id', rowId);
    editCell.appendChild(editForm);

    row.append(fixCell, editCell);
    return row;
}

/**
 * Create a Selector for gender and read the selected value
 * @param rowValue the input value already set for this row
 * @returns select-element for gender
 */
function createGenderSelector(rowValue) {
    const editForm = document.createElement('select');
    const optGroup = document.createElement('optgroup');
    const option1 = document.createElement('option');
    const option2 = document.createElement('option');
    const option3 = document.createElement('option');
    option1.setAttribute('value', 'female');
    option2.setAttribute('value', 'male');
    option3.setAttribute('value', 'divers');
    option1.innerHTML = "female";
    option2.innerHTML = "male";
    option3.innerHTML = "divers";
    optGroup.append(option1, option2, option3);
    if (rowValue === 'female'){
        option1.selected = true;
    } else if (rowValue === 'male'){
        option2.selected = true;
    } else {
        option3.selected = true;
    }
    editForm.setAttribute('value', rowValue);
    editForm.appendChild(optGroup);
    return editForm;
}

/**
 * Create a selector of type date
 * @param rowValue the input value already set for this row
 * @returns input-element for a date
 */
function createDateSelector(rowValue) {
    const editForm = document.createElement('input');
    editForm.setAttribute('type', 'date');
    editForm.setAttribute('value', rowValue);
    return editForm;
}

/**
 * Create an input field of type number
 * @param rowValue the input value already set for this row
 * @returns input-element for a number
 */
function createNumberInput(rowValue) {
    const editForm = document.createElement('input');
    editForm.setAttribute('type', 'text');
    editForm.setAttribute('value', rowValue);
    return editForm;
}

/**
 * Create an input field of type username
 * @param rowValue the input value already set for this row
 * @returns input-element for a username
 * */
function createUsernameInput(rowValue) {
    const editForm = document.createElement('input');
    editForm.setAttribute('type', 'text');
    editForm.setAttribute('value', rowValue);
    editForm.setAttribute('max', '5');
    return editForm;
}

/**
 * Create an inputfield of type password
 * @param rowValue the input value already set for this row
 * @returns input-element for a password
 */
function createPasswordInput(rowValue) {
    const editForm = document.createElement('input');
    editForm.setAttribute('type', 'password');
    editForm.setAttribute('value', rowValue);
    editForm.setAttribute('max', '30');
    return editForm;
}

/**
 * Create an inputfield for a name
 * @param rowValue the input value already set for this row
 * @returns input-element for a name
 */

function createNameInput(rowValue) {
    const editForm = document.createElement('input');
    editForm.setAttribute('type', 'text');
    editForm.setAttribute('value', rowValue);
    editForm.setAttribute('max', '30');
    return editForm;
}

/**
 * Create an inputfield for mail input
 * @param rowValue the input value already set for this row
 * @returns input-element for a mailaddress
 */
function createMailInput(rowValue) {
    const editForm = document.createElement('input');
    editForm.setAttribute('type', 'email');
    editForm.setAttribute('value', rowValue);
    editForm.setAttribute('max', '30');
    return editForm;
}

/**
 * Create a selector of type status
 * @param rowValue the input value already set for this row
 * @returns selector for status
 */
function createStatusSelector(rowValue) {
    const editForm = document.createElement('select');
    const optGroup = document.createElement('optgroup');
    const option1 = document.createElement('option');
    const option2 = document.createElement('option');
    option1.setAttribute('value', 'true');
    option2.setAttribute('value', 'false');
    option1.innerHTML = "active";
    option2.innerHTML = "inactive";
    optGroup.append(option1, option2);
    editForm.setAttribute('value', rowValue);
    editForm.appendChild(optGroup);
    return editForm;
}

/**
 * Create a selector of type role. not implemented yet.
 * @param rowValue the input value already set for this row
 * @returns selector for role
 */
function createRoleSelector(rowValue) {
    const editForm = document.createElement('select');
    const optGroup = document.createElement('optgroup');
    const option1 = document.createElement('option');
    const option2 = document.createElement('option');
    const option3 = document.createElement('option');
    option1.setAttribute('value', 'student');
    option2.setAttribute('value', 'professor');
    option3.setAttribute('value', 'administrator');
    option1.innerHTML = "student";
    option2.innerHTML = "professor";
    option3.innerHTML = "administrator";
    optGroup.append(option1, option2, option3);
    editForm.setAttribute('value', rowValue);
    editForm.appendChild(optGroup);
    return editForm;
}

