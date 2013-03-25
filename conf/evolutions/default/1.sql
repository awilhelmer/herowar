# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table map (
  name                      varchar(255),
  team_size                 integer)
;

create table user (
  username                  varchar(255),
  password                  varchar(255))
;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists map;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

