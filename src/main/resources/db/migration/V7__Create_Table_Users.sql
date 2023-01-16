CREATE TABLE [dbo].[users] (
  [id] [bigint] PRIMARY KEY IDENTITY(1,1) NOT NULL,
  [user_name] [varchar](255) UNIQUE,
  [full_name] [varchar](255),
  [password] [varchar](255),
  [account_non_expired] [bit],
  [account_non_locked] [bit],
  [credentials_non_expired] [bit],
  [enabled] [bit],
);
SET IDENTITY_INSERT [dbo].[users] OFF;