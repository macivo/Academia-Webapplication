import model from "../model.js";
import router from "../router.js";

let module = null;
let tempModule = null;
let profs = null;

/**
 *  Public interface
 *  */
export default {

    /**
     * Get title for template
     * @returns String with title
     */
    getTitle: function() {
        return "Edit Modules";
    },

    /**
     * Render element for template tpl-module-edit
     * @returns rendered component
     */
    render: function() {
        const $comp = $($('#tpl-module-edit').html());

        //get id from modules.component....
        const urlComp = window.location.href;
        const lastItem = urlComp.substring(urlComp.lastIndexOf('/') + 1);


        //get Data from module with this id
        model.getModule(lastItem)
            .then(moduleData => {
                module = moduleData;
                //make a copy of the received date in tempModule
                tempModule = Object.assign({}, module);
                //fill the tempModule data in form
                fillForm($comp, tempModule);
                //fetch all profs from Database
                renderProfs($comp, tempModule);
                return $comp;
            })
            .catch(jqXHR => {
                // error-message
                let message = 'Unexpected error(' + jqXHR.status + ')';
                $('#message-moduleEdit').text(message);
            });



        $('#create_moduleEdit', $comp).click(event =>{
            event.preventDefault();
            if (!document.querySelector('form').reportValidity()) return;
            //read the changed data from form into tempModule
            readForm(tempModule);
            //update the DB with the data from tempModule
            model.updateModule(tempModule, lastItem)
                .then(() => router.go('/modules'))
                .catch(jqXHR => {
                    // error-message
                    let message = "";
                    if(jqXHR.status === 406){
                        message = 'Special characters are not allowed [<>$()=;/]';
                        $('#message-moduleEdit').text(message);
                    }
                    else{
                        message = 'Unexpected error(' + jqXHR.status + ')'
                        $('#message-moduleEdit').text(message);
                    }
                });
        });

        $('#cancel-moduleEdit', $comp).click(event => {
            event.preventDefault();
            router.go('/modules');
        });
        return $comp;
    }
};
/**
 * Fill form with data from existing module
 * @param $comp - this template
 * @param module - the module to be filled in
 */
function fillForm($comp, module){
    $('#editNumber', $comp).val(module.number);
    $('#editName', $comp).val(module.name);
    $('#editDescription', $comp).val(module.description);
    //$('#editCoordinator', $comp).val(module.coordinator);
    $('#editCoordinator', $comp).val(module.firstname + " " + module.lastname);
    $('#editEcts', $comp).val(module.ects);
}
/**
 * Read the changed values for editing
 * @param tempModule - the changed module data
 */
function readForm(tempModule){
    tempModule.number = $('#editNumber').val();
    tempModule.name = $('#editName').val();
    tempModule.description = $('#editDescription').val();
    tempModule.coordinator = $('#editCoordinator').val();
    tempModule.ects = $('#editEcts').val();
}

/**
 * Render all profs for selection of module coordinator in create module
 * @param $comp - this template
 */
function renderProfs($comp, tempModule) {
    let $select = $('select', $comp);
    $select.empty();
    model.getProfs()
        .then((data) => {
            profs = data;
            for (let prof of profs) {
                let select = "";
                if (prof.personal_id == tempModule.coordinator){
                    select= ' selected="selected"';
                }
                let $opt = $('<option value="' + prof.personal_id + '"' + select+ '>' + prof.firstname + ' ' + prof.lastname + '</option>');
                $select.append($opt);
            }
        })
        .catch(jqXHR => {
            let message = "";
            if(jqXHR.status === 500){
                message = 'Unexpected error(' + jqXHR.status + ')'
                $('#message-module').text(message);
            }})

}