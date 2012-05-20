# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table comment (
  id                        bigint not null,
  author                    varchar(255),
  posted_at                 timestamp,
  content                   clob,
  POST_ID                   bigint,
  constraint pk_comment primary key (id))
;

create table post (
  POST_ID                   bigint not null,
  title                     varchar(255),
  posted_at                 timestamp,
  content                   clob,
  author_id                 bigint,
  constraint pk_post primary key (POST_ID))
;

create table tag (
  NAME                      varchar(255) not null,
  constraint pk_tag primary key (NAME))
;

create table user (
  id                        bigint not null,
  email                     varchar(255),
  fullname                  varchar(255),
  password                  varchar(255),
  is_admin                  boolean,
  constraint pk_user primary key (id))
;


create table TAGGED_POSTS (
  post_POST_ID                   bigint not null,
  tag_NAME                       varchar(255) not null,
  constraint pk_TAGGED_POSTS primary key (post_POST_ID, tag_NAME))
;
create sequence comment_seq;

create sequence post_seq;

create sequence tag_seq;

create sequence user_seq;

alter table comment add constraint fk_comment_post_1 foreign key (POST_ID) references post (POST_ID) on delete restrict on update restrict;
create index ix_comment_post_1 on comment (POST_ID);
alter table post add constraint fk_post_author_2 foreign key (author_id) references user (id) on delete restrict on update restrict;
create index ix_post_author_2 on post (author_id);



alter table TAGGED_POSTS add constraint fk_TAGGED_POSTS_post_01 foreign key (post_POST_ID) references post (POST_ID) on delete restrict on update restrict;

alter table TAGGED_POSTS add constraint fk_TAGGED_POSTS_tag_02 foreign key (tag_NAME) references tag (NAME) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists comment;

drop table if exists post;

drop table if exists TAGGED_POSTS;

drop table if exists tag;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists comment_seq;

drop sequence if exists post_seq;

drop sequence if exists tag_seq;

drop sequence if exists user_seq;

