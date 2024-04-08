create table person (
id serial primary key,
name varchar(30),
age integer,
license boolean
car_id integer references car(id));

create table car(
 id serial primary key,
 brand varchar(50),
 model varchar(100),
 price integer)