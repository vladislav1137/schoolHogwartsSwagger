-- liquibase formatted sql
-- changeset vnovozhilov:1
create index student_name on student(name);
--changeset vnovozhilov:2
create index faculty_name_color on faculty(name,color);
