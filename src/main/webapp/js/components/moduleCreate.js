import model from '../model.js';
import router from '../router.js';
import dataBinder from '../util.js';

let profs;
/**
 *  Public interface
 *  */
export default {

    /**
     * Get title for template
     * @returns String with title
     */
    getTitle: function() {
        return "Create new Module";
    },

    /**
     * Render element for template tpl-new-module
     * @returns rendered component
     */
    render: function() {
        const $comp = $($('#tpl-new-module').html());
        renderProfs($comp);

        let tempModule = model.createModule()
        dataBinder.bind($comp, tempModule);
        $('#create-module', $comp).click(event => {
            event.preventDefault();
            if (!document.querySelector('form').reportValidity()) return;
            model.addModule(tempModule)
                .then(() => router.go('/modules'))
                .catch(jqXHR => {
                    let message = "";
                    if(jqXHR.status === 409){
                        message = 'Module already exists';
                        $('#message-module').text(message);
                    }
                    else if(jqXHR.status === 403){
                        message = 'This Coordinator is not a professor';
                        $('#message-module').text(message);
                    }
                    else if(jqXHR.status === 406){
                        message = 'Special characters are not allowed [<>$()=;/]';
                        $('#message-module').text(message);
                    }
                    else{
                        message = 'Unexpected error(' + jqXHR.status + ')'
                        $('#message-module').text(message);
                    }
                });

        });
        $('#cancel-module', $comp).click(event => {
            event.preventDefault();
            router.go('/modules');
        });
        return $comp;
    }
};

/**
 * Render all profs for selection of module coordinator in create module
 * @param $comp - this template
 */
function renderProfs($comp) {
    let $select = $('select', $comp);
    $select.empty();
    $select.append('<option value="">Select module coordinator</option>');
    model.getProfs()
        .then((data) => {
            profs = data;
            for (let prof of profs) {
                let $opt = $('<option value="' + prof.personal_id + '">' + prof.firstname + ' ' + prof.lastname + '</option>');
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

