/**
 * 树相关界面控件演示树数据
 */
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052583', '北京', 'treeroot');
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052597', '河北', 'treeroot');
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052629', '海淀区', '1355638052583');
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052643', '朝阳区', '1355638052583');
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052657', '大兴区', '1355638052583');
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052671', '通州区', '1355638052583');
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052685', '昌平区', '1355638052583');
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052699', '东城区', '1355638052583');
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052713', '西城区', '1355638052583');
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052727', '丰台区', '1355638052583');
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052744', '张家口', '1355638052597');
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052758', '邯郸', '1355638052597');
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052772', '唐山', '1355638052597');
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052786', '石家庄', '1355638052597');
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052800', '邢台', '1355638052597');
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052820', '怀来', '1355638052744');
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052834', '涿鹿', '1355638052744');
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052848', '阳原', '1355638052744');
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052862', '宣化', '1355638052744');
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052882', '沙城', '1355638052820');
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052896', '鸡鸣驿', '1355638052820');
INSERT DEMO_LEFT_TREE(sid, name, parentsid) VALUES ('1355638052910', '新保安', '1355638052820');


alter table DEMO_SINGLE_DATAGRID add clobtest text;


alter table DEMO_SINGLE_DATAGRID add formkey varchar(300);



insert into DEMO_SINGLE_DATAGRID(sid, name, treenode_sid, gender, age_, formkey) values('2345', '测试formkeyfield', null, null, null, '/demos/multiple_datagridsdemo_formkeyform.jsp');


alter table SYS_OPTS add formkey varchar(300);


/**
 * demo表添加imgpath字段
 */

alter table DEMO_SINGLE_DATAGRID add imgpath varchar(500) NULL;

insert into DEMO_SINGLE_DATAGRID 
values('13622776068292', '哆啦A梦', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a111634u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('1362277606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a128335u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('14462277606929', '全可可小爱', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a134153u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('1562277606929', '全可可小爱', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a134394u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('13622757606929', '全迪迦奥特曼', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a138731u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('13622776006929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a214107_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('136221277606929', '小小猫和老鼠', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a218261u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('136227712606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a262783_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('136227237606929', '芭比之梦想豪宅', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a304007_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('136227763406929', '小小猫和老鼠', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a306705_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('13622776606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a150907u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('13622776076929', '全迪迦奥特曼', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a151177u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('13682277606929', '小小猫和老鼠', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a167419u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('13622779606929', '芭比之梦想豪宅', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a203937_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('13622776068929', '小小猫和老鼠', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/vrsa8848u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('11362277606829', '哆啦A梦', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a111634u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('21362277606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a128335u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('314462277606929', '全可可小爱', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a134153u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('41562277606929', '全可可小爱', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a134394u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('513622757606929', '全迪迦奥特曼', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a138731u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('613622776606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a150907u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('713622776076929', '全迪迦奥特曼', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a151177u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('813682277606929', '小小猫和老鼠', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a167419u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('913622779606929', '芭比之梦想豪宅', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a203937_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('2113622776006929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a214107_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('22136221277606929', '小小猫和老鼠', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a218261u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('23136227712606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a262783_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('231136227237606929', '芭比之梦想豪宅', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a304007_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('34136227763406929', '小小猫和老鼠', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a306705_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('45136227745606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a332192_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('56136227756606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a332321_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('67136227767606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a8821u2_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('78136227767806929', '小小猫和老鼠', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a9273_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('8913622776076929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a9982u2_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('9013622776068929', '小小猫和老鼠', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/vrsa8848u1_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('136227745606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a332192_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('136227756606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a332321_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('136227767606929', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a8821u2_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('136227767806929', '小小猫和老鼠', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a9273_160_90.jpg');

insert into DEMO_SINGLE_DATAGRID 
values('13622776076929a', '全铠甲勇士', null, null, null, null, 
'喜羊羊灰太狼拜大年', '/a9982u2_160_90.jpg');


/**
 * demogrid表tile数据附加挂接到左侧树节点_
 */

update DEMO_SINGLE_DATAGRID set treenode_sid= '1355638052583' where formkey is not null;




CREATE TABLE WCM_PAGE (
	sid varchar(36) NOT NULL, 
	name varchar(500) NULL, 
	templatesid varchar(36) NULL, 
	templatename varchar(200) NULL, 
	sitesid varchar(36) NOT NULL, 
	PRIMARY KEY (sid)
);

CREATE TABLE WCM_CONTAINER_DATA (
	sid varchar(36) NOT NULL, 
	assetsid varchar(36) NOT NULL, 
	pagesid varchar(36) NOT NULL, 
	containersid varchar(36) NULL, 
	PRIMARY KEY (sid)
);

CREATE TABLE WCM_ASSET (
	sid varchar(36) NOT NULL, 
	name varchar(200) NULL, 
	assetdata longblob NULL, 
	assettype varchar(1) NULL, 
	PRIMARY KEY (sid)
);

CREATE TABLE WCM_SITE (
	sid varchar(36) NOT NULL, 
	name varchar(200) NULL, 
	PRIMARY KEY (sid)
);




CREATE TABLE WCM_SITE_PUBLISH_HIS (
	sid varchar(36) NOT NULL, 
	sitesid varchar(36) NOT NULL, 
	createtime datetime NULL, 
	PRIMARY KEY (sid)
);



alter table WCM_PAGE add pagetype varchar(1) NULL;




alter table WCM_CONTAINER_DATA add assettype varchar(1) NULL;



alter table SYS_FILES add filedesc varchar(4000) NULL;




