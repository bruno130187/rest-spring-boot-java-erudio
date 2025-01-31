﻿CREATE TABLE [dbo].[user_permission] (
  [id_user] [bigint] NOT NULL,
  [id_permission] [bigint] NOT NULL,
  CONSTRAINT [pk_user_permission] PRIMARY KEY (id_user, id_permission),
  CONSTRAINT [fk_user_permission] FOREIGN KEY ([id_user]) REFERENCES [dbo].[users]([id]),
  CONSTRAINT [fk_user_permission_permission] FOREIGN KEY ([id_permission]) REFERENCES [dbo].[permission]([id]),
);
--SET IDENTITY_INSERT [dbo].[user_permission] OFF;

