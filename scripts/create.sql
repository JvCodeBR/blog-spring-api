CREATE TABLE "user"(
	id INTEGER not NULL,
	nickname VARCHAR(50) not NULL,
	email VARCHAR(50) not null,
	"password" VARCHAR(100) not null,
	active INTEGER not null,
	PRIMARY KEY(id)
);

CREATE TABLE "role"(
	id INTEGER not NULL,
	"role" VARCHAR(20) not null,
	PRIMARY KEY(id)
);

CREATE TABLE "user_role"(
	user_id INTEGER not null,
	role_id INTEGER not null,
	foreign key	(user_id) references "user"(id),
	foreign key	(role_id) references "role"(id)
);

CREATE TABLE "post"(
	id INTEGER not NULL,
	user_id integer not NULL,
	post_date DATE not NULL,
	"content" VARCHAR(300) not null,
	PRIMARY KEY(id),
	foreign KEY(user_id) references "user"(id)
);

create sequence user_sequence start 1;
create sequence role_sequence start 1;
create sequence post_sequence start 1;

insert into "role" values (nextval('role_sequence'), 'ROLE_ADMIN');
insert into "role" values (nextval('role_sequence'), 'ROLE_CUSTOMER');

