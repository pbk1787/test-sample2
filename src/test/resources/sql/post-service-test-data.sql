insert into users (id, email, nickname, address, certification_code, status, last_login_at)
values (1, 'test@test.com', 'test', 'Seoul', 'aaaaa-aaaa-aaaaa', 'ACTIVE', '0');

insert into users (id, email, nickname, address, certification_code, status, last_login_at)
values (2, 'test2@test.com', 'test2', 'Seoul', 'bbbbb-bbbb-bbbb', 'PENDING', '0');

insert into posts (content, created_at, modified_at, user_id)
values ('helloworld', 16783067398, 0, 1);