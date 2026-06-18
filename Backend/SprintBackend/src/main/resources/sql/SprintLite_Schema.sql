-- =========================
-- ENUM TYPES
-- =========================

CREATE TYPE PRIORITY AS ENUM (
    'LOW',
    'MEDIUM',
    'HIGH',
    'CRITICAL'
    );

CREATE TYPE ENTITYTYPE AS ENUM (
    'PRODUCT',
    'FEATURE',
    'STORY',
    'TASK',
    'BUG',
    'SPRINT'
    );

CREATE TYPE SPRINTSTATUS AS ENUM (
    'PLANNED',
    'ACTIVE',
    'COMPLETED'
    );

CREATE TYPE STATUS AS ENUM (
    'OPEN',
    'IN_PROGRESS',
    'BLOCKED',
    'DONE',
    'CLOSED'
    );

-- =========================
-- TABLES
-- =========================

CREATE TABLE SPRINT (
                        ID integer PRIMARY KEY,
                        SPRINTNAME text,
                        SPRINTDURATION integer DEFAULT 15,
                        STATUS SPRINTSTATUS,
                        STARTDATE date,
                        ENDDATE date
);

CREATE TABLE USERS (
                       ID integer PRIMARY KEY,
                       USERNAME varchar,
                       ROLE varchar,
                       CREATEDAT timestamp,
                       CREATEDBY varchar,
                       UPDATEDAT timestamp,
                       UPDATEDBY varchar
);

CREATE TABLE PRODUCTTABLE (
                              PRODUCTID integer PRIMARY KEY,
                              PRODUCTNAME varchar UNIQUE,
                              DESCRIPTION text,
                              OWNERID integer,
                              CREATEDAT timestamp,
                              CREATEDBY varchar,
                              UPDATEDAT timestamp,
                              UPDATEDBY varchar
);

CREATE TABLE FEATURETABLE (
                              FEATUREID integer PRIMARY KEY,
                              TITLE varchar,
                              DESCRIPTION text,
                              PRODUCTID integer,
                              SPRINTID integer,
                              FEATURESTATUS STATUS,
                              PRIORITY PRIORITY,
                              ESTIMATEDSTORYPOINTS integer,
                              REMAININGSTORYPOINTS integer,
                              CREATEDAT timestamp,
                              CREATEDBY varchar,
                              UPDATEDAT timestamp,
                              UPDATEDBY varchar
);

CREATE TABLE STORYTABLE (
                            ID integer PRIMARY KEY,
                            TITLE varchar,
                            BODY text,
                            FEATUREID integer,
                            USERID integer,
                            STORYSTATUS STATUS,
                            PRIORITY PRIORITY,
                            SPRINTID integer,
                            STORYPOINTS integer,
                            CREATEDAT timestamp,
                            CREATEDBY varchar,
                            UPDATEDAT timestamp,
                            UPDATEDBY varchar
);

CREATE TABLE TASKTABLE (
                           ID integer PRIMARY KEY,
                           TITLE varchar,
                           BODY text,
                           USERID integer NOT NULL,
                           TASKSTATUS STATUS,
                           PRIORITY PRIORITY,
                           STORYID integer NOT NULL,
                           SPRINTID integer NOT NULL,
                           ORIGINALESTIMATEHOURS integer,
                           REMAININGESTIMATEHOURS integer,
                           CREATEDAT timestamp,
                           CREATEDBY varchar,
                           UPDATEDAT timestamp,
                           UPDATEDBY varchar
);

CREATE TABLE BUGS (
                      ID integer PRIMARY KEY,
                      BUGSTATUS STATUS,
                      PRIORITY PRIORITY,
                      DESCRIPTION text,
                      ASSIGNEDTO integer,
                      STORYID integer,
                      SPRINTID integer,
                      ORIGINALESTIMATEHOURS integer,
                      REMAININGESTIMATEHOURS integer,
                      REOPENCOUNT integer DEFAULT 0,
                      CREATEDAT timestamp,
                      CREATEDBY varchar,
                      UPDATEDAT timestamp,
                      UPDATEDBY varchar
);

CREATE TABLE ATTACHMENTS (
                             ID integer PRIMARY KEY,
                             FILENAME varchar,
                             FILEPATH varchar,
                             UPLOADEDBY integer,
                             CREATEDAT timestamp,
                             CREATEDBY varchar,
                             UPDATEDAT timestamp,
                             UPDATEDBY varchar
);

CREATE TABLE ATTACHMENTMAPPING (
                                   ID integer PRIMARY KEY,
                                   ATTACHMENTID integer,
                                   ENTITYTYPE ENTITYTYPE,
                                   ENTITYID integer
);

CREATE TABLE COMMENTS (
                          ID integer PRIMARY KEY,
                          COMMENT text,
                          ENTITYTYPE ENTITYTYPE,
                          ENTITYID integer,
                          CREATEDBY integer,
                          CREATEDAT timestamp,
                          UPDATEDBY integer,
                          UPDATEDAT timestamp
);

CREATE TABLE USERPRODUCTMAPPING (
                                    ID integer PRIMARY KEY,
                                    USERID integer NOT NULL,
                                    PRODUCTID integer NOT NULL
);

CREATE TABLE WORKLOG (
                         ID integer PRIMARY KEY,
                         TASKID integer,
                         BUGID integer,
                         USERID integer,
                         WORKDATE date,
                         HOURSSPENT decimal,
                         REMARKS text
);

CREATE TABLE DSUNOTES (
                        ID integer PRIMARY KEY,
                        NOTES text,
                        NOTESDATE date,
                        ENTITYTYPE ENTITYTYPE,
                        ENTITYID integer,
                        CREATEDAT timestamp,
                        CREATEDBY varchar,
                        UPDATEDAT timestamp,
                        UPDATEDBY varchar
);

-- =========================
-- FOREIGN KEYS
-- =========================

ALTER TABLE PRODUCTTABLE
    ADD FOREIGN KEY (OWNERID) REFERENCES USERS(ID);

ALTER TABLE FEATURETABLE
    ADD FOREIGN KEY (PRODUCTID) REFERENCES PRODUCTTABLE(PRODUCTID);

ALTER TABLE FEATURETABLE
    ADD FOREIGN KEY (SPRINTID) REFERENCES SPRINT(ID);

ALTER TABLE STORYTABLE
    ADD FOREIGN KEY (FEATUREID) REFERENCES FEATURETABLE(FEATUREID);

ALTER TABLE STORYTABLE
    ADD FOREIGN KEY (USERID) REFERENCES USERS(ID);

ALTER TABLE STORYTABLE
    ADD FOREIGN KEY (SPRINTID) REFERENCES SPRINT(ID);

ALTER TABLE TASKTABLE
    ADD FOREIGN KEY (USERID) REFERENCES USERS(ID);

ALTER TABLE TASKTABLE
    ADD FOREIGN KEY (STORYID) REFERENCES STORYTABLE(ID);

ALTER TABLE TASKTABLE
    ADD FOREIGN KEY (SPRINTID) REFERENCES SPRINT(ID);

ALTER TABLE BUGS
    ADD FOREIGN KEY (ASSIGNEDTO) REFERENCES USERS(ID);

ALTER TABLE BUGS
    ADD FOREIGN KEY (STORYID) REFERENCES STORYTABLE(ID);

ALTER TABLE BUGS
    ADD FOREIGN KEY (SPRINTID) REFERENCES SPRINT(ID);

ALTER TABLE ATTACHMENTS
    ADD FOREIGN KEY (UPLOADEDBY) REFERENCES USERS(ID);

ALTER TABLE ATTACHMENTMAPPING
    ADD FOREIGN KEY (ATTACHMENTID) REFERENCES ATTACHMENTS(ID);

ALTER TABLE COMMENTS
    ADD FOREIGN KEY (CREATEDBY) REFERENCES USERS(ID);

ALTER TABLE COMMENTS
    ADD FOREIGN KEY (UPDATEDBY) REFERENCES USERS(ID);

ALTER TABLE USERPRODUCTMAPPING
    ADD FOREIGN KEY (USERID) REFERENCES USERS(ID);

ALTER TABLE USERPRODUCTMAPPING
    ADD FOREIGN KEY (PRODUCTID) REFERENCES PRODUCTTABLE(PRODUCTID);

ALTER TABLE WORKLOG
    ADD FOREIGN KEY (USERID) REFERENCES USERS(ID);

ALTER TABLE WORKLOG
    ADD FOREIGN KEY (TASKID) REFERENCES TASKTABLE(ID);

ALTER TABLE WORKLOG
    ADD FOREIGN KEY (BUGID) REFERENCES BUGS(ID);