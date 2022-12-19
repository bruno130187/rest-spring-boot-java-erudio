CREATE TABLE [dbo].[person](
    [id] [bigint] PRIMARY KEY IDENTITY(1,1) NOT NULL,
    [first_name] [varchar](80) NOT NULL,
    [last_name] [varchar](80) NOT NULL,
    [address] [varchar](200) NOT NULL,
    [gender] [varchar](10) NOT NULL);

SET IDENTITY_INSERT [dbo].[person] ON;




