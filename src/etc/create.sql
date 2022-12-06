CREATE TABLE IF NOT EXISTS person
(
    id              bigint                                  NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    gender          character varying(255)                  NOT NULL,
    firstname       character varying(255)                  NOT NULL,
    lastname        character varying(255)                  NOT NULL,
    date_of_birth   date                                    NOT NULL,
    email           character varying(255)                  NOT NULL,
    username        character varying(255)                  NOT NULL,
    password        character varying(255)                  NOT NULL,
    role            character varying(255)                  NOT NULL,
    status          boolean                                 NOT NULL,
    CONSTRAINT person_pkey PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS student
(
    id              bigint                                  NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    matriculation_number character varying(255)             NOT NULL,
    personal_id     bigint                                  NOT NULL,
    CONSTRAINT student_pkey PRIMARY KEY (id),
    CONSTRAINT student_personal_id_fkey FOREIGN KEY (personal_id)
        REFERENCES person (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE TABLE IF NOT EXISTS module
(
    id              bigint                                  NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    module_number   character varying(255)                  NOT NULL,
    name            character varying(255)                  NOT NULL,
    description     character varying(1028),
    module_coordinator bigint                               NOT NULL,
    ects            integer,
    CONSTRAINT module_pkey PRIMARY KEY (id),
    CONSTRAINT module_module_coordinator_fkey FOREIGN KEY (module_coordinator)
        REFERENCES person (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE TABLE IF NOT EXISTS module_run
(
    id              bigint                                  NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    module_id       bigint                                  NOT NULL,
    semester        character varying                       NOT NULL,
    year            integer                                 NOT NULL,
    running         boolean                                 NOT NULL,
    CONSTRAINT module_run_pkey PRIMARY KEY (id),
    CONSTRAINT module_run_module_id_fkey FOREIGN KEY (module_id)
        REFERENCES module (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE TABLE IF NOT EXISTS enrollment
(
    id              bigint                                  NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    module_run_id   bigint                                  NOT NULL,
    student_id      bigint                                  NOT NULL,
    grade           character varying(255)                  NOT NULL,
    CONSTRAINT enrollment_pkey PRIMARY KEY (id),
    CONSTRAINT enrollment_module_run_id_fkey FOREIGN KEY (module_run_id)
        REFERENCES module_run (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,

    CONSTRAINT enrollment_student_id_fkey FOREIGN KEY (student_id)
        REFERENCES student (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);



CREATE TABLE IF NOT EXISTS teaching
(
    id              bigint                                  NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    module_run_id   bigint                                  NOT NULL,
    personal_id     bigint                                  NOT NULL,
    CONSTRAINT teaching_pkey PRIMARY KEY (id),
    CONSTRAINT teaching_module_run_id_fkey FOREIGN KEY (module_run_id)
        REFERENCES module_run (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,

    CONSTRAINT teaching_personal_id_fkey FOREIGN KEY (personal_id)
        REFERENCES person (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
