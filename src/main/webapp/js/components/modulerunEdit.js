import service from "../service.js";
import router from "../router.js";
import model from "../model.js";
import data from "../data.js";


let tempModuleRun = null;

/**
 *  Public interface
 *  */
export default {
    /**
     * Get title for template
     * @returns String with title
     */
    getTitle: function() {
        return "Edit Moduleruns";
    },

    /**
     * Render element for template tpl-modulerun-view
     * @returns rendered component
     */
    render: function(modulerunId) {
        const $comp = $($('#tpl-modulerun-view').html());
        service.getModuleRun(modulerunId)
            .then(modulerun => renderModulrunEdit($comp, modulerun))
            .catch(() => {
                $comp.append('<p class="error">Editing failed!</p>');
                return $comp;
            });
        return $comp;
    }
};

let modrunId;
let modId;
let elements;

/**
 * Render edit view for modulerun
 * @param $comp
 * @param modulerun
 */
function renderModulrunEdit($comp, modulerun){
    modrunId = modulerun.id;
    modId = modulerun.moduleId;
    const modrunSemester = modulerun.semester;
    const modrunYear = modulerun.year;
    const modrunRunning = modulerun.running;
    const moduleName = modulerun.moduleName;
    const moduleNumber = modulerun.moduleNumber;

    elements = document.createElement('div');
    elements.setAttribute('class', 'content-div');
    const title = document.createElement('h2');
    title.innerHTML = 'Module Run Edit';

    //create table to enter changes for module run
    const table = document.createElement('table');

    //First row: Module name
    const moduleRow = document.createElement('tr');
    const fixCellMod =  document.createElement('td');
    fixCellMod.innerHTML = "Module: ";
    const valCellMod = document.createElement('td');
    valCellMod.innerHTML = moduleName+" "+moduleNumber;
    moduleRow.append(fixCellMod, valCellMod);

    //Second row: Year
    const yearRow = document.createElement('tr');
    const fixCellYear =  document.createElement('td');
    fixCellYear.innerHTML = "Year: ";
    const valCellYear =  document.createElement('td');
    const editForm = document.createElement('input');
    editForm.value = modrunYear;
    editForm.setAttribute('id', 'modrunYear');
    editForm.setAttribute('type', 'number');
    valCellYear.appendChild(editForm);
    yearRow.append(fixCellYear, valCellYear);

    //Third row: Semester
    const semesterRow = document.createElement('tr');
    const fixCellSemester =  document.createElement('td');
    fixCellSemester.innerHTML = "Semester: ";
    const valCellSemester =  document.createElement('td');
    const editFormSem = createSemesterSelector();
    editFormSem.setAttribute('id', 'modrunSem');
    editFormSem.value = modrunSemester;
    valCellSemester.appendChild(editFormSem);
    semesterRow.append(fixCellSemester, valCellSemester);

    //Fourth row: Running
    const runningRow = document.createElement('tr');
    const fixCellRunning =  document.createElement('td');
    fixCellRunning.innerHTML = "Currently running: ";
    const valCellRunning =  document.createElement('td');
    const editFormRunning = document.createElement('input');
    editFormRunning.setAttribute('type', 'checkbox');
    editFormRunning.setAttribute('id', 'modrunRunning');
       if (modrunRunning){
           editFormRunning.setAttribute('checked', 'true');
       }
    valCellRunning.appendChild(editFormRunning);
    runningRow.append(fixCellRunning, valCellRunning);

    //Fifth row: Professors
    const profRow = document.createElement('tr');
    const fixCellProf =  document.createElement('td');
    fixCellProf.innerHTML = "Professor(s): ";
    fixCellProf.setAttribute('style', 'text-align:top');
    const valCellProf =  document.createElement('td');
    const warning = document.createElement('p');
    warning.innerHTML = "Professors in running modules can not be edited!"
    const teacherTable = createTeacherTable(modrunId);
    valCellProf.append(teacherTable, warning);
    profRow.append(fixCellProf, valCellProf);
    table.append(moduleRow, yearRow, semesterRow, runningRow, profRow);
    elements.append(table);

    //save button to save new attributes
    const saveButton = renderSaveButton();

    //back button to get back to students overview
    const backButton = renderBackButton();

    $comp.append(title, elements, backButton, saveButton);

}

/**
 * Render a back button
 * @returns backButton
 */
function renderBackButton() {
    const backButton = document.createElement('button');
    backButton.innerHTML = 'cancel';
    backButton.setAttribute('id', 'backButton');
    backButton.setAttribute('class', 'secondary');
    backButton.addEventListener('click', function () {
        router.go("/moduleruns");
    });
    return backButton;
}

/**
 * Render a dave button
 * @returns saveButton
 */
function renderSaveButton() {
    const saveButton = document.createElement('button');
    saveButton.innerHTML = 'save';
    saveButton.setAttribute('id', 'saveButton');
    saveButton.addEventListener('click', saveButtonListener);
    return saveButton;
}

/**
 * Add a listener for save button
 */
function saveButtonListener() {

    //Getting all values from inputfields
    const yearNew = document.getElementById('modrunYear').value;
    const semesterNew = document.getElementById('modrunSem').value;
    const runningBool = document.getElementById('modrunRunning').checked;

    //Render new buttons
    const removableButton = document.getElementById('saveButton');
    const backButton = document.getElementById('backButton');

    //Remove old buttons
    removableButton.remove();
    backButton.remove();

    //send new data through model & service to backend put-method
    const modrunUpdated = model.createModuleRunWithParams(modId, semesterNew, yearNew, runningBool);
    model.updateModuleRun(modrunUpdated, modrunId);


    //render Success-text and back-button
    elements.append("Changes saved successfully !");
    const newBackButton = renderBackButton();
    newBackButton.innerHTML = "back";
    elements.append(`\n \n`);
    elements.append(newBackButton);
}

/**
 * Create a table with techers
 * @param moduleRunId
 * @returns profList
 */
function createTeacherTable(moduleRunId) {
    const profList = document.createElement('li');
    profList.setAttribute('class', '')
    model.getProfsForModulerun(moduleRunId)
        .then(profs => renderProfList(profs, profList))
        .catch(jqXHR => {
                profList.append('<p className="error">Loading of professors failed! Error code:'+ jqXHR.status + '</p>');
        });
    return profList;
}

/**
 * Render a list of professors
 * @param profs
 * @param profList
 * @returns profList
 */
function renderProfList(profs, profList) {
    let text = "";
    profs.forEach(prof => {
        text += prof.profFirstname + " " + prof.profLastname+", ";
    });
    text = text.substring(0, text.length - 2);
    profList.innerHTML = text;
    return profList;
}

/**
 * Create a selector for choosing a semester
 * @returns semesterSelector
 */
function createSemesterSelector() {
    const semesterSelector = document.createElement('select');
    const optGroup = document.createElement('optgroup');
    const option1 = document.createElement('option');
    const option2 = document.createElement('option');
    option1.setAttribute('value', 'autumn');
    option2.setAttribute('value', 'spring');
    option1.innerHTML = "autumn semester";
    option2.innerHTML = "spring semester";
    optGroup.append(option1, option2);
    semesterSelector.setAttribute('id', 'modrunSem');
    semesterSelector.appendChild(optGroup);
    return semesterSelector;
}