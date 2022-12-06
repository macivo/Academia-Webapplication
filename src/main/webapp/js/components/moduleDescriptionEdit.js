import model from "../model.js";
import router from "../router.js";
import data from '../data.js';

let module = null;
let tempModule = null;

/**
 *  Public interface
 *  */
export default {

    /**
     * Get title for template
     * @returns String with title
     */
    getTitle: function() {
        return "Edit Module Description";
    },

    /**
     * Render element for template tpl-module-description-edit
     * @returns rendered component
     */
    render: function() {
        const $comp = $($('#tpl-module-description-edit').html());

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
                return $comp;
            })
            .catch(jqXHR => {
                // error-message
                let message = 'Unexpected error(' + jqXHR.status + ')';
                $('#message-moduleDescriptionEdit').text(message);
            });

        $('#create_moduleDescEdit', $comp).click(event =>{
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
                        $('#message-moduleDescriptionEdit').text(message);
                    }
                    else{
                        message = 'Unexpected error(' + jqXHR.status + ')'
                        $('#message-moduleDescriptionEdit').text(message);
                    }
                });
        });

        $('#cancel_moduleDesEdit', $comp).click(event => {
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
    $('#editDesNumber', $comp).val(module.number);
    $('#editDesName', $comp).val(module.name);
    $('#editDesDescription', $comp).val(module.description);
    //$('#editDesCoordinator', $comp).val(module.coordinator);
    $('#editDesCoordinator', $comp).val(data.getUser().firstname + " " + data.getUser().lastname);
    $('#editDesEcts', $comp).val(module.ects);
}

/**
 * Read the changed values for editing
 * @param tempModule - the changed module data
 */
function readForm(tempModule){
    tempModule.number = $('#editDesNumber').val();
    tempModule.name = $('#editDesName').val();
    tempModule.description = $('#editDesDescription').val();
    tempModule.coordinator = tempModule.coordinator;
    //tempModule.coordinator = $('#editDesCoordinator').val();
    tempModule.ects = $('#editDesEcts').val();
}