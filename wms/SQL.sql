---------------------------
-- 어드민 표
---------------------------
create table admin(
	admin_id int primary key auto_increment,
	id varchar(20),
	pwd varchar(20),
	name varchar(20),
	email varchar(20));

---------------------------
-- 최상위 카테고리
---------------------------
CREATE TABLE topcategory(
	topcategory_id int PRIMARY KEY auto_increment,
	top_name varchar(20)
);

---------------------------
-- 하위 카테고리
---------------------------
create table subcategory(
	subcategory_id int primary key auto_increment,
	sub_name varchar(20),
	topcategory_id int,
	constraint fk_topcategory_subcategory foreign key(topcategory_id)
	references topcategory(topcategory_id)
);

insert into topcategory(top_name) values('상의');
insert into topcategory(top_name) values('하의');
insert into topcategory(top_name) values('신발');
insert into topcategory(top_name) values('액세서리');

insert into subcategory(sub_name, topcategory_id) values('티셔츠', 1);
insert into subcategory(sub_name, topcategory_id) values('가디건', 1);
insert into subcategory(sub_name, topcategory_id) values('점퍼', 1);
insert into subcategory(sub_name, topcategory_id) values('셔츠', 1);

insert into subcategory(sub_name, topcategory_id) values('치마', 2);
insert into subcategory(sub_name, topcategory_id) values('반바지', 2);
insert into subcategory(sub_name, topcategory_id) values('청바지', 2);
insert into subcategory(sub_name, topcategory_id) values('면바지', 2);

insert into subcategory(sub_name, topcategory_id) values('운동화', 3);
insert into subcategory(sub_name, topcategory_id) values('구두', 3);
insert into subcategory(sub_name, topcategory_id) values('슬리퍼', 3);
insert into subcategory(sub_name, topcategory_id) values('샌들', 3);

insert into subcategory(sub_name, topcategory_id) values('반지', 4);
insert into subcategory(sub_name, topcategory_id) values('목걸이', 4);
insert into subcategory(sub_name, topcategory_id) values('팔지', 4);
insert into subcategory(sub_name, topcategory_id) values('귀걸이', 4);

---------------------------
-- 색상 표
---------------------------
create table color(
	color_id int primary key auto_increment,
	color_name varchar(15));

---------------------------
-- 사이즈 표
---------------------------
create table size(
	size_id int primary key auto_increment,
	size varchar(10));

---------------------------
-- 상품 테이블 
---------------------------
create table product(
	product_id int primary key auto_increment
	, product_name varchar(20)
	, brand varchar(15)
	, price int default 0
	, discount int default 0
	, introduce varchar(250)
	, detail text
	, subcategory_id int
	, constraint fk_subcategory_product foreign key(subcategory_id)
	references subcategory(subcategory_id));

---------------------------
-- 상품 별 사이즈 정보
---------------------------
create table product_size(
	product_size_id int primary key auto_increment,
	product_id int,
	size_id int,
	constraint fk_product_product_size foreign key(product_id)
	references product(product_id),
	constraint fk_size_product_size foreign key(size_id)
	references size(size_id));

---------------------------
-- 상품 별 색상 정보
---------------------------
create table product_color(
	product_color_id int primary key auto_increment,
	product_id int,
	color_id int,
	constraint fk_product_product_color foreign key(product_id)
	references product(product_id),
	constraint fk_color_product_color foreign key(color_id)
	references color(color_id));

---------------------------
-- 상품 이미지 테이블
---------------------------
create table product_img(
	product_img_id int primary key auto_increment,
	filename varchar(20),
	product_id int,
	constraint fk_product_product_img foreign key(product_id)
	references product(product_id));

select * from topcategory;
select * from subcategory;
select * from product_color;
select * from color;
select * from size;
select * from product_size;

select * from subcategory inner join topcategory on subcategory.topcategory_id = topcategory.topcategory_id;

insert into topcategory(top_name) values('가방');

-- 각 상위 카테고리별 소속된 하위카테고리의 수

select top_name, COUNT(s.sub_name) from topcategory t left outer join subcategory s on t.topcategory_id = s.topcategory_id group by t.top_name;

select top_name, count(*) from topcategory group by top_name;

create table test(
	test_id int primary key auto_increment, 
	name varchar(20));

insert into test(name) values('scott');
insert into test(name) values('adams');
insert into test(name) values('allen');

-- 가장 최신에 들어간 pk 알아내는 방법
-- max는 잘못된 방법 왜? 내가 맥스를 조회하기전에 남이 만약에 인서트를 하면 값이 달라질수 있음
select max(test_id) from test;

-- 그래서 세션으로 조회
select LAST_INSERT_ID();

create table member(
	member_id int primary key auto_increment
	, id varchar(20)
	, pwd varchar(64)
	, name varchar(20)
	, email varchar(25)
	, regdate timestamp default now()
);


select product_id, product_name, brand, price, discount, introduce, detail, s.subcategory_id, sub_name from product p inner join subcategory s  on p.subcategory_id = s.subcategory_id  order by product_id desc limit 6;

-- product_size 테이블과 size 테이블을 조인하여 해당 상품의 사이즈를 출력
select * from product_size p inner join size s on p.size_id = s.size_id and p.product_id=6;

select * from product_color p inner join color c on p.color_id = c.color_id and p.product_id=6;

