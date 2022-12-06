import service from "../service.js";
import data from "../data.js";
import util from "../util.js";

/**
 * Generator a list of student grades as html table to page
 *
 * @param $comp - template
 * @param enrolls - list of enrollments
 * @returns {*} - template + enrollments as html
 */
function renderResult($comp, enrolls){
    const runningModule = document.createElement('details');
    const runningModuleSummary = document.createElement("summary");
    runningModuleSummary.innerText = "Running modules";
    runningModuleSummary.classList.add("myModuleHeadline");
    runningModuleSummary.style.color = "#3d3f49"

    const notRunningModule = document.createElement('details');
    const notRunningModuleSummary = document.createElement("summary");
    notRunningModuleSummary.innerText = "Past modules";
    notRunningModuleSummary.classList.add("myModuleHeadline");
    notRunningModuleSummary.style.color = "#3d3f49"

    runningModule.append(runningModuleSummary);
    notRunningModule.append(notRunningModuleSummary);

    const runningTable = document.createElement('table');
    const notRunningTable = document.createElement('table');

    const runningTableHead = document.createElement('tr');
    runningTableHead.innerHTML = `<th style="width: 10em;">Module Number</th><th style="width: 30em;">Module Name</th><th>Ects</th><th>Grade</th>`;

    const NotRunningTableHead = document.createElement('tr');
    NotRunningTableHead.innerHTML = `<th style="width: 10em;">Module Number</th><th style="width: 30em;">Module Name</th><th>Ects</th><th>Grade</th>`;

    runningTable.append(runningTableHead);
    notRunningTable.append(NotRunningTableHead);

    let haveRunningList = false;
    let haveNotRunningList = false;
    if(enrolls.length === 0) {
        const enrollIsEmpty = document.createElement('h3');
        enrollIsEmpty.innerText = "You dont have any enrollments";
        $comp.append(enrollIsEmpty);
        return $comp;
    } else {
        enrolls.forEach(enroll => {
            const tableRow = document.createElement('tr');
            tableRow.style.fontSize = 'initial';
            tableRow.innerHTML = `<td>${enroll.module_number}</td>`;
            tableRow.innerHTML += `<td>${enroll.name}</td>`;
            tableRow.innerHTML += `<td>${enroll.ects}</td>`;
            tableRow.innerHTML += `<td>${enroll.grade}</td>`;
             if(enroll.running){
                 runningTable.append(tableRow);
                 haveRunningList = true;
             } else {
                 notRunningTable.append(tableRow);
                 haveNotRunningList = true;
            }
        });
    }
    // if a list of module is empty, the table of empty list should not be added
    const enrollIsEmpty = document.createElement('p');
    enrollIsEmpty.style.fontSize = 'initial';
    enrollIsEmpty.style.fontWeight = 'initial';
    if(haveRunningList && haveNotRunningList) {
        runningModule.append(runningTable);
        notRunningModule.append(notRunningTable);
    } else if (!haveRunningList){
        enrollIsEmpty.innerText = "Your enrollment for running-module is empty";
        runningModule.append(enrollIsEmpty);
        notRunningModule.append(notRunningTable);
    } else if (!haveNotRunningList){
        enrollIsEmpty.innerText = "Your enrollment for not-running-module is empty";
        runningModule.append(runningTable);
        notRunningModule.append(enrollIsEmpty);
    }
    $comp.append(runningModule, notRunningModule);
    return $comp;
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
        return "My Results";
    },

    /**
     * Render element for template tpl-my-modules
     * @returns rendered component
     */
    render: function() {
        const $comp = $($('#tpl-my-results').html());
        service.getEnrollmentByStudentId(data.getStudentId())
            .then(enrolls => renderResult($comp, enrolls))
            .catch(jqXHR => {
                util.showOkModal("Error", "Loading list of enrollment failed!");
                return $comp;
            });
        return $comp;
    }
};