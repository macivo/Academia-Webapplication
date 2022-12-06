import data from './data.js';

/* Services for AJax Connection to API */
const BASE_URL = 'api/';

export default {

    /**
     * Get all modules
     * @returns all modules & response status
     */
    getModules: function () {
        let settings = {
            url: BASE_URL + 'modules',
            type: 'GET',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            },
        };
        console.log('Sending ' + settings.type + ' request to ' + settings.url);
        return $.ajax(settings);
    },

    /**
     * Post a new module
     * @param the module to be posted
     * @returns response status
     */
    postModule: function (module) {
        let settings = {
            url: BASE_URL + 'modules',
            type: 'POST',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            },
            contentType: 'application/json',
            data: JSON.stringify(module)
        };
        console.log('Sending ' + settings.type + ' request to ' + settings.url);
        return $.ajax(settings);
    },

    /**
     * Get a module with particular id
     * @param id of a module
     * @returns response status
     */
    getModule: function (id) {
        return $.ajax({
            url: BASE_URL + 'modules/' + id,
            type: 'GET',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            }
        }).fail(jqXHR => console.log("ERROR: " + jqXHR.status));
    },

    /**
     * Edit a module
     * @param moduledata
     * @param id of module to be updated
     * @returns response status
     */
    putModule: function (module, id) {
        return $.ajax({
            url: BASE_URL + 'modules/' + id,
            type:   'PUT',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            },
            data: JSON.stringify(module),
            contentType: 'application/json'
        }).fail(jqXHR => console.log("ERROR: " + jqXHR.status));
    },

    /**
     * Delete a module
     * @param id of module to be deleted
     * @returns response status
     */
    deleteModule: function (id) {
        return $.ajax({
            url: BASE_URL + 'modules/' + id,
            type: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            },
            data: JSON.stringify(id),
            contentType: 'application/json'
        }).fail(jqXHR => console.log("ERROR: " + jqXHR.status));
    },

    /**
     * Get all module runs
     * @returns all moduleruns & response status
     */
    getModuleRuns: function () {
        let settings = {
            url: BASE_URL + 'moduleruns',
            type: 'GET',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            },
        };
        console.log('Sending ' + settings.type + ' request to ' + settings.url);
        return $.ajax(settings);
    },

    /**
     * Post a modulerun
     * @param modulerun to be posted
     * @returns response status
     */
    postModuleRun: function (modulerun) {
        let settings = {
            url: BASE_URL + 'moduleruns',
            type: 'POST',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            },
            contentType: 'application/json',
            data: JSON.stringify(modulerun)
        };
        console.log('Sending ' + settings.type + ' request to ' + settings.url);
        return $.ajax(settings);
    },

    /**
     * Get a single modulerun
     * @param id of modulerun
     * @returns modulerun & response status
     */
    getModuleRun: function (id) {
        return $.ajax({
            url: BASE_URL + 'moduleruns/' + id,
            type: 'GET',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            }
        }).fail(jqXHR => console.log("ERROR: " + jqXHR.status));
    },

    /**
     * Edit modulerun
     * @param modulerun to be edited
     * @param id of modulerun to be edited
     * @returns response status
     */
    putModuleRun: function (modulerun, id) {
        return $.ajax({
            url: BASE_URL + 'moduleruns/'+ id,
            type:   'PUT',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            },
            data: JSON.stringify(modulerun),
            contentType: 'application/json'
        }).fail(jqXHR => console.log("ERROR: " + jqXHR.status));
    },

    /**
     * Delete modulerun
     * @param id of modulerun to be deleted
     * @returns response status
     */
    deleteModuleRun: function (id) {
        return $.ajax({
            url: BASE_URL + 'moduleruns/' + id,
            type: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            },
            data: JSON.stringify(id),
            contentType: 'application/json'
        }).fail(jqXHR => console.log("ERROR: " + jqXHR.status));
    },

    /**
     * Get all students
     * @returns response status
     */
    getStudents: function () {
        return $.ajax({
            url: BASE_URL + 'students',
            type: 'GET',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            }
        }).fail(jqXHR => console.log("ERROR: " + jqXHR.status));
    },

    /**
     * Get a particular student
     * @param id of student
     * @returns student & response status
     */
    getStudent: function (student_id) {
        return $.ajax({
            url: BASE_URL + 'students/' + student_id,
            type: 'GET',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            }
        }).fail(jqXHR => console.log("ERROR: " + jqXHR.status));
    },

    /**
     * Post a new student
     * @param student to be posted
     * @returns response status
     */
    postStudent: function (student) {
        let settings = {
            url: BASE_URL + 'students',
            type: 'POST',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            },
            contentType: 'application/json',
            data: JSON.stringify(student)
        };
        console.log('Sending ' + settings.type + ' request to ' + settings.url);
        return $.ajax(settings);
    },

    /**
     * Delete a student
     * @param id of student to be deleted
     * @returns response status
     */
    deleteStudent: function (id) {
        return $.ajax({
            url: BASE_URL + 'students/' + id,
            type: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            },
            data: JSON.stringify(id),
            contentType: 'application/json'
        }).fail(jqXHR => console.log("ERROR: " + jqXHR.status));
    },

    /**
     * Update student
     * @param student to be updated
     * @param id of student
     * @returns response status
     */
    putStudent: function (student, id) {
        return $.ajax({
            url: BASE_URL + 'students/'+id,
            type:   'PUT',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            },
            data: JSON.stringify(student),
            contentType: 'application/json'
        }).fail(jqXHR => console.log("ERROR: " + jqXHR.status));
    },

    /**
     * Used for login. Searches a person by username
     * @param username from login-form
     * @returns person & response status
     */
    getPersonByUsername: function (username, password) {
        return $.ajax({
            url: BASE_URL + 'persons/login/' + username,
            type: 'GET',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(username + ':' + password)
            }
        }).fail(jqXHR => console.log("ERROR: " + jqXHR.status));
    },

    /**
     * Get all professors
     * @returns all professors & response status
     */
    getProfs: function () {
        let settings = {
            url: BASE_URL + 'persons/'+ 'profs',
            type: 'GET',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            },
        };
        console.log('Sending ' + settings.type + ' request to ' + settings.url);
        return $.ajax(settings);
    },

    /**
     * Get all moduleruns for a particular professor
     * @returns all moduleruns for a professor & response status
     */
    getModuleRunForProf:  function () {
        let settings = {
            url: BASE_URL + 'teaching/',
            type: 'GET',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            },
        };
        console.log('Sending ' + settings.type + ' request to ' + settings.url);
        return $.ajax(settings);
    },

    /**
     * Get all professors for particular modulerun
     * @param id of modulerun
     * @returns all professors for a modulerun & response status
     */
    getProfsForModulerun: function (modrunId) {
        let settings = {
            url: BASE_URL + 'profteaching/' + modrunId,
            type: 'GET',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            },
        };
        console.log('Sending ' + settings.type + ' request to ' + settings.url);
        return $.ajax(settings);
    },

    /**
     * Get all students for a particular modulerun
     * @param id of modulerun
     * @returns all students for a modulerun & response status
     */
    getStudentsInRunningModule: function (module_run_id) {
        let settings = {
            async: false,
            url: BASE_URL + 'studentsmodule/' + module_run_id,
            type: 'GET',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            },
        };
        console.log('Sending ' + settings.type + ' request to ' + settings.url);
        return $.ajax(settings);
    },

    /**
     * Get all enrollments for a particular modulerun
     * @param id of modulerun
     * @returns all enrollments for a modulerun & response status
     */
    getEnrollmentByModuleRunId: function (moduleRunId) {
        return $.ajax({
            async: false,
            url: BASE_URL + 'enrollments/module/'+ moduleRunId,
            type: 'GET',
            headers: {
                'Accept': 'application/json' ,
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            }
        }).fail(jqXHR => console.log("ERROR: " + jqXHR.status));
    },

    /**
     * Get all enrollments for particular student
     * @param id of student
     * @returns all enrollments for a student & response status
     */
    getEnrollmentByStudentId: function (studentId) {
        return $.ajax({
            url: BASE_URL + 'enrollments/student/'+ studentId,
            type: 'GET',
            headers: {
                'Accept': 'application/json' ,
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            }
        }).fail(jqXHR => console.log("ERROR: " + jqXHR.status));
    },

    /**
     * Set a garde for a student in an enrollment
     * @param enrollment
     * @returns response status
     */
    setStudentGrade(enrollment) {
        return $.ajax({
            url: BASE_URL + 'enrollments/'+ enrollment.id,
            type: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            },
            data: JSON.stringify(enrollment),
            contentType: 'application/json'
        }).fail(jqXHR => console.log("ERROR: " + jqXHR.status));
    },

    /**
     * Get all moduleruns of a student to which he/she is enrolled to
     * @param id of student
     * @returns all moduleruns a student is enroleld to & response status
     */
    getEnrolledModuleRunForStudent(studentId){
        let settings = {
        url: BASE_URL + 'studentEnroll/' + studentId,
        type: 'GET',
        headers: {
            'Accept': 'application/json',
            'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
        },
    };
    console.log('Sending ' + settings.type + ' request to ' + settings.url);
    return $.ajax(settings);
    },

    /**
     * Get all moduleruns for an enrollment
     * @returns all moduleruns for an enrollment & response status
     */
    getAllModuleRunsForEnrollment(){
        let settings = {
            url: BASE_URL + 'studentEnroll',
            type: 'GET',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            },
        };
        console.log('Sending ' + settings.type + ' request to ' + settings.url);
        return $.ajax(settings);
    },

    /**
     * Post an enrollment
     * @param enrollment
     * @returns response status
     */
    //post a enrollment for student
    enrollStudent: function (enroll) {
        let settings = {
            url: BASE_URL + 'enrollments',
            type: 'POST',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            },
            contentType: 'application/json',
            data: JSON.stringify(enroll)
        };
        console.log('Sending ' + settings.type + ' request to ' + settings.url);
        return $.ajax(settings);
    },

    /**
     * Create a teaching
     * @param teaching
     * @returns response status
     */
    createTeaching: function(teaching) {
        let settings = {
            url: BASE_URL + 'profteaching',
            type: 'POST',
            headers: {
                'Accept': 'application/json',
                'Authorization': 'Basic ' + btoa(data.getUser().username + ':' + data.getUser().password)
            },
            contentType: 'application/json',
            data: JSON.stringify(teaching)
        };
        console.log('Sending ' + settings.type + ' request to ' + settings.url);
        return $.ajax(settings);
    }
}