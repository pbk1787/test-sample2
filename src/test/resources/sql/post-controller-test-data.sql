insert into users (id, email, nickname, address, certification_code, status, last_login_at)
values (1, 'test@test.com', 'test', 'Seoul', 'aaaaa-aaaa-aaaaa', 'ACTIVE', '0');

insert into posts (id, content, created_at, modified_at, user_id)
values (1, 'helloworld', 16783067398, 16783067398, 1)