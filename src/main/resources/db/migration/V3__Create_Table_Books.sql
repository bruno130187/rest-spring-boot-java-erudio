CREATE TABLE [dbo].[books] (
  [id] [bigint] PRIMARY KEY IDENTITY(1,1) NOT NULL,
  [author] [varchar](255),
  [launch_date] [date] NOT NULL,
  [price] [decimal](38,2) NOT NULL,
  [title] [text]);
SET IDENTITY_INSERT [dbo].[books] OFF;
