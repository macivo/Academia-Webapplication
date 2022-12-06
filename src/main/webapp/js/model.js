import service from './service.js';

let student = null;
let module = null;
let modules = null;
let moduleRun = null;
let moduleRuns = null;
let profs = null;
let studentsInRunningModule = null;
let enrolledModulesForStudent = null;
let enrollment = null;

export default {

    /**
     * create a new module
     * @returns the new module
     */
    createModule() {
        return new Module();
    },

    /**
     * Add a new module
     * @param the module to be posted
     * @returns response data
     */
    addModule: function (tempModule) {
        return service.postModule(tempModule)
            .then(moduleData => {
                module = moduleData;
                return;

            });
    },

    /**
     * Get all modules data
     * @returns  response data as promise
     */
    getModules: function () {
        return service.getModules()
            .then(modulesData => {
                modules = modulesData;
                return modules;
            });
    },
    /**
     * Get all data about a module
     * @param id - the id of the module
     * @returns response data as promise
     */
    getModule: function (id) {
        return service.getModule(id)
            .then(moduleData => {
                module = moduleData;
                return module;

            });
    },

    /**
     * Updates an existing module
     * @param tempModule - the module to be updated
     * @param id - the id of the module to be updated
     * @returns response data as promise
     */
    updateModule: function(tempModule, id) {
        return service.putModule(tempModule, id)
            .then(moduleData => {
                return moduleData;
            });
    },

/*    createModuleRun() {
        return new ModuleRun();
    },*/

    /**
     * Creates a new modulerun
     * @param moduleId - the module id
     * @param semester - the semester autumn or spring
     * @param year - the year
     * @param running - true if the modulerun is running otherwise false
     * @returns the new modulerun
     */
    createModuleRunWithParams(moduleId, semester, year, running) {
        return new ModuleRunPost(moduleId, semester, year, running);
    },

    /**
     * Add a new modulerun to the database
     * @param tempModuleRun - the modulerun to be added
     * @returns response data as promise
     */
    addModuleRun: function (tempModuleRun) {
        return service.postModuleRun(tempModuleRun)
            .then(moduleRunData => {
                moduleRun = moduleRunData;
                return moduleRun;
            });
    },

    /**
     * Get the modulerun id
     * @returns modulerun id
     */
    getModulerunId: function () {
        return moduleRun.id;
    },

    /**
     * Get a single modulerun by id
     * @param id - id of the modulerun
     * @returns response data as promise
     */
    getModuleRun: function (id) {
        return service.getModuleRun(id)
            .then(moduleRunData => {
                moduleRun = moduleRunData;
                return moduleRun;

            });
    },

    /**
     * Get moduleruns
     * @returns response data as promise
     */
    getModuleRuns: function () {
        return service.getModuleRuns()
            .then(moduleRuns => {
                moduleRuns = moduleRuns;
                return moduleRuns;

            });
    },

    /**
     * Updates an existing modulerun
     * @param tempModuleRun - the modulerun to be updated
     * @param id - the id of the modulerun
     * @returns response data as promise
     */
    updateModuleRun: function(tempModuleRun, id) {
        return service.putModuleRun(tempModuleRun, id)
            .then(moduleRunData => {
                return moduleRunData;
            });
    },

    /**
     * Creates a new student
     * @returns the new student
     */
    createStudent() {
        return new Student();
    },

    /**
     * Creates a new student with various information
     * @param student_id - the student id
     * @param gender - female, male or diverse
     * @param firstname - the first name of the student
     * @param lastname - the last name of the student
     * @param date_of_birth - the date of birth of the student
     * @param email - the email of the student
     * @param username - username of the student
     * @param password - password of the student
     * @param matriculation_number - the matriculation number of the student
     * @param role - student for students
     * @param status - active or inactive
     * @returns a student object
     */
    createStudentWithParam(student_id, gender, firstname, lastname, date_of_birth, email, username, password, matriculation_number, role, status) {
        return new Student(student_id, gender, firstname, lastname, date_of_birth, email, username, password, matriculation_number, role, status);
    },

    /**
     * Add a student to database
     * @param tempStudent - the new student to add
     * @returns response data as promise
     */
    addStudent: function (tempStudent) {
        tempStudent.password = md5(tempStudent.password);
        return service.postStudent(tempStudent)
            .then(studentData => {
                student = studentData;
                return;

            });
    },

    /**
     * Updates an existing student
     * @param studentUpdated -  the student to be updated
     * @param student_id - the student id
     * @returns response data as promise
     */
    updateStudent: function(studentUpdated, student_id) {
        return service.putStudent(studentUpdated, student_id)
            .then(studentData => {
                student = studentData;
                return;
            });
    },

    /**
     * Get all profs
     * @returns response data as promise
     */
    getProfs: function () {
        return service.getProfs()
            .then(profsData => {
                profs = profsData;
                return profs;
            });
    },

    /**
     * Get the moduleruns for a professor
     * @returns response data as promise
     */
    getModuleRunForProf: function () {
        return service.getModuleRunForProf()
            .then(moduleRunForProfData => {
                return moduleRunForProfData;
            });
    },

    /**
     * Get the professors for a modulerun
     * @param modrunId - the modulerun id
     * @returns response data as promise
     */
    getProfsForModulerun: function (modrunId) {
        return service.getProfsForModulerun(modrunId)
            .then(profsForModuleRun => {
                return profsForModuleRun;
            });
    },

    /**
     * Get all students in a modulerun
     * @param id - the id if the modulerun
     * @returns response data as promise
     */
    getStudentsInRunningModule: function (id) {
        return service.getStudentsInRunningModule(id)
            .then(studentsInRunningModuleData => {
                studentsInRunningModule = studentsInRunningModuleData;
                return studentsInRunningModuleData;
            });
    },

    /**
     * Get all enrollments for a student
     * @param id -  the student id
     * @returns response data as promise
     */
    getEnrolledModuleRunForStudent: function (id) {
        return service.getEnrolledModuleRunForStudent(id)
            .then(enrolledModulesForStudentData => {
                enrolledModulesForStudent = enrolledModulesForStudentData;
                return enrolledModulesForStudentData;
            });
    },

    /**
     * Get all moduleruns
     * @returns response data as promise
     */
    getAllModuleRunsForEnrollment: function () {
        return service.getAllModuleRunsForEnrollment()
            .then(getAllModuleRunsForEnrollmentData => {
                //moduleRuns = moduleRuns;
                return getAllModuleRunsForEnrollmentData;
            });
    },

    /**
     * Creates a new enrollment
     * @param studentId - the id of the student
     * @param moduleRunId - the modulerun id
     * @returns an enroll object
     */
    createEnrollment(studentId, moduleRunId) {
        return new Enroll(studentId, moduleRunId);
    },

    /**
     * Post an enrollment for a studnet
     * @param enroll - the enroll
     * @returns response data as promise
     */
    enrollStudent: function (enroll) {
        return service.enrollStudent(enroll)
            .then(enrollData => {
                enrollment = enrollData;
                return;
            });
    },

    /**
     * Creates a new teaching
     * @param modrunId - the modulrun id
     * @param personalId - the personal id
     * @returns a teaching object
     */
    createTeaching(modrunId, personalId) {
        return new Teaching(modrunId, personalId);
    },

    /**
     * Add a teaching to the database
     * @param teaching - the teaching to be added
     * @returns response data as promise
     */
    createProfTeaching: function(teaching) {
        return service.createTeaching(teaching)
            .then(teachingData => {
                teaching = teachingData;
                return;
            });
    }
}

/**
 * Constructor for creating a new student with various information
 * @param student_id - the student id
 * @param gender - female, male or diverse
 * @param firstname - the first name of the student
 * @param lastname - the last name of the student
 * @param date_of_birth - the date of birth of the student
 * @param email - the email of the student
 * @param username - username of the student
 * @param password - password of the student
 * @param matriculation_number - the matriculation number of the student
 * @param role student for students
 * @param status - active or inactive
 * @constructor
 */

    function Student(student_id, gender, firstname, lastname, date_of_birth, email, username, password, matriculation_number, role, status) {
    this.student_id = student_id;
    this.gender = gender;
    this.firstname = firstname;
    this.lastname = lastname;
    this.date_of_birth = date_of_birth;
    this.email = email;
    this.username = username;
    this.password = password;
    this.matriculation_number = matriculation_number;
    this.role = role;
    this.status = status;
}

/**
 * Constructor for creating a new module with various information
 * @param id - the module id
 * @param number - the number of the module
 * @param name - the name of the module
 * @param description - the module description
 * @param coordinator - the module coordinator
 * @param ects - the ects
 * @param running - false for the creation
 * @param firstname - the first name of the module coordinator
 * @param lastname - the last name of the module coordinator
 * @constructor
 */
function Module(id, number, name, description, coordinator, ects, running, firstname, lastname) {
    this.id = id;
    this.number = number;
    this.name = name;
    this.description = description;
    this.coordinator = coordinator;
    this.ects = ects;
    this.running = running || false;
    this.firstname = firstname;
    this.lastname = lastname;

}

/**
 * Constructor for creating a new modulerun with various information
 * @param id - the id of the modulerun
 * @param semester -  autumn or spring
 * @param year - the year
 * @param running - true if module is running otherwise false
 * @param moduleName - the name of the module
 * @param moduleNumber - the number of the module
 * @param moduleId - the module id
 * @constructor
 */
function ModuleRun(id, semester, year, running, moduleName, moduleNumber, moduleId) {
    this.id = id;
    this.semester = semester;
    this.year = year;
    this.running = running;
    this.moduleName = moduleName;
    this.moduleNumber = moduleNumber;
    this.moduleId = moduleId;
}

/**
 * Constructor for creating a new modulerun with various information
 * @param moduleId id - the id of the modulerun
 * @param semester -  autumn or spring
 * @param year - the year
 * @param running true if module is running otherwise false
 * @constructor
 */
function ModuleRunPost(moduleId, semester, year, running) {
    this.moduleId = moduleId;
    this.semester = semester;
    this.year = year;
    this.running = running;
}

/**
 * Constructor for creating a new person with various information
 * @param personal_id - the personal id
 * @param firstname - the first name
 * @param lastname - the last name
 * @constructor
 */
function Prof(personal_id, firstname, lastname) {
    this.personal_id = personal_id;
    this.firstname = firstname;
    this.lastname = lastname;
}

/**
 * Constructor for creating a new enroll with various information
 * @param studentId - the student id
 * @param moduleId - the modulerun id
 * @constructor
 */
function Enroll(studentId, moduleId) {
    this.student_id = studentId;
    this.module_run_id = moduleId;
    this.grade = '*';
}

/**
 * Constructor for creating a new teaching with various information
 * @param modrunId - the modulerun id
 * @param personalId - the personal id
 * @constructor
 */
function Teaching(modrunId, personalId) {
    this.modulerunId = modrunId;
    this.personalId = personalId;
}