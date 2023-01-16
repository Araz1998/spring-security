create table if not exists users (
    id bigserial primary key,
    username varchar(30) not null,
    password varchar(80) not null
);

create table if not exists roles (
    id serial primary key,
    name varchar(50) not null
);

create table IF NOT EXISTS users_roles (
    user_id  bigint not null references users(id),
    role_id int not null references roles(id),
    primary key (user_id, role_id)
);

create table IF NOT EXISTS users_photo (
    id serial primary key,
    user_id  bigint not null references users(id),
    image_source varchar(50) not null
);