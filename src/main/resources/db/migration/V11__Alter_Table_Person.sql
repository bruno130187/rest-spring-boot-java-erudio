IF COL_LENGTH ('rest-spring-boot-erudio.dbo.person', 'enabled') IS NULL
BEGIN
  ALTER TABLE [dbo].[person] ADD [enabled] [BIT] NOT NULL DEFAULT 1;
END;