create user 'rsvpuser'@'localhost' identified by 'Password@123456';

grant ALL PRIVILEGES ON rsvpdb.* TO 'rsvpuser'@'localhost';

create table rsvp(
id int auto_increment not null primary key, 
name varchar(50),
email varchar(50),
phone varchar(50),
confirmation_date datetime,
comments longtext,
unique(email)
);

insert into rsvp (name, email, phone, confirmation_date, comments) values
('Tom M', 'Tom.M@example.com', '555-1234', '2023-03-17 10:00:00', 'Looking forward to the event!'),
('Nick Smith', 'nick.smith@example.com', '555-5678', '2023-03-17 10:00:00', 'Can\'t wait!'),
('Bob Johnson', 'bob.johnson@example.com', '555-9012', '2023-03-17 10:00:00', 'Not Sure'),
('Alice Lee', 'alice.lee@example.com', '555-3456', '2023-03-17 10:00:00', 'Sorry, I won\'t be able to make it.'),
('Samuel Jackson', 'samuel.jackson@example.com', '433-543', '2023-03-17 10:00:00', 'Excited to attend!');

insert into rsvp (name, email, phone, confirmation_date, comments) values
('Tom S', 'Tom.S@example.com', '555-1234', '2023-03-17 10:00:00', 'NOT looking forward to the event!');

# task 2
select * from rsvp;
select * from rsvp where name like "%tom%";

update rsvp set designation = "Assist-Manager" where employee_id=3;

select * from rsvp where email = 'alice.lee@example.com';

select count(*) as total_count from rsvp;