/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 * Group 3 - PT3 BTI3022
 * Developers: Nicole Herold, Rebecca Tabea Vogt, Mac Müller
 */

import data from "./data.js";

/*Any function for global using*/
export default {
    /**
     * Binding a object to a input-form
     *
     * @param $comp a template
     * @param object from AJAX call
     */
    bind: function($comp, object) {
        $('[data-field]', $comp).each(function() {
            let key = $(this).attr('data-field');
            if ($(this).val()) {
                object[key] = $(this).val();
            }
            $(this).change(() => object[key] = $(this).val());
        });
    },
    /**
     * Set a information of a person and should be showed in index.html
     */
    setInformation: function (){
        if(data.getUser()){
            const user = data.getUser();
            let title = ' ';
            if (user.role === 'professor') {
                title = 'Professor ';
                $('#info_matNo_label').hide();
                $('#info_matNo').hide();
            } else if (user.role === 'administrator'){
                    title = 'Administrator ';
                    $('#info_matNo_label').hide();
                    $('#info_matNo').hide();
            } else {
                $('#info_matNo_label').show();
                $('#info_matNo').show();
                $('#info_matNo').text(data.getMatNo());
            }

            //to show photo in info
            if ((user.role != 'student') && (user.gender === 'male')){
                $('#user_photo').attr("src", "../img/profMan.png");
            }
            if ((user.role != 'student') && (user.gender === 'female')){
                $('#user_photo').attr("src", "../img/profWoman.png");
            }
            if ((user.role === 'student') && (user.gender === 'male')){
                $('#user_photo').attr("src", "../img/man.png");
            }
            if ((user.role === 'student') && (user.gender === 'female')){
                $('#user_photo').attr("src", "../img/woman.png");
            }
            if (user.gender === 'divers'){
                $('#user_photo').attr("src", "../img/divers.png");
            }


            $('#info_title').text(title);
            $('#info_name').text(user.firstname+' '+user.lastname);
            $('#info_dateOfBirth').text(user.date_of_birth);
            $('#info_email').text(user.email);
        }
    },
    /**
     * Show a modal with "ok" button
     *
     * @param title ex. Error
     * @param text of information
     */
    showOkModal: function (title, text){
        const modal = document.getElementById("modal");
        const modalContainer = document.getElementById("modal_container");
        const modal_title = document.getElementById("modal_title");
        modal_title.innerText = title;
        const modal_text = document.getElementById("modal_text");
        modal_text.innerText = text;
        const okButton = document.createElement("button");
        okButton.innerText = "OK"
        modalContainer.appendChild(okButton);
        modal.style.display = "block";
        // close modal with "ok" button
        okButton.addEventListener('click', function (){
            closeModal();
        });
        // close modal if clicked on modal, clicking on modal-container will not close the modal
        window.onclick = function(event) {
            if (event.target === modal) {
                closeModal();
            }
        }
        function closeModal() {
            modal.style.display = "none";
            modalContainer.removeChild(okButton);
        }
    },
    /**
     * Show a notification with out "ok" button.
     * Notification disappears after 1.5 seconds
     *
     * @param title of notification
     * @param text of notification
     */
    showFastNotify: function (title, text) {
        const modal = document.getElementById("modal");
        const modalContainer = document.getElementById("modal_container");
        const modal_title = document.getElementById("modal_title");
        modal_title.innerText = title;
        const modal_text = document.getElementById("modal_text");
        modal_text.innerText = text;
        modal.style.display = "block";
        setTimeout(function(){
            modal.style.display = "none";
        }, 1500);
    }

}