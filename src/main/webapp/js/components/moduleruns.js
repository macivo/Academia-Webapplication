import service from "../service.js";
import router from "../router.js";
import model from "../model.js";
import data from "../data.js";

/**
 *  Public interface
 *  */
export default {

    /**
     * Get title for template
     * @returns String with title
     */
    getTitle: function() {
        return "List of Module Runs";
    },

    /**
     * Render element for template tpl-modulerun-view
     * @returns rendered component
     */
    render: function() {
        const $comp = $($('#tpl-modulerun-view').html());
            model.getModuleRuns()
                .then(moduleruns => renderModulerunList($comp, moduleruns))
                .catch(() => {
                    $comp.append('<p class="error">Loading list of module runs failed!</p>');
                    return $comp;
                });

        return $comp;
    }
};


/**
 * Render a modulerun list
 * @param $comp rendered view
 * @param moduleruns
 */
function renderModulerunList($comp, moduleruns) {
    let elements = document.createElement('div');
    elements.setAttribute('class', 'content-div');
    const title = document.createElement('h2');
    title.innerHTML = 'Module Runs';

    let moduleList = new Set;
    let moduleRunListRunning = [];
    let moduleRunListPast = [];

    const message = document.createElement('p');
    message.setAttribute('id', 'message');

    moduleruns.forEach(modulerun => {

        if (modulerun.moduleId != 0) {
            moduleList.add(new ModuleList(modulerun.moduleName, modulerun.moduleNumber, modulerun.moduleId));
        }


        if (modulerun.id != 0) {
            if (modulerun.running) {
                moduleRunListRunning.push(new ModuleRunList(modulerun.id, modulerun.semester, modulerun.year, modulerun.running, modulerun.moduleId, modulerun.moduleName, modulerun.moduleNumber));
            } else {
                moduleRunListPast.push(new ModuleRunList(modulerun.id, modulerun.semester, modulerun.year, modulerun.running, modulerun.moduleId, modulerun.moduleName, modulerun.moduleNumber));
            }
        }

    });

    /**
     * Differentiate between running and past moduleruns, render with different methods
     */
    for (const modulerunRunning of moduleRunListRunning) {
        elements.append(renderListElement(modulerunRunning));
    }
    for (const modulerunPast of moduleRunListPast) {
        elements.append(renderListElement(modulerunPast));
    }
    $comp.append(title);
    $comp.append(message);
    $comp.append(elements);

    //Create and show List of Modules to create new module run
    const title2 = document.createElement('h2');
    title2.innerHTML = 'Create new Module Run';
    $comp.append(title2);
    let moduleDropdown = renderModuleDropdown(moduleList);
    $comp.append(moduleDropdown);
    $comp.append(renderCreateButton());

}

/**
 * Render create button
 * @returns button for creating a new modulerun
 */
function renderCreateButton() {
    const createButton = document.createElement('button');
    createButton.innerHTML = 'create';
    createButton.addEventListener('click', function() {
        const moduleChoice = document.getElementById('moduleChoice').value;
        data.setModuleId(moduleChoice);
        data.setModuleName()
        router.go('/modulerunCreate');
    })
    return createButton;
}

/**
 * Render a modulerun (= list element)
 * @param modulerun
 * @returns details-element
 */
function renderListElement(modulerun) {
    const details = document.createElement('details');
    const summary = document.createElement('summary');

    const ul = document.createElement('ul');

    summary.innerText = renderTitle(modulerun.semester, modulerun.year) + " " + modulerun.moduleName;
    ul.innerHTML = `<li>id: ${modulerun.id}</li>`;
    ul.innerHTML += `<li>${modulerun.semester}-semester ${modulerun.year}</li>`;
    let running = " ";
    if (modulerun.running) {
        running = "Status: Currently running";
    } else {
        running = "Status: Completed";
        summary.setAttribute('style', 'color: #c2c0b6')
    }

    const liRunning = document.createElement('li');
    liRunning.innerHTML = running;
    ul.append(liRunning);
    ul.append(createTeacherTable(modulerun.id));
    if (modulerun.running) {
        ul.append(renderEditButton(modulerun));
    }
    ul.append(renderDeleteButton(modulerun));



    details.append(summary);
    //ul.append(editButton);
    details.append(ul);
    return details;

}

/**
 * Render a delete button for a modulerun
 * @param modulerun
 * @returns Deletebutton
 */
function renderDeleteButton(modulerun){
    const deleteButton = renderButton("delete");
    deleteButton.addEventListener('click', function () {
        removeModuleRun(modulerun.id);
    });
    return deleteButton;
}

/**
 * Render an edit button
 * @param modulerun
 * @returns Editbutton
 */
function renderEditButton(modulerun){
    const modulerunId = modulerun.id;
    const editButton = renderButton("edit");
    editButton.addEventListener('click', function() {
        router.go('/modulerunEdit', modulerunId);
    });
    return editButton;
}

/**
 * Render a moduledropdown to choose of which module to create a modulerun
 * @param moduleList
 * @returns moduleDropdown
 */
function renderModuleDropdown(moduleList) {
    let moduleDropdownList = moduleList;
    moduleDropdownList = deleteDuplicatesModuleList(moduleList);
    let moduleDropdown = document.createElement('select');
    let optGroup = document.createElement('optgroup');
    optGroup.setAttribute('value', "Choose a Module..");
    for (const element of moduleDropdownList) {
        const option = document.createElement('option');
        option.setAttribute('value', element.moduleId);
        option.innerHTML = element.moduleName+" "+element.moduleNumber;
        optGroup.append(option);
    }
    moduleDropdown.append(optGroup);
    moduleDropdown.setAttribute('id', 'moduleChoice');
    return moduleDropdown;
}

/**
 * Create a table with teachers
 * @param moduleRunId
 * @returns profList list with profs
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
 * Render a list with profs
 * @param profs
 * @param profList empty proflist
 * @returns profList full proflist
 */
function renderProfList(profs, profList) {
    let text = "Professor(s): ";
    profs.forEach(prof => {
        text += prof.profFirstname + " " + prof.profLastname+", ";
    });
    text = text.substring(0, text.length - 2);
    profList.innerHTML = text;
    return profList;
}

/**
 * Module list to persist a modulelist object
 * @param moduleName
 * @param moduleNumber
 * @param moduleId
 * @constructor
 */
function ModuleList(moduleName, moduleNumber, moduleId) {
    this.moduleName = moduleName;
    this.moduleNumber = moduleNumber;
    this.moduleId = moduleId;
}

/**
 * Modulerun list to persist a modulerunlist object
 * @param id
 * @param semester
 * @param year
 * @param running
 * @param moduleId
 * @param moduleName
 * @param moduleNumber
 * @constructor
 */
function ModuleRunList(id, semester, year, running, moduleId, moduleName, moduleNumber) {
    this.id = id;
    this.semester = semester;
    this.year = year;
    this.running = running;
    this.moduleId = moduleId;
    this.moduleName = moduleName;
    this.moduleNumber = moduleNumber;
}

/**
 * Delete duplicate in module list.
 * @param moduleList
 * @returns moduleList without duplicates
 */
function deleteDuplicatesModuleList(moduleList) {
    let moduleList1 = moduleList;
    let moduleList2 = new Set;
    let matchFound = false;
    for (const item1 of moduleList1.values()) {
        matchFound = false;
        for (const item2 of moduleList2.values()) {
            if (item1.moduleId === item2.moduleId) {
                matchFound = true;
            }
        }
        if (!matchFound) {
            moduleList2.add(item1);
        }
    }
    return moduleList2;
}


/**
 * Render a button
 * @param text for button
 * @returns Button
 */
function renderButton(text) {
    const button = document.createElement('button');
    button.innerHTML = text;
    return button;
}

/**
 * Render a title for semester and year
 * @param semester
 * @param year
 * @returns titleString with semester and year
 */
function renderTitle(semester, year) {
    let titleString = " ";
    if (semester === "spring") {
        titleString = "SS ";
    } else {
        titleString = "AS ";
    }
    return titleString = titleString + year;
}

/**
 * Remove a module run
 * @param id of modulerun
 */
function removeModuleRun(id) {
    service.deleteModuleRun(id)
        .then(document.getElementById('message').innerHTML = "modulerun deleted!")
        .then(document.getElementById('message').style.transition="opacity 5s")
        .then(setTimeout(() => {router.go('/moduleruns')}, 1000))
        .catch(jqXHR => {
            document.getElementById('message').innerHTML = '<p className="error">Removal of module run failed! Error code:'+ jqXHR.status + '</p>';
        });
}

