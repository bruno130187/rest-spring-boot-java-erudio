IF COL_LENGTH ('rest-spring-boot-erudio.dbo.books', 'enabled') IS NULL
BEGIN
  ALTER TABLE [dbo].[books] ADD [enabled] [BIT] NOT NULL DEFAULT 1;
END;