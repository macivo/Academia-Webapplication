/*
 * Academia (c) 2020, Bern University of Applied Sciences, Switzerland
 * COPY CODE FROM LESSON of Web Programming (BTI1301a) 20 (Prof. Philipp Locher)
 */
import data from "./data.js";
import util from "./util.js";

const routes = Object.create(null);
const $main = $('main');

/**
 * main element of index.html will be replace by a view
 * @param $view
 */
function setView($view) {
    $main.fadeOut(150, function(){ $main.empty().append($view).fadeIn(300); });
}

/**
 * get a view from a component
 */
function render() {
    const hash = decodeURI(location.hash).replace('#/', '').split('/');
    const path = '/' + (hash[0] || '');
    if(!routes[path]) {
        setView($("<h2>404 Not Found</h2><p>Sorry, page not found!</p>"));
        return;
    }
    // Authentication checking
     if(!data.getUser()) {
        location.hash = '/login';
     }
    util.setInformation();
    const component = routes[path];
    const param = hash.length > 1 ? hash[1] : '';
    const $view = component.render(param);
    setView($view);
    document.title = "Academia: " + (component.getTitle ? " " + component.getTitle() : " ");
}
$(window).on('hashchange', render)

/**
 * Public interface
 * */
export default {
    /**
     * register a path for a component
     * @param path of component
     * @param name of component
     */
    register: function (path, component) {
        routes[path] = component;
    },

    /**
     * Go to path
     * @param path
     * @param param
     */
    go: function(path, param) {
        path += param ? '/' + param : '';
        if (location.hash !== '#' + path) {
            location.hash = path;
        } else {
            render();
        }
    }
};