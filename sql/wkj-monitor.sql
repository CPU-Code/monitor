create table if not exists tb_admin
(
	id int auto_increment
		primary key,
	login_name varchar(50) null comment '登录名',
	password varchar(60) null comment '密码',
	type tinyint null comment '类型 1超级管理员 0普通用户',
	board varchar(50) null comment '看板'
);

create table if not exists tb_alarm
(
	id int auto_increment comment 'id'
		primary key,
	name varchar(50) null comment '报警名称',
	quota_id int null comment '指标id',
	operator varchar(10) null comment '运算符',
	threshold int null comment '报警阈值',
	level int null comment '报警级别  1一般 2严重',
	cycle int null comment '沉默周期（分钟）',
	webhook varchar(1000) null comment 'web钩子',
	constraint tb_alarm_name_uindex
		unique (name)
);

create table if not exists tb_board
(
	id int auto_increment comment 'id'
		primary key,
	admin_id int default 1 null comment '管理员id',
	name varchar(50) null comment '看板名称',
	quota varchar(100) default '0' null comment '指标(趋势时设置)',
	device varchar(100) null comment '设备(累计)',
	`system` tinyint default 0 null comment '是否是系统看板',
	disable tinyint default 0 null comment '是否不显示',
	constraint tb_board_name_uindex
		unique (name)
);


create table if not exists tb_gps
(
	id int not null comment 'id'
		primary key,
	subject varchar(50) null comment '主题',
	sn_key varchar(50) null comment '设备识别码字段',
	single_field tinyint null comment '类型（单字段、双字段）',
	value_key varchar(50) null comment '经纬度字段',
	separation varchar(10) null comment '经纬度分隔符',
	longitude varchar(20) null comment '经度字段',
	latitude varchar(20) null comment '维度字段',
	constraint tb_gps_subject_uindex
		unique (subject)
);

create table if not exists tb_quota
(
	id int auto_increment comment 'id'
		primary key,
	name varchar(50) null comment '指标名称',
	unit varchar(20) null comment '指标单位',
	subject varchar(50) null comment '报文主题',
	value_key varchar(50) null comment '指标值字段',
	sn_key varchar(50) null comment '设备识别码字段',
	webhook varchar(1000) null comment 'web钩子',
	value_type varchar(10) null comment '指标字段类型，Double、Inteter、Boolean',
	reference_value varchar(100) null comment '参考值',
	constraint tb_quota_name_uindex
		unique (name)
);