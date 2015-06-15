-- id=clearQuartzData0 desc=清除历史数据---
delete from QRTZ_CALENDARS;
-- id=clearQuartzData1 desc=清除历史数据---
delete from QRTZ_CRON_TRIGGERS;
-- id=clearQuartzData2 desc=清除历史数据---
delete from QRTZ_FIRED_TRIGGERS;
-- id=clearQuartzData3 desc=清除历史数据---
delete from QRTZ_PAUSED_TRIGGER_GRPS;
-- id=clearQuartzData4 desc=清除历史数据---
delete from QRTZ_SCHEDULER_STATE;
-- id=clearQuartzData5 desc=清除历史数据---
delete from QRTZ_LOCKS;
-- id=clearQuartzData6 desc=清除历史数据---
delete from QRTZ_JOB_DETAILS;
-- id=clearQuartzData7 desc=清除历史数据---
delete from QRTZ_JOB_LISTENERS;
-- id=clearQuartzData8 desc=清除历史数据---
delete from QRTZ_SIMPLE_TRIGGERS;
-- id=clearQuartzData9 desc=清除历史数据---
delete from QRTZ_BLOB_TRIGGERS;
-- id=clearQuartzData10 desc=清除历史数据---
delete from QRTZ_TRIGGER_LISTENERS;
-- id=clearQuartzData11 desc=清除历史数据---
delete from QRTZ_TRIGGERS;
-- id=clearQuartzData12 desc=清除历史数据---
INSERT INTO QRTZ_LOCKS VALUES('TRIGGER_ACCESS');
-- id=clearQuartzData13 desc=清除历史数据---
INSERT INTO QRTZ_LOCKS VALUES('JOB_ACCESS');
-- id=clearQuartzData14 desc=清除历史数据---
INSERT INTO QRTZ_LOCKS VALUES('CALENDAR_ACCESS');
-- id=clearQuartzData15 desc=清除历史数据---
INSERT INTO QRTZ_LOCKS VALUES('STATE_ACCESS');
-- id=clearQuartzData16 desc=清除历史数据---
INSERT INTO QRTZ_LOCKS VALUES('MISFIRE_ACCESS');