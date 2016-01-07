CREATE TABLE HR.EVENTS  (	
 EVENT_ID NUMBER(6,0), 
 EVENT_DATE DATE, 
 EVENT_TITLE VARCHAR2(256), 
 CONSTRAINT EVENT_ID_PK PRIMARY KEY (EVENT_ID)
  ) 
  
  CREATE SEQUENCE HR.events_seq;
  
select * from hr.events;

CREATE TABLE HR.Persons  (	
 PERSON_ID NUMBER(6,0), 
 AGE NUMBER, 
 FIRST_NAME VARCHAR2(256), 
 LAST_NAME VARCHAR2(256),
 CONSTRAINT PERSONS_ID_PK PRIMARY KEY (PERSON_ID)
  ) 