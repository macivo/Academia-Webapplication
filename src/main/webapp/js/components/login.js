import service from "../service.js";
import data from "../data.js";
import router from "../router.js";

/**
 *  checking login, shows notification if errors
 *  @param $comp template
 */
function renderLogin($comp) {
    const username = document.forms[0].username.value;
    const password = document.forms[0].password.value;
    if (!username || !password) {
        document.getElementById('error').innerText = "Please enter a username and password";
    } else {
        service.getPersonByUsername(username, md5(password))
            .then(person => {
                if (person.username === username && person.password === md5(password)) {
                    data.setUser(person);
                    document.getElementById('error').innerText = "Login successful";
                    setTimeout(function () {
                        router.go('/home');
                    }, 1000);
                } else {
                    wrongLogin();
                }
            })
            .catch(jqXHR => {
                wrongLogin();
            });
    }
}

/**
 * Showing that the input values of login is wrong, and reset the input form
 */
function wrongLogin() {
    document.getElementById('error').innerText = "Wrong login data";
    setTimeout(function () {
        document.getElementById('error').innerText = "";
        document.getElementById('username').value = "";
        document.getElementById('password').value = "";
    }, 3000);
}

/**
 *  Public interface
 * */
export default {

    /**
     * Get title for template
     * @returns String with title
     */
    getTitle: function () {
        return "Login";
    },

    /**
     * Render element for template tpl-login
     * @returns rendered component
     */
    render: function () {
        const $comp = $($('#tpl-login').html());
        $('.header_navi').css("visibility", "hidden");
        $('.logo').css({"display": "block", "margin": "auto", "float": "none"});
        $('.information').hide();
        $('[data-action=login]', $comp).click(function (e) {
            renderLogin();
            e.preventDefault();
        });
        return $comp;
    }
};