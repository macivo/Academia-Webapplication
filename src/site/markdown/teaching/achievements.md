# Project "Academia": Achievements

Authors: Eric Dubuis, Stephan Fischli

<div style="page-break-after: always"></div>

## Revision History:

| Version   | Date          | Description                 | Author          |
| --------- | ------------- | --------------------------- | --------------- |
| 1.2       | Feb 18, 2021  | Updated Sprints, Criteria   | Eric Dubuis     |
| 1.1       | Feb 15, 2021  | Updated Technology Stack    | Stephan Fischli |
| 1.0       | Sep 14, 2020  | Initial version             | Eric Dubuis     |

<div style="page-break-after: always"></div>

## Content

**[1. Introduction](#heading--1)**

**[2. Requirements from the Perspective of Students](#heading--2)**

**[3. Technology Stack](#heading--3)**

**[4. Scrum Sprints](#heading--4)**

**[5. Evaluation Criteria](#heading--5)**

  * [5.1 Functional Requirements](#heading--5-1)
  * [5.2 Non-Functional Requirements](#heading--5-2)
  * [5.3 Process (Agile Project Management)](#heading--5-3)
  * [5.4 Software Documentation (English, Software Engineering)](#heading--5-4)
  * [5.5 Additional Language (English) Requirements](#heading--5-5)

**[6. First Week Tasks](#heading--6)**

**[7. Sprint Preparations](#heading--7)**

**[8. Software Documentation](#heading--8)**

**[9. Process Documentation](#heading--9)**

  * [9.1 Issue Lists](#heading--9-1)
    * [9.1.1 Labels](#heading--9-1-1)
    * [9.1.2 Issue Lists](#heading--9-1-1)
  * [9.2 Git Branches](#heading--9-2)

----

<div id="heading--1"/>

## 1. Introduction

During their second study year, BFH students of computer sciences
accomplish a software development project. Several goals shall be
achieved:

- They learn to work in teams.
- They learn to adhere given code quality rules.
- They learn to use the tools such as Git necessary to work in teams.
- They learn to organize their work on branches of the Git repository.
- They learn to use a predefined technology stack.
- They familiarize themselves with an automated build process.


<div id="heading--2"/>

## 2. Requirements from the Perspective of the Students

Students will have to:

- Organize themselves in teams.
- Perform the project in sprints.
- Deliver code.
- Deliver documentation (in English).
- Follow Scrum procedures.
- ...

An overall (imaginary) [vision](../product/vision.html) of
the product is given. An initial, coarse set of epics is
given in form of a product backlog in the initial
GitLab project repository.

Before each sprint, the following applies:

- The product owner may add new product backlog items into
  the product backlog.
- The Scrum team may add new product backlog items into
  the product backlog.
- The product owner priorities the product backlog items.
- The product owner and the Scrum team define the items
  that are _sprint ready_.
- The Scrum team estimates the efforts of sprint-ready
  items.
- _Sprint planning_: The product owner and the Scrum team
  define the items
  to part of the next sprint. These items will be moved
  into the _sprint backlog_ for next current sprint.
- The product owner and the Scrum team define the
  _definition of done_ for the sprint backlog items.

At the beginning of each sprint, the following applies:

- Items of estimates of more than 4 hours must be
  split into two or more _tasks_.
- New estimates are given for these tasks.
- All items are attributed with the label 'To Do'.
- ...

During each sprint:

- Each Scrum team member selects a task.
- For each task involving programming, a new _task branch_
  is created, and the task's label is attributed with
  the label 'Doing'.
- Finished items fulfilling the _definition of done_
  criteria are attributed with the label 'Done'.
- ... (more to follow)

At the end of each sprint, the following applies:

- ... (more to follow)

At the end of the project, the deliverable consists of:

- Running and tested code.
- DB-locking from the beginning of the third sprint.
- Responsiveness: optional.
- A complete software design document (text, UML context diagram,
  UML structure diagram, UML interaction diagram).

<div id="heading--3"/>

## 3. Technology Stack

The project must use the following technologies:

**Infrastructure**

  - Webserver: Tomcat 9
  - Database: PostgreSQL 13

**Client-side development**

  - UI: JavaScript with jQuery

**Server-side development**

  - REST interface: Java Servlets
  - Business logic: Java / Kotlin
  - Data Access: JDBC

For the use of other client or server technologies, a request must be made to the project management.

<div id="heading--4"/>

## 4. Scrum Sprints

There will be three Scrum sprints to perform. The sprints are time-boxed;
however, the length in weeks is not always the same. The following
table defines the sprints in terms of semester weeks:

| Semester Week     | Sprint, Duration  | Due date, Time        |
| :---------------: | :---------------: | --------------------- |
| 2 to 3            | 1, 2 weeks        | Mar 14, 2021, 23h59   |
| 4 to 6            | 2, 3 weeks (*)    | Apr  4, 2021, 23h59   |
| 2 to 3            | 3, 3 weeks        | Apr 25, 2021, 23h59    |

> __(*):__ Including the Holy Week

<div id="heading--5"/>

## 5. Evaluation Criteria

For each sprint, the following generic evaluation criteria
apply:

- Degree of the completion of the promises made for a
  sprint.
- The quality achieved for the deliverables of a
  sprint.

Feedback will be given during sprints and/or at the end of
each sprint. A single, overall qualification will be provided at
the end of the project. The exact list of criteria is:

<div id="heading--5-1"/>

### 5.1 Functional Requirements

The application shall support the following use cases:

- An administrator adds/edits/deletes modules *(not required for BTI3022 students)*
- An administrator adds/edits/deletes module runs *(not required for BTI3022 students)*
- A teacher adds/edits module description (as module coordinator)
- A teacher adds/edits student grades (as course teacher)
- A student enrolls to courses
- A student views his/her grades

<div id="heading--5-2"/>

### 5.2 Non-Functional Requirements

The following non-functional aspects must be taken into account:

**Server Subsystem**

- Multi-layer architecture implementing a clear separation of concerns
- Well-structured service interfaces according to REST principles
- Clear and efficient data model and O/R mapping
- User authentication and role-/data-based authorization
- Locking to avoid lost update problems

**Client Subsystem**

- Single-page application (plain JavaScript or framework)
- Proper usage of templates, components, routing, state management
- Appealing design, fully functional on all devices (aspects of RWD)
- Proper implementation of a multi-role user interface
- Complete and adequate exception handling and feedback to the user

<div id="heading--5-3"/>

### 5.3 Process (Agile Project Management)

The following criteria are evaluated for the software development process:

- Scrum roles assigned?
- Scrum procedure applied?
- Product Backlog: Displayed on the screen (projected via beam) or on the wall in paper form during the review in question
- Spint Backlog(s): Displayed on the screen (projected via beam) or on the wall in paper form during the review in question
- Impediment Backlog: Displayed on the screen (projected via beam) or on the wall in paper form during the review in question

<div id="heading--5-4"/>

### 5.4 Software Documentation (English, Software Engineering)

The following criteria are evaluated for the software documentation:

- Syntactically correct English text
- Grammatically correct English sentences
- Domain model with correct UML
- Data model / ERD
- Use case model with correct UML
- System sequence diagram for course enrollment
- UML diagram of choice for software structure
- UML deployment diagram

<div id="heading--5-5"/>

### 5.5 Additional Language (English) Requirements

The following organisational aspects are mandatory. Deductions will
be incurred for not including them.

  * Title page- include project title, group number,
    first and last names of all participants, date
  * Table of contents- optional, however, already
    available in the given template
  * Players- include a table showing the team member’s
    roles, responsibilities and contributions to the
    work executed for the sprint
  * Graphic considerations- fonts, colours, indenting,
    visualisations of coding / case studies / tests, etc.
  * Glossary- define and clarify jargon (specialist terms),
    acronyms or words that are used in an atypical way
  * Bibliography- (optional)
  * Submissions- documents should be in PDF format, entitle the PDF with team number, project name and sprint number, e.g. team 7_software_sprint 2, submit your document by the agreed upon date in the appropriate assignment folder on moodle.


Communicate the content with good writing skills:

  * The introduction is a text which mentions the
    objectives of the project, the purpose of the
    documentation and the targeted audience. 
  * Organisation should clearly indicate sections
    and sub-sections, (with accompanying numbers).
  * Text throughout which explains methods as well
    as justifications for choices made (why not other
    choices).
  * Clear explanations of the visuals, coding and charts
  * Language should have a consistent voice with a
    semi-formal (neutral) register.  A well-balanced
    text includes a reasonable amount of concise
    language techniques (relative clauses), linking words,
    and language which helps show clarification and
    precision where needed.  The goal of a sprint
    report is to guide the reader through the processes
    undertaken and justify the chosen methods with
    minimal repetition and without forfeiting clarity.
    Good writing skills also include grammar and
    vocabulary; sentence variety, word choice, word
    formation, spelling and punctuation are but a
    few examples to consider while writing about your
    projects.

<div id="heading--6"/>

## 6. First Week Tasks

The tasks for the first week (the week before sprint 1) are:

- Study the [Vision](../product/vision.html) document.
- Review given epics / user stories.
- Add new epics / user stories into the product backlog,
  if necessary.
- Refine epics / user stories.
- Provide estimates.
- Mock the UI for selected epics / user stories.
- Provide an initial domain model.
- Provide an initial design of the REST interface.

<div id="heading--7"/>

## 7. Sprint Preparations

From the beginning of the second week of the term, each
Scrum team will perform Scrum _sprints_. For each
sprint, the following generic rules apply:

The tasks for a sprint are:

- The product owner accepts the work done of the preceding sprint.
- The product owner prioritizes the items of the product backlog.
- The product owner and Scrum team refine the product backlog
  items if necessary.
- The Scrum team provides estimates for the topmost
  product backlog items.
- The product owner and the Scrum team define the set
  of product backlog items which are to be dealt with during
  the next sprint.

<div id="heading--8"/>

## 8. Software Documentation

During each sprint, the Scrum team expedite the documentation
of the software architecture / the software design. An [initial
template](../product/software-documentation.html) is given, whose content must be continuously
supplemented. This document must be written in English.

<div id="heading--9"/>

## 9. Process Documentation

The Scrum team follows (some of) the principles of Scrum. The
team will have to document its experiences. It will also have
to document the results of sprint reviews /
sprint retro-perspectives carried out.

<div id="heading--9-1"/>

### GitLab Issue Boards

You will manage the product backlog and the sprint backlog with the
help of
[GitLab _issue boards_](https://docs.gitlab.com/ee/user/project/issue_board.html).

<div id="heading--9-1-1"/>

#### Issue Lists

You will use the _Development_ issue board. It provides
the following labels for _issue lists_:

| Name / State      | Purpose                   |
| ----------------- | --------------------------|
| Open              | Will be used as product backlog.
| To Do             | Any item to be done during a particular sprint.
| Doing             | Any item which is currently be processed by a Scrum team member.
| Closed            | Will be used for closed sprint backlog items. Sprint backlog items must be labeled, see below.

For an issue being in the _Doing_ state must have the
followng attributes:

  * _Assignee_: A single team member responsible for this item.
  * _Estimated time_: The estimated amount of time in hours for this item.
  * _Effective time_: The effective time in hours spent to complete this item.


<div id="heading--9-1-2"/>

#### Labels

Any item to be processed during a particular sprint 1 to 3 must
be labeled accordingly:

| Label             | Sprint                    |
| ----------------- | --------------------------|
| Sprint 1          | Label to be used to tag any item being processed during sprint 1.
| Sprint 2          | Label to be used to tag any item being processed during sprint 2.
| Sprint 3          | Label to be used to tag any item being processed during sprint 3.

It is your task to attribute issue items with the correct labels.


<div id="heading--9-2"/>

### Git Branches

For each item in the issue list being in state of _Doing_,
the _assignee_ creates a new Git branch having as name the same
issue number as the item, and works on that branch
until completion of the item. When completed, the
_assignee_ merges the branch onto the Git master
branch.

The _assignee_ records the effective time spent
for completeing the item and changes the
state of the item to _Closed_.
