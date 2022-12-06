/*
 * Academia (c) 2021, Bern University of Applied Sciences, Switzerland
 * Group 3 - PT3 BTI3022
 * Developers: Nicole Herold, Rebecca Tabea Vogt, Mac Müller
 *
 * main.js
 * 	- List of JavaScript components imports
 * 	- List of router registrations
 *  - List of navigators
 */

import router from "./router.js";
import home from "./components/home.js";
import modules from "./components/modules.js";
import moduleCreate from './components/moduleCreate.js';
import moduleEdit from './components/moduleEdit.js';
import moduleDescriptionEdit from './components/moduleDescriptionEdit.js';
import modulesTeacher from './components/modulesTeacher.js';
import students from "./components/students.js";
import studentEdit from "./components/studentEdit.js";
import studentCreate from './components/studentCreate.js';
import studentsInModuleRun from './components/studentsInModuleRun.js';
import moduleruns from "./components/moduleruns.js";
import modulerunCreate from "./components/modulerunCreate.js";
import modulerunEdit from "./components/modulerunEdit.js";
import enrollment from "./components/enrollment.js";
import login from "./components/login.js";
import logout from "./components/logOut.js";
import results from "./components/results.js";


router.register('/home', home);
router.register('/students', students);
router.register('/modules', modules);
router.register('/moduleruns', moduleruns);
router.register('/moduleCreate', moduleCreate);
router.register('/moduleEdit', moduleEdit);
router.register('/moduleDescriptionEdit', moduleDescriptionEdit);
router.register('/modulesTeacher', modulesTeacher);
router.register('/modulerunEdit', modulerunEdit);
router.register('/modulerunCreate', modulerunCreate);
router.register('/studentsInModuleRun', studentsInModuleRun);
router.register('/studentEdit', studentEdit);
router.register('/studentCreate', studentCreate);
router.register('/enrollment', enrollment);
router.register('/login', login);
router.register('/results', results);
router.register('/logOut', logout);


/**
 * Page navigator
 */
$('#modules').click(function () { router.go('/modules');});
$('#moduleruns').click(function () { router.go('/moduleruns');});
$('#modulerunCreate').click(function () { router.go('/modulerunCreate');});
$('#modulerunEdit').click(function () { router.go('/modulerunEdit');});
$('#moduleCreate').click(function () { router.go('/moduleCreate');});
$('#moduleEdit').click(function () { router.go('/moduleEdit');});
$('#moduleDescriptionEdit').click(function () { router.go('/moduleDescriptionEdit');});
$('#teacherModuleruns').click(function () { router.go('/modulesTeacher');});
$('#students').click(function () { router.go('/students');});
$('#studentEdit').click(function () { router.go('/studentEdit');});
$('#studentCreate').click(function () { router.go('/studentCreate');});
$('#studentInModuleRun').click(function () { router.go('/studentsInModuleRun');});
$('#enrollment').click(function () { router.go('/enrollment');});
$('#results').click(function () { router.go('/results');});
$('#logout').click(function () { router.go('/logOut');});

/**
 * Home page
 */
router.go('/home');


