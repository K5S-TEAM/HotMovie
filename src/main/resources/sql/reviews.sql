create table review (
    review_id bigint not null AUTO_INCREMENT,
    date_time datetime,
    description varchar(255),
    good_count integer not null,
    member_id bigint,
    score integer not null,
    movie_id bigint,
    primary key (review_id),
    FOREIGN KEY (movie_id) REFERENCES movie(movie_id)
) engine=InnoDB default charset = utf8mb4;

create table movie (
    movie_id bigint not null,
    average_score double precision not null,
    name varchar(255),
    num integer not null,
    sum_score double precision not null,
    primary key (movie_id)
) engine=InnoDB default charset = utf8mb4;


create table member (
    member_id bigint not null AUTO_INCREMENT,
    birth_date date,
    email varchar(255),
    primary key (member_id)
) engine=InnoDB default charset = utf8mb4;

create table hibernate_sequence (
       next_val bigint
)