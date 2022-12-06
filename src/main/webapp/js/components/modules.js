import service from "../service.js";
import router from "../router.js";
import model from '../model.js';
import data from '../data.js';

/**
 *  Public interface
 *  */
export default {

    /**
     * Get title for template
     * @returns String with title
     */
    getTitle: function() {
        return "List of Modules";
    },

    /**
     * Render element for template tpl-module-view
     * @returns rendered component
     */
    render: function() {
        const $comp = $($('#tpl-module-view').html());
                    model.getModules()
                        .then(modules => renderModulesList($comp, modules))
                        .catch(jqXHR => {
                            $comp.append('<p class="error">Loading list of modules failed!</p>');
                            return $comp;
                        });
        return $comp;
    }
};

/**
 * Renders the list of modules for this view
 * @param $comp - this template
 * @param modules - the modules to be rendered
 */
function renderModulesList($comp, modules) {

    const elements = document.createElement('div');
    elements.setAttribute('class', 'content-div');
    const title = document.createElement('h2');
    title.innerHTML = 'Modules List';

    //button to create a new student
    const createButton = document.createElement('button');
    createButton.innerHTML = 'create new module';
    createButton.addEventListener('click', function() {
        router.go('/moduleCreate');

    });



    modules.forEach(module => {
        const details = document.createElement('details');
        const summary = document.createElement('summary');

        //edit-button to edit dataset of a student
        const editButton = renderButton("edit");
        editButton.addEventListener('click', function () {
            router.go('/moduleEdit', module.id);
        });

        //editDescription button to edit dataset of a student
        const editDescriptionButton = renderButton("edit description");
        editDescriptionButton.addEventListener('click', function () {
            router.go('/moduleDescriptionEdit', module.id);
        });



            const deleteButton = renderButton("delete");
            deleteButton.onclick = function (even) {
                removeModule(module.id, $comp);
            }



        const ul = document.createElement('ul');
        summary.innerText = module.number + " " + module.name;
        ul.innerHTML = `<li>id: ${module.id}</li>`;
        ul.innerHTML += `<li>Number: ${module.number}</li>`;
        ul.innerHTML += `<li>Name: ${module.name}</li>`;
        ul.innerHTML += `<li>Description: ${module.description}</li>`;
        ul.innerHTML += `<li>Module coordinator: ${module.firstname} ${module.lastname}</li>`;
        ul.innerHTML += `<li>ECTS: ${module.ects}</li>`;
        if((data.getUserRole()=="administrator") && (module.running === true)) {
            ul.innerHTML += `<li class="info">This is a running module and cannot be deleted or edited!</li>`;
        }
        details.append(summary);
        if((data.getUserRole()=="administrator") && (module.running === false)) {
            ul.append(deleteButton);
            ul.append(editButton);
        }


        if ((data.getUserRole()=="professor") && (module.coordinator === data.getUserId()) && (module.running === false)) {
            ul.append(editDescriptionButton);
        }
        if((data.getUserRole()=="professor") && (module.coordinator === data.getUserId()) && (module.running === true)) {
            ul.innerHTML += `<li class="info">This is a running module and module description cannot be edited!</li>`;
        }

        details.append(ul);
        elements.append(details);
    });
    $comp.append(title);
    $comp.append(elements);



    //show button create new Student
    if(data.getUserRole()=="administrator") {
        $comp.append(createButton);
    }
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
 * Remove/delete a module
 * @param id - the id of the module
 */
function removeModule(id, $comp) {
    service.deleteModule(id)
        .then(setTimeout(() => { router.go('/modules') }, 50))
        .catch(jqXHR => {
             $comp.append('<p class="error">Delete Module failed! please try again</p>');
             setTimeout(() => { router.go('/modules') }, 2000);
             return $comp;
        });
}
