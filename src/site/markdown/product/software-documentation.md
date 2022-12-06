# Project "Academia": Software Documentation

![Logo](..\images\baseline_school_black_48dp.png)

## Group 3



<div style="page-break-after:always"></div>



**Team Members**

| Name | Given Name | Sprint 1 Scrum Role | Sprint 2 Scrum Role | Sprint 3 Scrum Role |
|---|---|---|---|---|
| Herold | Nicole | Scrum master | Team member | Team member |
| Müller | Mac | Team member | Scrum master | Team member |
| Vogt | Rebecca Tabea | Team member | Team member | Scrum master |



**Revision History**

| Version   | Date          | Description           | Author        |
|-----------|---------------|-----------------------|---------------|
| 1.0       | Feb 18, 2021  | Template              | Eric Dubuis   |
| 2.0 | April 24, 2021 | Final Version | Nicole Herold, Rebecca Vogt, Mac Müller |



**Sample Users for testing**

For testing purposes, please use the following login data:

| Role   | Username          | Password           | 
|-----------|---------------|-----------------------|
| Administrator | admin | 12345 | 
| Professor | professor | 12345 | 
| Student | vogtr | 12345 | 

<div style="page-break-after: always"></div>

## Table of Content

1. **[Introduction](#heading--1)**

   1.1 [Requirements](#heading--1-1)\
   1.2 [Stakeholders](#heading--1-2)\
   1.3 [Constraints](#heading--1-3)

2. **[Context: Use Case Diagrams](#heading--2)**

   2.1 [Administrator manages modules](#heading--2-1)\
   2.2 [Professor manages module descriptions (in role of module coordinator)](#heading--2-2)\
   2.3 [Administrator manages modulerun](#heading--2-3)\
   2.4 [Student enrolls to modulerun](#heading--2-4)\
   2.5 [Administrator creates and manages students](#heading--2-5)\
   2.6 [Teacher adds/edits student grades](#heading--2-6)

3. **[Component View](#heading--3)**

   3.1 [Domain Model](#heading--3-1)\
   3.2 [Classdiagram with packages of Software Structure](#heading--3-2)

4. **[Dynamic View](#heading--4)**

5. **[Deployment View](#heading--5)**

6. **[Technical Details](#heading--6)**

   6.1 [Technologies used](#heading--6-1)\
   6.2 [Communication frontend-backend](#heading--6-2)\
   6.3 [Data storage](#heading--6-3)\
   6.4 [Security & Filters](#heading--6-4)\
   6.5 [Session handling](#heading--6-5)\
   6.6 [Handling of transactions](#heading--6-6)\
   6.7 [Testing](#heading--6-7)

7. **[Design Decisions](#heading--7)**

   7.1 [Database-Design](#heading--7-1)\
   7.2 [User Interface](#heading--7-2)

8. **[Miscellaneous](#heading--8)**

   8.1 [Outlook](#heading--8-1)


<div style="page-break-after: always"></div>

<div id="heading--1"/>

## 1. Introduction

The requirements are set according to the document _achievements_, which is created by the product owner.

<div id="heading--1-1"/>

### 1.1 Requirements

#### 1.1.1 Functional requirements

The software should provide the possibility to administer lectures and faculty members at Bern University of Applied Sciences (Berner Fachhochschule/BFH). It will provide an interface which enables different user groups to login and use different functionalities according to their role.
In terms of lectures there are two aspects explicitly mentioned in the application:
* Module: The template of a lecture of a certain type. It consists of a title, a modulenumber (which can contain letters and numbers) and a description. Each module has a coordinator.
* Modulerun: A lecture of a certain module. It contains a status (actively running / past), a semester (autumn or spring) as well as a year.

The user roles are defined as follows:
* Administrator: An employee at BFH who is authorized to manage modules and moduleruns as well as the students.
    * An administrator shall be able to add, edit and delete modules as well as moduleruns. Descriptions of modules are an exception though since
      only professors with the role of module coordinator are able to edit them.
    * An administrator shall be able to add, edit and delete users. So far this function is implemented only for users with the role _student_.

* Teacher/Professor: The terms _professor_ and _teacher_ are used interchangeably in this document. This role describes a member of the teaching staff at BFH. Only a teacher hold the position of module coordinator for a particular module.
    * A teacher shall be able to add and edit a description for a module in case he is a module coordinator.
    * A teacher can add and edit the grades for students who are enrolled to his moduleruns.

* Student: A student is an immatriculated faculty member. Each student has a unique immatriculation number.
    * A student shall be able to enroll to moduleruns.
    * A student shall be able to see his/her past and current moduleruns he/she inscribed to and his/her grades.

#### 1.1.2 Non-functional requirements

The application will implement a clear separation of concerns. We will focus on a multi-layer architecture (3-tier). The frontend will be realized with Javascript as a
Single-page application (SPA) by using the MVC-pattern.
For the backend Java Servlets will be implemented providing an API according to REST principles. The backend provides a connection to a Postgres-Database via JDBC.
In terms of security the authentication of users will be role- and data-based. There are serverside mechanisms to check for each request if a user is authorized to post or get data.
This is realized with specific filters. Besides the technical aspects an appealing design is also important to provide a good user experience. For any occuring error in the application
the user shall get a proper feedback.

<div id="heading--1-2"/>

### 1.2 Stakeholders

Stakeholders for the project are

* Users: The faculty members of the Bern University of Applied Sciences
    * Administrative staff
    * Professors
    * Students

* The Bern University of Applied Sciences as an organisation.

* Probably other Universities: An agreement on which tool to use is possible between different Universities. The application might also be marketed beyond Bern University of Applied Sciences.

<div id="heading--1-3"/>

### 1.3 Constraints

The Project has to be developed according to SCRUM principles. The timeframe to develop the project is limited; the team has all in all three sprints (8 weeks) and is newly assembled for this projects.
It will take time to get to know each other. It is feasible to develop only part of the whole application due to these constraints.


<div style="page-break-after: always"></div>
<div id="heading--2"/>

## 2. Context: Use Case Diagrams

<div id="heading--2-1"/>

### 2.1 Administrator manages modules

This Use case shows how an administrator can manage modules.

![UML Context Diagram](..\images\usecase_manage_modules.png)


| **Use Case:** Manage modules |
|----|
| **Description:** A user in the role of administrator should be able to manage the modules. This means he can create, edit and delete modules. Delete and edit is only possible when no lecture of this module (modulerun) is held at the moment. |
| **Actors:** Only a user in the role of administrator has the right to manage modules. |
| **Trigger:** Administrator logs in and clicks the module view. |
| **Precondition:** no precondition |
| **Main scenario:** 1. user logs in as administrator, 2. clicks on module view, 3. sees all modules, 4. has three options: - create new module --> new user input, - edit existing module --> input changes, - delete existing module --> no input, (edit and delete only works on modules which do not have an active modulerun at the moment), 5. user clicks „apply“, 6. new view with changes is displayed |
| **Error scenario:** Something went wrong. Changes can not be saved in database. No internet connection |
| **Result:** Modules are managed. Created, edited or deleted. |
| **Postcondition:** The changed modules are displayed in the module view. |

<div style="page-break-after:always"></div>
<div id="heading--2-2"/>

### 2.2 Professor manages module descriptions (in role of module coordinator)
This Use case shows a teacher managing module description as module coordinator.

![UML Context Diagram](..\images\usecase_module_description.png)


| **Use Case:** Manage module description |
|----|
| **Description:** A user in the role of teacher and module coordinator for a module should be able to manage the module description. This means he/she can edit the description. This is only possible when he/she is the module coordinator.
| **Actors:** Only a user in the role of teacher and module coordinator has the right to manage the module description. |
| **Trigger:** Teacher logs in and clicks edit description. |
| **Precondition:** teacher must be the module coordinator for the module. |
| **Main scenario:** 1. user logs in as teacher, 2. clicks on module view, 3. clicks on edit module description, 4. make changes in the module description 5. user clicks „save“, 6. new view with changes is displayed |
| **Error scenario:** Something went wrong. Changes can not be saved in database. No internet connection |
| **Result:** Module description is managed. |
| **Postcondition:** The changed module description is displayed in the module view. |

<div style="page-break-after:always"></div>
<div id="heading--2-3"/>

### 2.3 Administrator manages modulerun

![Use Case Diagram Students](..\images\usecase_admin_manage_modulesruns.png)

| **Use Case:** Administrator manage modulerun |
|----|
| **Description:** A user in the role of administrator module should be able to manage the modulerun. This means he/she can create, edit and delete moduleruns.
| **Actors:** Only a user in the role of administrator has the right to manage the moduleruns. |
| **Trigger:** Administrator logs in and clicks modulerun. |
| **Precondition:** No precondition. Only user have to be administrator |
| **Main scenario:** 1. user logs in as administrator, 2. clicks on moduleruns, 3. creates a new modulerun for an existing module, 4.selects the teacher for this modulerun 5. user clicks „save“, 6. new view with changes is displayed |
| **Error scenario:** Something went wrong. Changes can not be saved in database. No internet connection |
| **Result:** Modulerun is managed. |
| **Postcondition:** The changes are displayed in the modulerun view. |

<div style="page-break-after:always"></div>
<div id="heading--2-4"/>

### 2.4 Student enrolls to modulerun

![Use Case Diagram Students](..\images\usecase_student.png)


| **Use Case:** Enrollment to modulerun |
|----|
| **Description:** A student is able to enroll to a modulerun. |
| **Actors:** Student. |
| **Trigger:** A student would like to enroll. |
| **Precondition:** The enrollment must have been created by an administrator. |
| **Main scenario:** 1. A student logs into the platform, 2. The student chooses a modulerun or several moduleruns and enrolls to them 3. After the moduleruns is finished, the student is able to see his grade(s). |
| **Error scenario:** Student enrolls to a wrong moduleruns by accident. |
| **Result:** Student is enrolled to moduleruns. |
| **Postcondition:** The enrollment is displayed in section "My Modules" in students view after login. |

<div style="page-break-after:always"></div>
<div id="heading--2-5"/>

### 2.5 Administrator creates and manages students

![Use Case Diagram Students](..\images\usecase_manage_students.png)

| **Use Case:** Create and manage students |
|----|
| **Description:** A user with the role of administrator should be able to manage the students. This means he can create, edit and delete students. |
| **Actors:** Only a user with the role of administrator has the right so manage students. |
| **Trigger:** Administrator logs in and clicks the student view. |
| **Precondition:** no precondition. |
| **Main scenario:** 1. user logs in as administrator, 2. clicks on student view, 3. sees all students, 4. has three options: - create new student --> new input for student, - edit existing student --> input changes, - delete existing student --> no input, 5. user clicks „apply“, 6. new view with changes is displayed. |
| **Error scenario:** Something went wrong. Changes can not saved in database. No internet connection. |
| **Result:** Students are managed. Created, edited or deleted. |
| **Postcondition:** The changed students are displayed in student view for administrator. |

<div style="page-break-after:always"></div>
<div id="heading--2-6"/>

### 2.6 Teacher adds/edits student grades

![Use Case Diagram Students](..\images\usecase_teacher_manage_student_grade.png)

| **Use Case:** A teacher adds/edits student grades |
|----|
| **Description:** A user as teacher/professor should be able to add/edit the student grades. |
| **Actors:** Only a professor. |
| **Trigger:** Professor of the modulerun would like to assign a grade to a student. |
| **Precondition:** He/She has to be assigned as professor of the modulerun. |
| **Main scenario:** 1. user logs in as professor, 2. clicks on "My Module", 3. selects his/her module, 4. clicks on "show my students" 5. select a grade for a student |
| **Error scenario:** Something went wrong. Changes can not saved in database. No internet connection. |
| **Result:** Students can see their grades in "My Results".  |
| **Postcondition:** Notification will be shown as "DONE" after grade was updated.  |



<div style="page-break-after: always"></div>
<div id="heading--3"/>


## 3. Component View

<div id="heading--3-1"/>

### 3.1 Domain Model

The domain model shows the relationship between domains and gives the multiplicities. An administrator can change, add and delete both:
modules and moduleruns. Each module can implement multiple moduleruns. The module description can only be changed by the module coordinator.
A teacher can see the moduleruns and set the grades for his/her moduleruns.
Students can enroll to active moduleruns. A student can see his/her grades of each modulerun he/she is enrolled to.

![DomainModel](..\images\domain_model.png)


<div style="page-break-after: always"></div>
<div id="heading--3-2"/>

### 3.2 Class diagram with packages of software structure

This is a reduced class diagram containing the main packages. It also shows the division between model, view and control. Please note that not the entirety of
classes could be displayed - where necessary, the classes were reduced to provide a good overview.


![UML Package Diagram](..\images\classes_packages.png)


<div style="page-break-after: always"></div>
<div id="heading--4"/>

<div style="page-break-after: always"></div>

## 4. Dynamic View

This sequence diagram shows the POST request in the backend when a student enrolls to a module run. The filters are not shown in detail
as this would be too extensive for this illustration.


![UML System Sequence Diagram](..\images\sequence_diagram.png)

<div style="page-break-after: always"></div>
<div id="heading--5"/>


## 5. Deployment View

The deployment diagram shows the association via TCP/IP protocol
between 3 devices: user-computer, application-/web-server and database server.
The application server contains 2 main execution environments.
The frontend communicates with backend via ajax-call from jQuery library.
For production purposes, a Tomcat web-server and a PostgreSQL database are used.
For testing purposes, an embedded Jetty web server and the in-memory H2 database are used.

![UML Deployment Diagram](..\images\deployment_diagram.png)


<div style="page-break-after: always"></div>
<div id="heading--6"/>


## 6. Technical Details

<div id="heading--6-1"/>

### 6.1 Technologies used
For this project maven was used as build tool. During development we used a local Tomcat Server and pgAdmin tool for testing. The backend is written in Java and
the frontend in Javascript/jQuery with HTML and CSS. Furthermore we use a Postgres-database.

<div id="heading--6-2"/>

### 6.2 Communication frontend-backend
The communication between the frontend- and backend-components is realized via Ajax-requests. The class service.js is the central node of the frontend components
which sends Ajax-Requests via HTTP-methods to the backend. The HTTP-methods are defined according to REST-principles in the various servlets.
Servlets will receive and send data in JSON-format. The data is written into and received from a Postgres database via a repository for each servlet.
Database changes and requests are submitted via SQL-queries defined in the repository. JSON-data received by a servlet in the POST- and PUT-methods is then
converted into a java object via an object mapper factory (package util).

<div id="heading--6-3"/>

### 6.3 Data storage
Non-persistent data for intermediate use can be stored in the particular _persist_-method within a repository in the backend.
In the frontend, data.js persists user data of the running session for authentication.
Persistent data is stored in a Postgres-Database. For details refer to [7.1 Database-Design](#heading--7-1).

<div id="heading--6-4"/>

### 6.4 Security & Filters
Several filters can be found in the package _util_. They fulfill the following purpose:

* Filtering for potential security risks.
* Filtering for the correct data format.


* Logging filter: Used to display the status of a request/response to/from a servlet and repository.
* Media type filter: Checks whether POST- and PUT-requests contain the correct format in the body as well as in the accept-header (JSON).
  If the accept-header does not match, the filter will send a 406-status (_not acceptable_) in the response. If the content-type does not match,
  the filter will send a 415-status (_unsupported media type_) in the response.
* Sanitizing filter: Prevents the user from injecting code into input fields. POST- and PUT-requests are checked for characters defined in
  HTML_PATTERN. If one character is found, the filter will send a 406-status (_not acceptable_) in the response. A request wrapper is used to
  read the body of a request without altering the input stream.
* Authentication filter: Each request is filtered by checking the user credentials. User credentials are base64-encryptet in the request (via service.js) and decrypted in the filter.
  The credentials are then looked up in the postgres-database. If there is no matching dataset, the filter will send a 401-status (_unauthorized_) in the response.

Passwords are written in md5-encryption into the database to prevent potential readers from reading a plain text password. Each servlet checks in its own methods, if
a user/role is allowed to access data.

<div id="heading--6-5"/>

### 6.5 Session handling
An explicit session handling is not implemented, since Java Servlets are stateless. User data is saved for each session in the frontend-class data.js
and used for requests. The user data is set back when a user logs out or refreshes the browser.

<div id="heading--6-6"/>

### 6.6 Handling of transactions
For every transaction in a servlet a connection without auto-commit is created. The transaction is then executed in the repository. In case
an exception is thrown, this will be caught in the servlet. The servlet then sends a fitting status in the response and rolls back the transaction.
If no exception is thrown, the transaction will be commited by the servlet.

<div id="heading--6-7"/>

### 6.7 Testing
**Integration Tests:** 4 important repositories were tests with the help of integration tests: Enroll Repository, Module Repository,
Modulerun Repository and Person Repository. They were tested by finding, persisting, updating and deleting a particular dataset. Furthermore the corresponding servlets
were tested as well in the same manner.

**Class Tests:** The classes Enroll, Module, ModuleRun and Person were tested by creating an object of these classes and using their getter- and setter methods.

<div style="page-break-after: always"></div>
<div id="heading--7"/>

## 7. Design Decisions

<div id="heading--7-1"/>

### 7.1 Database-Design
![Mock-up for the Loginscreen](..\images\UML_DatabaseDesign.png)

The database contains 6 tables. The "teaching" table is a list of professors and module-runs,
because a module-run may have more than a professor. A "student" table extends a "person" table with a matriculation number as attribute.

<div style="page-break-after: always"></div>
<div id="heading--7-2"/>

### 7.2 User Interface

#### 7.2.1 Planned User Interface

We use a simplistic User Interface-design. It mainly consists of yellow- and gray-tones. For the logo we used Open Sans and Source Sans as fonts.

**Login Screen**

![Mock-up for the Loginscreen](..\images\layout-1.png)

<div style="page-break-after: always"></div>

**Students View**

![Mock-up for the Loginscreen](..\images\layout-2.png)

Students can choose between their homescreen, called "My modules", and an enrollment view where they are able to enroll to
additional modules. In the "My Modules"-view they are able to see past modules as well as the assigned grades.

![Mock-up for the Loginscreen](..\images\layout-3.png)

<div style="page-break-after: always"></div>

**Teachers View**

![Mock-up for the Loginscreen](..\images\layout-4.png)

Teachers are able to assign grades to Students within their respective module runs.

![Mock-up for the Loginscreen](..\images\layout-5.png)

Teachers with assigned role "module coordinator" are in addition to assigning grades able to change module
descriptions.

<div style="page-break-after: always"></div>

**Administrators View**

![Mock-up for the Loginscreen](..\images\layout-6.png)

Administrators are able to create new module runs.

<div style="page-break-after: always"></div>

**Design Guidelines**

![Mock-up for the Loginscreen](..\images\layout-7.png)

<div style="page-break-after: always"></div>

#### 7.2.2 Implemented User Interface

The User Interface underwent minor changes during the development process.

**Login Screen / Lougout Screen**

![Mock-up for the Loginscreen](..\images\login_logout.png)


**Students View**

![Mock-up for the student_view](..\images\student_view.png)

<div style="page-break-after: always"></div>

**Teachers View**

![Mock-up for the prof_view](..\images\prof_view.png)

![Mock-up for the prof_view](..\images\prof_view2.png)

<div style="page-break-after: always"></div>

**Administrators View**

![Mock-up for the admin_view](..\images\admin_view.png)

![Mock-up for the admin_view](..\images\admin_view2.png)

![Mock-up for the admin_view](..\images\admin_view3.png)

<div style="page-break-after: always"></div>
<div id="heading--8"/>

## 8. Miscellaneous

<div id="heading--8-1"/>

### 8.1 Outlook

There are many scenarios as to how to extend the application. Due to the limited timeframe given for this project, not all necessary features
were implemented. Possible additions include:

* The password can be changed by a user. So far only an initial password, set by an administrator, is given.
* The profile picture can be changed by a user. A placeholder is now used instead.
* An administrator is able to enter grades as well. It is fairly common for teaching staff to ask an administrative staff member for help with
  entering grades. It is also possible that for some reason grades have to be corrected later on.
* Users and administrators can be registered as well. So far only the registration of students by an administrator is implemented.
* Users can be set to _non-active_ by an administrator, for example: If a professor or a student takes a sabbatical semester.
* The main page after login stating _Welcome to Academia!_ can be configured by an administrator. It could then display the menu of the mensa, news about the
  university, etc.