-- Creation of all the tables
 
CREATE EXTENSION pgcrypto;

create table program(
   id serial primary key,
   name varchar(10) not null,
   year smallint not null,
   UNIQUE(name, year)
);
 
create table categories(
   id serial primary key,
   description varchar not null UNIQUE
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
   content bytea not null,
   fiche bytea,
   timestamp_fiche timestamp,
   report bytea,
   timestamp_report timestamp,
   slides bytea,
   timestamp_slides timestamp,
   supervisor_id int, foreign key (supervisor_id) references person(id) on delete cascade,
   scientific_validated boolean not null,
   administr_validated boolean not null,
   is_taken boolean not null,
   program_id int, foreign key (program_id) references program(id),
   confidential_internship boolean not null DEFAULT false
);
 
create table internship_category(
   id serial primary key,
   internship_id int, foreign key (internship_id) references internship(id) on delete cascade,
   category_id int, foreign key (category_id) references categories(id) on delete cascade
);
 
create table role_type(
   id serial primary key,
   role varchar(32)
);

create table defense(
   id serial primary key,
   date date,-- not null,
   time time,-- not null,
   referent_id int, foreign key (referent_id) references person(id) on delete cascade,
   jury2_id int, foreign key (jury2_id) references person(id) on delete cascade,
   student_id int, foreign key (student_id) references person(id) on delete cascade
);
 
create table person_roles(
   id serial primary key,
   role_id int, foreign key (role_id) references role_type(id),
   person_id int, foreign key (person_id) references person(id) on delete cascade
);
 
create table error(
   id serial primary key,
   date date not null,
   time timestamp not null,
   method_raised varchar(128) not null,
   description text,
   person_id int, foreign key (person_id) references person(id) on delete cascade
);
 
create table person_internship(
   id serial primary key,
   internship_id int, foreign key (internship_id) references internship(id),
   person_id int unique, foreign key (person_id) references person(id) on delete cascade
);
 
create table person_program(
   id serial primary key,
   program_id int, foreign key (program_id) references program(id),
   person_id int, foreign key (person_id) references person(id) on delete cascade
);
 
create table program_category(
   id serial primary key,
   program_id int, foreign key (program_id) references program(id) on delete cascade,
   cat_id int, foreign key (cat_id) references categories(id)
);
 
 
-- initialization of the database, i.e. insertion of the first admin
 
insert into role_type(role) values ('Student');
insert into role_type(role) values ('Assistant');
insert into role_type(role) values ('Professor');
insert into role_type(role) values ('Proponent');
insert into role_type(role) values ('Admin');
 
insert into person(name, email, creation_date, valid, password) values ('Charlie Root', 'charlie.root@example.org', current_date, 'true', crypt('charlie', gen_salt('bf')));

insert into person_roles(role_id, person_id) values (
  (select id from role_type where role = 'Admin'),
  (select id from person where email = 'charlie.root@example.org')
);

-- add rows to the database
 
insert into program(name, year) values ('inf', 2020);
insert into program(name, year) values ('map', 2020);
insert into program(name, year) values ('bio', 2020);
insert into program(name, year) values ('mat', 2020);
insert into program(name, year) values ('phys', 2020);

-- modifications to add (Sept. 26th 2022)
-- ALTER TABLE internship ADD COLUMN institution VARCHAR;
-- ALTER TABLE internship DROP CONSTRAINT internship_program_id_fkey;