# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table caller (
  id                        bigint auto_increment not null,
  call_from                 varchar(255),
  call_to                   varchar(255),
  days                      bigint,
  hours                     bigint,
  minutes                   bigint,
  seconds                   bigint,
  digits                    varchar(255) not null,
  constraint pk_caller primary key (id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table caller;

SET FOREIGN_KEY_CHECKS=1;

