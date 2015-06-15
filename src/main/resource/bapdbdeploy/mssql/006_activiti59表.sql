/****** Object:  Table [dbo].[ACT_ID_INFO]    Script Date: 07/27/2012 21:56:43 ******/
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
CREATE TABLE [dbo].[ACT_ID_INFO](
	[ID_] [nvarchar](64) NOT NULL,
	[REV_] [int] NULL,
	[USER_ID_] [nvarchar](64) NULL,
	[TYPE_] [nvarchar](64) NULL,
	[KEY_] [nvarchar](255) NULL,
	[VALUE_] [nvarchar](255) NULL,
	[PASSWORD_] [image] NULL,
	[PARENT_ID_] [nvarchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY];
/****** Object:  Table [dbo].[ACT_ID_GROUP]    Script Date: 07/27/2012 21:56:43 ******/
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
CREATE TABLE [dbo].[ACT_ID_GROUP](
	[ID_] [nvarchar](64) NOT NULL,
	[REV_] [int] NULL,
	[NAME_] [nvarchar](255) NULL,
	[TYPE_] [nvarchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];
/****** Object:  Table [dbo].[ACT_HI_TASKINST]    Script Date: 07/27/2012 21:56:43 ******/
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
CREATE TABLE [dbo].[ACT_HI_TASKINST](
	[ID_] [nvarchar](64) NOT NULL,
	[PROC_DEF_ID_] [nvarchar](64) NULL,
	[TASK_DEF_KEY_] [nvarchar](255) NULL,
	[PROC_INST_ID_] [nvarchar](64) NULL,
	[EXECUTION_ID_] [nvarchar](64) NULL,
	[NAME_] [nvarchar](255) NULL,
	[PARENT_TASK_ID_] [nvarchar](64) NULL,
	[DESCRIPTION_] [nvarchar](4000) NULL,
	[OWNER_] [nvarchar](64) NULL,
	[ASSIGNEE_] [nvarchar](64) NULL,
	[START_TIME_] [datetime] NOT NULL,
	[END_TIME_] [datetime] NULL,
	[DURATION_] [numeric](19, 0) NULL,
	[DELETE_REASON_] [nvarchar](4000) NULL,
	[PRIORITY_] [int] NULL,
	[DUE_DATE_] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];
/****** Object:  Table [dbo].[ACT_HI_PROCINST]    Script Date: 07/27/2012 21:56:43 ******/
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
CREATE TABLE [dbo].[ACT_HI_PROCINST](
	[ID_] [nvarchar](64) NOT NULL,
	[PROC_INST_ID_] [nvarchar](64) NOT NULL,
	[BUSINESS_KEY_] [nvarchar](255) NULL,
	[PROC_DEF_ID_] [nvarchar](64) NOT NULL,
	[START_TIME_] [datetime] NOT NULL,
	[END_TIME_] [datetime] NULL,
	[DURATION_] [numeric](19, 0) NULL,
	[START_USER_ID_] [nvarchar](255) NULL,
	[START_ACT_ID_] [nvarchar](255) NULL,
	[END_ACT_ID_] [nvarchar](255) NULL,
	[SUPER_PROCESS_INSTANCE_ID_] [nvarchar](64) NULL,
	[DELETE_REASON_] [nvarchar](4000) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
UNIQUE NONCLUSTERED 
(
	[PROC_INST_ID_] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];
/****** Object:  Table [dbo].[ACT_HI_DETAIL]    Script Date: 07/27/2012 21:56:43 ******/
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
CREATE TABLE [dbo].[ACT_HI_DETAIL](
	[ID_] [nvarchar](64) NOT NULL,
	[TYPE_] [nvarchar](255) NOT NULL,
	[PROC_INST_ID_] [nvarchar](64) NOT NULL,
	[EXECUTION_ID_] [nvarchar](64) NOT NULL,
	[TASK_ID_] [nvarchar](64) NULL,
	[ACT_INST_ID_] [nvarchar](64) NULL,
	[NAME_] [nvarchar](255) NOT NULL,
	[VAR_TYPE_] [nvarchar](255) NULL,
	[REV_] [int] NULL,
	[TIME_] [datetime] NOT NULL,
	[BYTEARRAY_ID_] [nvarchar](64) NULL,
	[DOUBLE_] [float] NULL,
	[LONG_] [numeric](19, 0) NULL,
	[TEXT_] [nvarchar](4000) NULL,
	[TEXT2_] [nvarchar](4000) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];
/****** Object:  Table [dbo].[ACT_HI_COMMENT]    Script Date: 07/27/2012 21:56:43 ******/
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
CREATE TABLE [dbo].[ACT_HI_COMMENT](
	[ID_] [nvarchar](64) NOT NULL,
	[TYPE_] [nvarchar](255) NULL,
	[TIME_] [datetime] NOT NULL,
	[USER_ID_] [nvarchar](255) NULL,
	[TASK_ID_] [nvarchar](64) NULL,
	[PROC_INST_ID_] [nvarchar](64) NULL,
	[ACTION_] [nvarchar](255) NULL,
	[MESSAGE_] [nvarchar](4000) NULL,
	[FULL_MSG_] [image] NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY];
/****** Object:  Table [dbo].[ACT_HI_ATTACHMENT]    Script Date: 07/27/2012 21:56:43 ******/
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
CREATE TABLE [dbo].[ACT_HI_ATTACHMENT](
	[ID_] [nvarchar](64) NOT NULL,
	[REV_] [int] NULL,
	[USER_ID_] [nvarchar](255) NULL,
	[NAME_] [nvarchar](255) NULL,
	[DESCRIPTION_] [nvarchar](4000) NULL,
	[TYPE_] [nvarchar](255) NULL,
	[TASK_ID_] [nvarchar](64) NULL,
	[PROC_INST_ID_] [nvarchar](64) NULL,
	[URL_] [nvarchar](4000) NULL,
	[CONTENT_ID_] [nvarchar](64) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];
/****** Object:  Table [dbo].[ACT_HI_ACTINST]    Script Date: 07/27/2012 21:56:43 ******/
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
CREATE TABLE [dbo].[ACT_HI_ACTINST](
	[ID_] [nvarchar](64) NOT NULL,
	[PROC_DEF_ID_] [nvarchar](64) NOT NULL,
	[PROC_INST_ID_] [nvarchar](64) NOT NULL,
	[EXECUTION_ID_] [nvarchar](64) NOT NULL,
	[ACT_ID_] [nvarchar](255) NOT NULL,
	[ACT_NAME_] [nvarchar](255) NULL,
	[ACT_TYPE_] [nvarchar](255) NOT NULL,
	[ASSIGNEE_] [nvarchar](64) NULL,
	[START_TIME_] [datetime] NOT NULL,
	[END_TIME_] [datetime] NULL,
	[DURATION_] [numeric](19, 0) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];
/****** Object:  Table [dbo].[ACT_GE_PROPERTY]    Script Date: 07/27/2012 21:56:43 ******/
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
CREATE TABLE [dbo].[ACT_GE_PROPERTY](
	[NAME_] [nvarchar](64) NOT NULL,
	[VALUE_] [nvarchar](300) NULL,
	[REV_] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[NAME_] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];
INSERT [dbo].[ACT_GE_PROPERTY] ([NAME_], [VALUE_], [REV_]) VALUES (N'historyLevel', N'3', 1)
INSERT [dbo].[ACT_GE_PROPERTY] ([NAME_], [VALUE_], [REV_]) VALUES (N'next.dbid', N'1', 1)
INSERT [dbo].[ACT_GE_PROPERTY] ([NAME_], [VALUE_], [REV_]) VALUES (N'schema.history', N'create(5.9)', 1)
INSERT [dbo].[ACT_GE_PROPERTY] ([NAME_], [VALUE_], [REV_]) VALUES (N'schema.version', N'5.9', 1)
/****** Object:  Table [dbo].[ACT_RE_PROCDEF]    Script Date: 07/27/2012 21:56:43 ******/
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
CREATE TABLE [dbo].[ACT_RE_PROCDEF](
	[ID_] [nvarchar](64) NOT NULL,
	[REV_] [int] NULL,
	[CATEGORY_] [nvarchar](255) NULL,
	[NAME_] [nvarchar](255) NULL,
	[KEY_] [nvarchar](255) NULL,
	[VERSION_] [int] NULL,
	[DEPLOYMENT_ID_] [nvarchar](64) NULL,
	[RESOURCE_NAME_] [nvarchar](4000) NULL,
	[DGRM_RESOURCE_NAME_] [nvarchar](4000) NULL,
	[HAS_START_FORM_KEY_] [tinyint] NULL,
	[SUSPENSION_STATE_] [tinyint] NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];
/****** Object:  Table [dbo].[ACT_RE_DEPLOYMENT]    Script Date: 07/27/2012 21:56:43 ******/
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
CREATE TABLE [dbo].[ACT_RE_DEPLOYMENT](
	[ID_] [nvarchar](64) NOT NULL,
	[NAME_] [nvarchar](255) NULL,
	[DEPLOY_TIME_] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];
/****** Object:  Table [dbo].[ACT_ID_USER]    Script Date: 07/27/2012 21:56:43 ******/
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
CREATE TABLE [dbo].[ACT_ID_USER](
	[ID_] [nvarchar](64) NOT NULL,
	[REV_] [int] NULL,
	[FIRST_] [nvarchar](255) NULL,
	[LAST_] [nvarchar](255) NULL,
	[EMAIL_] [nvarchar](255) NULL,
	[PWD_] [nvarchar](255) NULL,
	[PICTURE_ID_] [nvarchar](64) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];
/****** Object:  Table [dbo].[ACT_RU_EXECUTION]    Script Date: 07/27/2012 21:56:43 ******/
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
CREATE TABLE [dbo].[ACT_RU_EXECUTION](
	[ID_] [nvarchar](64) NOT NULL,
	[REV_] [int] NULL,
	[PROC_INST_ID_] [nvarchar](64) NULL,
	[BUSINESS_KEY_] [nvarchar](255) NULL,
	[PARENT_ID_] [nvarchar](64) NULL,
	[PROC_DEF_ID_] [nvarchar](64) NULL,
	[SUPER_EXEC_] [nvarchar](64) NULL,
	[ACT_ID_] [nvarchar](255) NULL,
	[IS_ACTIVE_] [tinyint] NULL,
	[IS_CONCURRENT_] [tinyint] NULL,
	[IS_SCOPE_] [tinyint] NULL,
	[IS_EVENT_SCOPE_] [tinyint] NULL,
	[SUSPENSION_STATE_] [tinyint] NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];
/****** Object:  Table [dbo].[ACT_RU_EVENT_SUBSCR]    Script Date: 07/27/2012 21:56:43 ******/
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
CREATE TABLE [dbo].[ACT_RU_EVENT_SUBSCR](
	[ID_] [nvarchar](64) NOT NULL,
	[REV_] [int] NULL,
	[EVENT_TYPE_] [nvarchar](255) NOT NULL,
	[EVENT_NAME_] [nvarchar](255) NULL,
	[EXECUTION_ID_] [nvarchar](64) NULL,
	[PROC_INST_ID_] [nvarchar](64) NULL,
	[ACTIVITY_ID_] [nvarchar](64) NULL,
	[CONFIGURATION_] [nvarchar](255) NULL,
	[CREATED_] [datetime] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];
/****** Object:  Table [dbo].[ACT_ID_MEMBERSHIP]    Script Date: 07/27/2012 21:56:43 ******/
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
CREATE TABLE [dbo].[ACT_ID_MEMBERSHIP](
	[USER_ID_] [nvarchar](64) NOT NULL,
	[GROUP_ID_] [nvarchar](64) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[USER_ID_] ASC,
	[GROUP_ID_] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];
/****** Object:  Table [dbo].[ACT_GE_BYTEARRAY]    Script Date: 07/27/2012 21:56:43 ******/
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
CREATE TABLE [dbo].[ACT_GE_BYTEARRAY](
	[ID_] [nvarchar](64) NOT NULL,
	[REV_] [int] NULL,
	[NAME_] [nvarchar](255) NULL,
	[DEPLOYMENT_ID_] [nvarchar](64) NULL,
	[BYTES_] [image] NULL,
	[GENERATED_] [tinyint] NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY];
/****** Object:  Table [dbo].[ACT_RU_TASK]    Script Date: 07/27/2012 21:56:43 ******/
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
CREATE TABLE [dbo].[ACT_RU_TASK](
	[ID_] [nvarchar](64) NOT NULL,
	[REV_] [int] NULL,
	[EXECUTION_ID_] [nvarchar](64) NULL,
	[PROC_INST_ID_] [nvarchar](64) NULL,
	[PROC_DEF_ID_] [nvarchar](64) NULL,
	[NAME_] [nvarchar](255) NULL,
	[PARENT_TASK_ID_] [nvarchar](64) NULL,
	[DESCRIPTION_] [nvarchar](4000) NULL,
	[TASK_DEF_KEY_] [nvarchar](255) NULL,
	[OWNER_] [nvarchar](64) NULL,
	[ASSIGNEE_] [nvarchar](64) NULL,
	[DELEGATION_] [nvarchar](64) NULL,
	[PRIORITY_] [int] NULL,
	[CREATE_TIME_] [datetime] NULL,
	[DUE_DATE_] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];
/****** Object:  Table [dbo].[ACT_RU_VARIABLE]    Script Date: 07/27/2012 21:56:43 ******/
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
CREATE TABLE [dbo].[ACT_RU_VARIABLE](
	[ID_] [nvarchar](64) NOT NULL,
	[REV_] [int] NULL,
	[TYPE_] [nvarchar](255) NOT NULL,
	[NAME_] [nvarchar](255) NOT NULL,
	[EXECUTION_ID_] [nvarchar](64) NULL,
	[PROC_INST_ID_] [nvarchar](64) NULL,
	[TASK_ID_] [nvarchar](64) NULL,
	[BYTEARRAY_ID_] [nvarchar](64) NULL,
	[DOUBLE_] [float] NULL,
	[LONG_] [numeric](19, 0) NULL,
	[TEXT_] [nvarchar](4000) NULL,
	[TEXT2_] [nvarchar](4000) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];
/****** Object:  Table [dbo].[ACT_RU_JOB]    Script Date: 07/27/2012 21:56:43 ******/
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
CREATE TABLE [dbo].[ACT_RU_JOB](
	[ID_] [nvarchar](64) NOT NULL,
	[REV_] [int] NULL,
	[TYPE_] [nvarchar](255) NOT NULL,
	[LOCK_EXP_TIME_] [datetime] NULL,
	[LOCK_OWNER_] [nvarchar](255) NULL,
	[EXCLUSIVE_] [bit] NULL,
	[EXECUTION_ID_] [nvarchar](64) NULL,
	[PROCESS_INSTANCE_ID_] [nvarchar](64) NULL,
	[RETRIES_] [int] NULL,
	[EXCEPTION_STACK_ID_] [nvarchar](64) NULL,
	[EXCEPTION_MSG_] [nvarchar](4000) NULL,
	[DUEDATE_] [datetime] NULL,
	[REPEAT_] [nvarchar](255) NULL,
	[HANDLER_TYPE_] [nvarchar](255) NULL,
	[HANDLER_CFG_] [nvarchar](4000) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];
/****** Object:  Table [dbo].[ACT_RU_IDENTITYLINK]    Script Date: 07/27/2012 21:56:43 ******/
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
CREATE TABLE [dbo].[ACT_RU_IDENTITYLINK](
	[ID_] [nvarchar](64) NOT NULL,
	[REV_] [int] NULL,
	[GROUP_ID_] [nvarchar](64) NULL,
	[TYPE_] [nvarchar](255) NULL,
	[USER_ID_] [nvarchar](64) NULL,
	[TASK_ID_] [nvarchar](64) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID_] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY];
