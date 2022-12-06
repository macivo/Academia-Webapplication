/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 * Group 3 - PT3 BTI3022
 * Developers: Nicole Herold, Rebecca Tabea Vogt, Mac Müller
 */
import service from "./service.js";

let data = {};

/**
 * searching for a matriculation number
 * @param id a personal_id
 */
function setStudent(id){
    service.getStudents()
        .then(students => {
            students.forEach(student => {
                if(student.personal_id === id) {
                    data.student = student;
                }
            });
        })
        .catch(jqXHR => {
            document.getElementById('error').innerText = "You dont have Matriculation No. Please contact administrator"+jqXHR;
        });
}

/**
 * Public interface
 * */
export default {
    /**
     * get a user
     * @returns user or null
     */
    getUser: function() {
        return data.user ? data.user: null;
    },

    /**
     * Get the user id
     * @returns personal-id of user
     */
    getUserId: function () {
        return data.user.personal_id;
    },

    /**
     * Set user
     * @param person the user to be set
     */
    setUser: function (person) {
        data.user = person;
        if (person.role === 'student'){
            setStudent(person.personal_id);
        }
    },

    /**
     * Log out empties data object
     * @returns empty data-object
     */
    logout: function() {
        return data = {};
    },

    /**
     * get role of user
     * @returns user data or null
     */
    getUserRole: function () {
        return data.user? data.user.role: null;
    },

    /**
     * set matriculation number
     * @param matriculation number
     */
    setMatNo(matNo) {
        data.student.matriculation_number = matNo;
    },

    /**
     * get matriculation number
     * @returns matriculation number
     */
    getMatNo() {
        return data.student.matriculation_number;
    },

    /**
     * set module-id
     * @param module-id
     */
    setModuleId: function(moduleId) {
        data.moduleId = moduleId;
    },

    /**
     * set modulename
     * @param module name
     */
    setModuleName: function(moduleName) {
        data.moduleName = moduleName;
    },

    /**
     * set module number
     * @param moduleNumber
     */
    setModuleNumber: function(moduleNumber) {
        data.moduleName = moduleNumber;
    },

    /**
     * get module-id
     * @returns module-id
     */
    getModuleId: function() {
        return data.moduleId;
    },

    /**
     * get module name
     * @returns module name
     */
    getModuleName: function() {
        return data.moduleName;
    },

    /**
     * get module number
     * @returns module number
     */
    getModuleNumber: function() {
        return data.moduleNumber;
    },

    /**
     * get student id
     * @returns student id
     */
    getStudentId: function () {
        return data.student.student_id;
    }
}