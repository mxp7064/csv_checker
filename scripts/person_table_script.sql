/****** Object:  Table [dbo].[person]    Script Date: 9/12/2018 11:56:14 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[person](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[firstName] [nchar](50) NULL,
	[lastName] [nchar](50) NULL,
	[zip] [int] NULL,
	[city] [nchar](50) NULL,
	[phone] [nchar](50) NULL,
 CONSTRAINT [PK_persons] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
