# create DB, should be done by support, needed only for local run
create database jasperDB;
# grant rights (used mysqladministrator)

# creating appropriate tables
create table pcall_recs


CREATE TABLE pcall_recs (
 id INTEGER  NOT NULL AUTO_INCREMENT,
 phone_number VARCHAR(13)  NOT NULL,
 des_phone_number varchar(13)  NOT NULL,
 caller_name varchar(50)  NOT NULL,
 call_start DATETIME  NOT NULL,
 call_end datetime  NOT NULL,
 PRIMARY KEY (id)
)
ENGINE = MyISAM;