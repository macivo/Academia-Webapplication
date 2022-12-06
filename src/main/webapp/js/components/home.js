import data from "../data.js";

/**
 * Show navigation and user information
 */
function render(){
    $('.logo').removeAttr('style');
    $('.information').show();
    $('.header_navi').css("visibility","visible");
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
        return "Home";
    },

    /**
     * Render element for template tpl-home
     * @returns rendered component
     */
    render: function() {
        const $comp = $($('#tpl-home').html());

        if (data.getUserRole() === "administrator") {
            document.getElementById("teacherModuleruns").style.display="none";
            document.getElementById("enrollment").style.display="none";
            document.getElementById("results").style.display="none";
        }

        if (data.getUserRole() === "professor") {
            document.getElementById("students").style.display="none";
            document.getElementById("moduleruns").style.display="none";
            document.getElementById("enrollment").style.display="none";
            document.getElementById("results").style.display="none";
        }

        if (data.getUserRole() === "student") {
            document.getElementById("students").style.display="none";
            document.getElementById("teacherModuleruns").style.display="none";
            document.getElementById("moduleruns").style.display="none";
        }
        render();
        return $comp;
    }
};