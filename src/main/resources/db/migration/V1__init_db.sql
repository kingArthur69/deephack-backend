drop table if exists roles;
drop table if exists user_roles;
drop table if exists users;
drop table if exists meters;
drop table if exists input_data;

create table roles
(
    role_id bigint not null auto_increment,
    name    varchar(255),
    primary key (role_id)
);

create table user_roles
(
    user_id bigint not null,
    role_id bigint not null
);

create table users
(
    user_id       bigint not null auto_increment,
    IDNP      varchar(255),
    name      varchar(255),
    surname       varchar(255),
    password      varchar(255),
    enabled       bit,
    token_expired bit,
    primary key (user_id)
);

alter table user_roles
    add constraint FKh8ciramu9cc9q3qcqiv4ue8a6
        foreign key (role_id)
            references roles (role_id);


alter table user_roles
    add constraint FKhfh9dx7w3ubf1co1vdev94g3f
        foreign key (user_id)
            references users (user_id);