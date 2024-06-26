# 数据库初始化
# @author <a href="https://github.com/liyupi">程序员鱼皮</a>
# @from <a href="https://yupi.icu">编程导航知识星球</a>

-- 创建库
create database if not exists oj;

-- 切换库
use oj;

-- 用户表
create table if not exists user (
    id bigint auto_increment comment 'id' primary key,
    userAccount varchar(256) not null comment '账号',
    userPassword varchar(512) not null comment '密码',
    unionId varchar(256) null comment '微信开放平台id',
    mpOpenId varchar(256) null comment '公众号openId',
    userName varchar(256) null comment '用户昵称',
    userAvatar varchar(1024) null comment '用户头像',
    userProfile varchar(512) null comment '用户简介',
    userRole varchar(256) default 'user' not null comment '用户角色：user/admin/ban',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete tinyint default 0 not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 題目表
create table if not exists question (
    id bigint auto_increment comment 'id' primary key,
    title varchar(512) null comment '标题',
    content text null comment '内容',
    tags varchar(1024) null comment '标签列表（json 数组）',
    answer TEXT NULL COMMENT '答案题解',
    submitNum int default 0 not null comment '提交人数',
    acceptedNum int default 0 not null comment '通过人数',
    judgeCase TEXT NULL COMMENT '判题用例（json 数组）',
    judgeConfig TEXT NULL COMMENT '判题配置（json 对象）',
    userId bigint not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete tinyint default 0 not null comment '是否删除',
    index idx_userId (userId)
) comment '题目' collate = utf8mb4_unicode_ci;

-- 题目提交表
create table if not exists question_submit (
    id bigint auto_increment comment 'id' primary key,
    postId bigint not null comment '题目 id',
    userId bigint not null comment '提交用户 id',
    language VARCHAR(128) NOT NULL COMMENT '题目语言',
    code TEXT NOT NULL COMMENT '提交代码',
    judgeInfo TEXT NULL COMMENT '判题信息（json 对象）',
    status int DEFAULT 0 NOT NULL COMMENT '判题状态 0-待判题 1-判题中 2-成功 3-失败',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞';

-- 帖子收藏表（硬删除）
create table if not exists post_favour (
    id bigint auto_increment comment 'id' primary key,
    postId bigint not null comment '帖子 id',
    userId bigint not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子收藏';