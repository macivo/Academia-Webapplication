import router from "../router.js";
import model from "../model.js";
import data from "../data.js";

/**
 * Public interface with main title for module run and render-function
 */
export default {

    /**
     * Get title for template
     * @returns String with title
     */
    getTitle: function() {
        return "Create new Module Run";
    },

    /**
     * Render element for template tpl-modulerun-view
     * @returns rendered component
     */
    render: function() {
        const $comp = $($('#tpl-modulerun-view').html());
        const moduleId = data.getModuleId();
        model.getModule(moduleId)
            .then(moduledata => renderModulerunList($comp, moduleId, moduledata))
            .catch(() => {
                $comp.append('<p class="error">Loading list of module runs failed!</p>');
                return $comp;
            });
        return $comp;
    }
};

//a few global variables to be used in different functions
let elements;
let modData;
let profSelector;
let profsForModrun = new Array;
let profRow;

/**
 * Render the modulerun-list
 * @param $comp content for template #tpl-modulerun-view
 * @param moduleId the id of the module, user wants to create a modulerun of
 * @param moduleData additional data (name, number) for the module
 */
function renderModulerunList($comp, moduleId, moduleData) {
    modData = moduleData;
    const moduleName = moduleData.name;
    const moduleNumber = moduleData.number;

    elements = document.createElement('div');
    elements.setAttribute('class', 'content-div');
    const title = document.createElement('h2');
    title.innerHTML = 'Create Module Run';


    //create table to enter credentials for module run
    const table = document.createElement('table');
    table.setAttribute('id', 'maintable');

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
    editForm.setAttribute('placeholder', 'Please enter year');
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
    valCellSemester.appendChild(editFormSem);
    semesterRow.append(fixCellSemester, valCellSemester);

    //fourth row: Profs
    profRow = document.createElement('tr');
    profRow.setAttribute('class', 'table-noborder');
    const fixCellProf =  document.createElement('td');
    fixCellProf.setAttribute('class', 'cell-topalign');
    fixCellProf.innerHTML = "Professor(s): ";
    const valCellProf =  document.createElement('td');
    valCellProf.setAttribute('class', 'cell-topalign');
    const valCellPlus =  document.createElement('td');
    valCellPlus.setAttribute('class', 'cell-topalign');
    const addedProfs = document.createElement('table');
    addedProfs.setAttribute('class', 'table-noborder');
    addedProfs.setAttribute('style', 'display:none');
    addedProfs.setAttribute('id', 'addedProfs');
    valCellPlus.appendChild(renderAddTeacherButton());
    fetchAndCreateTeacherSelector();
    valCellProf.append(addedProfs, profSelector);
    profRow.append(fixCellProf, valCellProf, valCellPlus);
    profRow.setAttribute('style', 'display:none');

    table.append(moduleRow, yearRow, semesterRow, profRow);
    elements.append(table);
    $comp.append(title);
    $comp.append(elements);
    $comp.append(renderBackButton(), renderModulerunSaveButton());

}

/**
 * A aelector for selecting the semester
 * @returns semesterSelector with two options: autumn and spring
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

/**
 * fetch profs from backend and create a selector
 */
function fetchAndCreateTeacherSelector() {
    profSelector = document.createElement('select');
    profSelector.setAttribute('id', 'profSel');
    model.getProfs()
        .then(profs => renderSelectorProfList(profs))
        .catch(jqXHR => {
            profList.append('<p className="error">Loading of professors failed! Error code:'+ jqXHR.status + '</p>');
        });
}

/**
 * Render an optgroup of professors to select from
 * @param profs fetched in function fetchAndCreateTeacherSelector()
 */
function renderSelectorProfList(profs) {
    const optGroup = document.createElement('optgroup');
    profs.forEach(prof => {
        const option = document.createElement('option');
        option.innerHTML = prof.firstname+" "+prof.lastname;
        option.setAttribute('value', prof.personal_id);
        optGroup.append(option);
    });
    profSelector.appendChild(optGroup);
}

/**
 * Render a button to get from moduleruncreate.js to moduleruns.js
 * @returns backButton named 'back' which leads to moduleruns.js
 */
function renderBackButton() {
    const backButton = document.createElement('button');
    backButton.innerHTML = 'back';
    backButton.setAttribute('id', 'backButton');
    backButton.setAttribute('class', 'secondary');
    backButton.addEventListener('click', function () {
        router.go("/moduleruns");
    });
    return backButton;
}

/**
 * Render a button to add a teacher
 * @returns teacherButton adding a teacher
 */
function renderAddTeacherButton() {
    const teacherButton = document.createElement('button');
    teacherButton.innerHTML = 'add teacher';
    teacherButton.setAttribute('id', 'teacherButton');
    teacherButton.addEventListener('click', teacherButtonListener);
    return teacherButton;
}

/**
 * Render a button to save the whole dataset
 * @returns saveButton for modulerun
 */
function renderModulerunSaveButton() {
    const saveButton = document.createElement('button');
    saveButton.innerHTML = 'save';
    saveButton.setAttribute('id', 'save_Button');
    saveButton.addEventListener('click', ModulerunSaveButtonListener);
    return saveButton;
}

/**
 * Render save button for saving added teachers
 * @returns saveButton
 */
function renderTeacherSaveButton2() {
    const saveButton = document.createElement('button');
    saveButton.innerHTML = 'save & back';
    saveButton.setAttribute('id', 'save_Button');
    saveButton.addEventListener('click', teacherSaveButtonListener);
    return saveButton;
}

/**
 * Render a delete button
 * @returns delButton
 */
function renderDeleteButton() {
    const delButton = document.createElement('button');
    delButton.setAttribute('class', 'small');
    delButton.innerHTML = "-";
    return delButton;
}

/**
 * Buttonlistener for savebutton for moduleruns
 */
function ModulerunSaveButtonListener() {

    //Getting all values from user input
    const modrunYear = document.getElementById("modrunYear").value;
    document.getElementById("modrunYear").setAttribute('disabled', 'true');
    const modrunSem = document.getElementById("modrunSem").value;
    document.getElementById("modrunSem").setAttribute('disabled', 'true');
    const modrun = model.createModuleRunWithParams(modData.id, modrunSem, modrunYear, true);

    //Add module run, catch possible exceptions
    let message = "Saved successfully. Add teachers"
    model.addModuleRun(modrun)
        .then(() => modrun)
        .catch(jqXHR => {
            if(jqXHR.status === 403){
                message = "You are not authorized to do this";
            }else if(jqXHR.status === 400){
                message = "Bad request - Could not create Dataset";
            }else if (jqXHR.status === 500){
                message = "can not write to database";
            }else{
                message = "Unexpected error(" + jqXHR.status + ")";
            }
        });
    elements.append(message)

    //Remove old buttons
    const removableButton = document.getElementById('save_Button');
    const backButton = document.getElementById('backButton');
    profRow.setAttribute('style', 'display:true');
    removableButton.remove();
    backButton.remove();

    //Display new buttons
    const newSaveButton = renderTeacherSaveButton2();
    newSaveButton.innerHTML = "Save";
    elements.append(newSaveButton);
    elements.append(status);

}

/**
 * Buttonlistener for savebutton for added professors
 */
function teacherSaveButtonListener() {

    //Get modulerun-id
    const modrunId = model.getModulerunId();

    //For each professor, create a dataset in DB-table 'teaching'
    profsForModrun.forEach(prof => {
        const teaching = model.createTeaching(modrunId, prof);
        model.createProfTeaching(teaching)
            .then(() => teaching)
            .catch(jqXHR => {
                if(jqXHR.status === 403){
                    status = "You are not authorized to do this"
                        .elements.append(status);
                }
                else if (jqXHR.status === 500){
                    status = "can not write to database"
                        .elements.append(status);
                }else{
                    status = "Unexpected error(" + jqXHR.status + ")"
                        .elements.append(status);
                }
            });
    });

    //Empty array of profs and prof row for next use
    profsForModrun = [];
    profRow = null;

    //go back to modulerun overview
    router.go("/moduleruns");
}

/**
 * Buttonlistener for addTeacherButton
 */

function teacherButtonListener() {

    //get all input values from user
    const profSel = document.getElementById('profSel');
    const profSelPersonalId = profSel.value;
    const profSelIndex = profSel.selectedIndex;
    const profSelName = profSel.options[profSelIndex].innerHTML;

    /**
     * check, if a teacher chosen from dropdown menu is already added to the list. If he/she is not, add him/her
     * If he/she is already added, do nothing and skip this part
     */
    if (!profsForModrun.includes(profSelPersonalId)) {

        //push personal id of added teacher into Array profsForModrun
        profsForModrun.push(profSelPersonalId);

        // put added teachers name into table addedProfs to be displayed immediately
        const addedProfs = document.getElementById('addedProfs');
        const newProf = document.createElement('tr');
        newProf.setAttribute('class', 'table-noborder');
        newProf.innerHTML = profSelName;

        //create a delete button for this prof
        const delButton = renderDeleteButton();

        // remove prof from list if delete button is clicked
        delButton.addEventListener('click', function () {
            newProf.remove();
            profsForModrun.pop();
        });

        // render newly added profs name & delete button
        newProf.append(delButton);
        addedProfs.append(newProf);
        addedProfs.setAttribute('style', 'display:true');
    }

}
