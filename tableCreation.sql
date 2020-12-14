-- Creation of all the tables
 
create table program(
   id serial primary key,
   name varchar(10) not null,
   year smallint not null
);
 
create table categories(
   id serial primary key,
   description varchar not null
);
 
create table person(
   id serial primary key,
   name varchar(128) not null,
   email varchar(128) not null,
   creation_date date not null,
   valid boolean not null,
   password text not null check (length(password) >= 8)
);
 
create table internship(
   id serial primary key,
   title varchar not null,
   creation_date date not null,
   content text not null,
   supervisor_id int, foreign key (supervisor_id) references person(id),
   scientific_validated boolean not null,
   administr_validated boolean not null,
   is_taken boolean not null,
   program_id int, foreign key (program_id) references program(id),
   pdf_file bytea not null
);
 
create table internship_category(
   id serial primary key,
   internship_id int, foreign key (internship_id) references internship(id),
   category_id int, foreign key (category_id) references categories(id)
);
 
create table role_type(
   id serial primary key,
   role varchar(32)
);
 
create table person_roles(
   id serial primary key,
   role_id int, foreign key (role_id) references role_type(id),
   person_id int, foreign key (person_id) references person(id)
);
 
create table error(
   id serial primary key,
   date date not null,
   time timestamp not null,
   method_raised varchar(128) not null,
   description text,
   person_id int, foreign key (person_id) references person(id)
);
 
create table person_internship(
   id serial primary key,
   internship_id int, foreign key (internship_id) references internship(id),
   person_id int unique, foreign key (person_id) references person(id)
);
 
create table person_program(
   id serial primary key,
   program_id int, foreign key (program_id) references program(id),
   person_id int, foreign key (person_id) references person(id)
);
 
create table program_category(
   id serial primary key,
   program_id int, foreign key (program_id) references program(id),
   cat_id int, foreign key (cat_id) references categories(id)
);
 
 
-- initialization of the database, i.e. insertion of the first admin
 
insert into role_type(role) values ('Student');
insert into role_type(role) values ('Assistant');
insert into role_type(role) values ('Professor');
insert into role_type(role) values ('Proponent');
insert into role_type(role) values ('Admin');
 
insert into person(name, email, creation_date, valid, password) values ('Manolescu, Ioana', 'ioana.manolescu@inria.fr', current_date, 'true', 'iloveDBMS');
 
insert into person_roles(role_id, person_id) values (5, 1);
 
-- add rows to the database
 
insert into program(name, year) values ('inf', 2020);
insert into program(name, year) values ('map', 2020);
insert into program(name, year) values ('bio', 2020);
insert into program(name, year) values ('mat', 2020);
insert into program(name, year) values ('phys', 2020);
<<<<<<< HEAD
 
copy person from 'webappdata/person.csv' CSV header;
copy internship from 'webappdata/internship.csv' CSV header;
copy categories from 'webappdata/categories.csv' CSV header;
copy internship_category from 'webappdata/internship_category.csv' CSV header;
copy person_roles from 'webappdata/person_roles.csv' CSV header;
copy person_program from 'webappdata/person_program.csv' CSV header;
copy program_category from 'webappdata/program_category.csv' CSV header;
 
 
=======

copy person from 'path/person.csv' CSV header;
copy internship from 'path/intership.csv' CSV header;
copy categories from 'path/categories.csv' CSV header;
copy internship_category from 'path/internship_category.csv' CSV header;
copy person_roles from 'path/person_roles.csv' CSV header;
copy person_program from 'path/person_program.csv' CSV header;
copy program_categories from 'path/program_category.csv' CSV header;


>>>>>>> bea1ce777da42c9cd85bcfec19ca565a8d14fec5
SELECT setval('person_id_seq', (SELECT MAX(id) FROM person)+1);
SELECT setval('internship_id_seq', (SELECT MAX(id) FROM internship)+1);
SELECT setval('categories_id_seq', (SELECT MAX(id) FROM categories)+1);
SELECT setval('internship_category_id_seq', (SELECT MAX(id) FROM internship_category)+1);
SELECT setval('person_roles_id_seq', (SELECT MAX(id) FROM person_roles)+1);
SELECT setval('person_program_id_seq', (SELECT MAX(id) FROM person_program)+1);
SELECT setval('program_category_id_seq', (SELECT MAX(id) FROM program_category)+1);