import router from "../router.js";
import model from '../model.js';
import data from '../data.js';


let myModuleRuns = null;
/**
 *  Public interface
 *  */
export default {
    getTitle: function() {
        return "List of your running modules";
    },

    /**
     * Render element for template tpl-mymodule-view
     * @returns rendered component
     */

    render: function() {
        const $comp = $($('#tpl-mymodule-view').html());

        const userId = data.getUserId();

        model.getModuleRunForProf()
            .then(modulesData => {
                myModuleRuns = modulesData;
                renderMyModulesList($comp, myModuleRuns)
            })
            .catch(jqXHR => {
                $comp.append('<p class="error">Loading list of your modules failed!</p>');
                return $comp;
            });

        return $comp;
    },
    getModuleRun: function() {
        return myModuleRuns;
    },

};

/**
 * Render the list of moduleruns for a teacher
 * @param $comp -  this template
 * @param myModuleRuns - the modulesruns for a theacher
 */
function renderMyModulesList($comp, myModuleRuns) {

    const elementsRunning = document.createElement('div');
    const elementsNotRunning = document.createElement('div');
    const title = document.createElement('h2');
    title.innerHTML = 'Modules you are teaching';


    const runningModules = document.createElement('h3');
    runningModules.classList.add("myModuleHeadline")
    runningModules.innerHTML = 'Running modules';

    const notRunningModules = document.createElement('h3');
    notRunningModules.classList.add("myModuleHeadline")
    notRunningModules.innerHTML = 'Not running modules';

    myModuleRuns.forEach(module => {

        if (module.running === true) {


            const details = document.createElement('details');
            const summary = document.createElement('summary');

            //seeStudent-Button render
            const seeStudentsButton = renderButton("show my students");
            seeStudentsButton.addEventListener('click', function() {
                router.go('/studentsInModuleRun', module.id);
            });

            const ul = document.createElement('ul');
            summary.innerText = module.moduleNumber + " " + module.moduleName;
            ul.innerHTML += `<li>Semester: ${module.semester}</li>`;
            ul.innerHTML += `<li>Year: ${module.year}</li>`;
            ul.innerHTML += `<li>id: ${module.id}</li>`;
            ul.innerHTML += `<li>Module id: ${module.moduleId}</li>`;
            ul.innerHTML += `<li>Module running: ${module.running}</li>`;

            ul.append(seeStudentsButton);

            details.append(summary);
            details.append(ul);
            elementsRunning.append(details);

        }
    });

    myModuleRuns.forEach(module => {

        if (module.running === false) {

            const details = document.createElement('details');
            const summary = document.createElement('summary');

            const ul = document.createElement('ul');
            summary.innerText = module.moduleNumber + " " + module.moduleName;
            ul.innerHTML += `<li>Semester: ${module.semester}</li>`;
            ul.innerHTML += `<li>Year: ${module.year}</li>`;
            ul.innerHTML += `<li>id: ${module.id}</li>`;
            ul.innerHTML += `<li>Module id: ${module.moduleId}</li>`;
            ul.innerHTML += `<li>Module running: ${module.running}</li>`;

            details.append(summary);
            details.append(ul);
            elementsNotRunning.append(details);

        }
    });

    $comp.append(title);
    $comp.append(runningModules);
    $comp.append(elementsRunning);
    $comp.append(notRunningModules);
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