import data from "../data.js";
import router from "../router.js";

/**
 *  Clear all data. Then go to homepage after 3 second
 *  @param $comp template
 */
function render($comp) {
    data.logout();
    const msg = document.createElement('h3');
    msg.innerHTML = 'Logout was successful !!!';
    $comp.append(msg);
    $('.header_navi').css("visibility","hidden");
    setTimeout(function(){window.location.reload(true)},1000);
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
        return "Logout";
    },

    /**
     * Render element for template tpl-logout
     * @returns rendered component
     */
    render: function() {
        const $comp = $($('#tpl-logout').html());
        render($comp);
        return $comp;
    }
};