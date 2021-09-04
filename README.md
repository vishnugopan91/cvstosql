# cvstosql
This is a generic utility that can be used to create SQL file for simple data migration tasks.
Say, you are provided with an Excel/csv with fname, surname, age and you have to construct a query like, 'INSERT INTO EMP VALUES('Vishnu', 'Gopan', 30)', you can simply put few configurations in the application.property file and run the utility as a REST application, where the POST endpoint will read your csv and generate SQL output as required.
