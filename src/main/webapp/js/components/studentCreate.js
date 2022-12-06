import service from "../service.js";
import model from '../model.js';
import router from '../router.js';
import dataBinder from '../util.js';

/**
 *  Public interface
 *  */
export default {

    /**
     * Get title for template
     * @returns String with title
     */
    getTitle: function() {
        return "Create new Student";
    },

    /**
     * Render element for template tpl-new-student
     * @returns rendered component
     */
    render: function() {
        const $comp = $($('#tpl-new-student').html());
        let tempStudent = model.createStudent();
        dataBinder.bind($comp, tempStudent);

        $('#create-student', $comp).click(event => {
            event.preventDefault();
            if (!document.querySelector('form').reportValidity()) return;
            model.addStudent(tempStudent)
                .then(() => router.go('/students'))
                .catch(jqXHR => {
                let message = "";
                if(jqXHR.status === 409){
                    message = 'Student already exists';
                    $('#message').text(message);
                }
                else if(jqXHR.status === 406){
                    message = 'Special characters are not allowed [<>$()=;/]';
                    $('#message').text(message);
                }
                else{
                    message = 'Unexpected error(' + jqXHR.status + ')'
                    $('#message').text(message);
                }
            });
        });
        $('#cancel', $comp).click(event => {
            event.preventDefault();
            router.go('/students');
        });
        return $comp;
    }
};

