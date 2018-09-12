CREATE PROCEDURE InsertPersonProcedure
	@FirstName nvarchar(50),  
    @LastName nvarchar(50),   
	@Zip nvarchar(50), 
	@City nvarchar(50), 
	@Phone nvarchar(50)
AS  
	BEGIN 
		BEGIN TRY
			IF EXISTS (SELECT * FROM person WHERE 
						firstName = @FirstName AND
						lastName = @LastName AND
						zip = @Zip AND
						city = @City AND
						phone = @Phone)
	
				BEGIN
					RAISERROR ('DUPLICATE_RECORD', 16, 1) WITH LOG;
				END
			ELSE
				BEGIN
					INSERT INTO person (firstName, lastName, zip, city, phone) VALUES
					(@FirstName, @LastName, @Zip, @City, @Phone)
					RETURN 0;
				END
		END TRY
		BEGIN CATCH
			DECLARE @ErrorMessage NVARCHAR(4000),
					@ErrorSeverity INT,
					@ErrorState INT;  

			SELECT  @ErrorMessage = ERROR_MESSAGE(),  
				    @ErrorSeverity = ERROR_SEVERITY(),  
				    @ErrorState = ERROR_STATE();  

			RAISERROR (@ErrorMessage, @ErrorSeverity, @ErrorState);  
		END CATCH
	END
GO  