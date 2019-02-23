# Student Registraton Database Management System (SRDBMS)

## How to use?
In order to start using the program, you must run this using your dedicated IDE for java.
The app accepts two to three arguments **IN ORDER**:

1. `command/input file path` (the file that the program will scan)
2. `log file path` (to print the console outputs into a .dat file)
3. `database name` (the name you want for your database) **-optional**

> NOTE: The app have a default config set, if you don't provide the main arguments, it will use the default app config.
> this might cause errors and the program would not work.

> IMPORTANT: Do not forget to include your JDBC Driver in your project.

1. Start Apache and MySQL
2. Configure App Settings
    - specify the command file and log file path
  
3. Create the command/input file
4. Run the program!

## Commands
1. `A` (Add/Create a student record)
  ```
  A
  <student id>
  <last name>
  <first name>
  <course>
  <units earned/taken> *(max units: 210)*
  
  Sample:
  A
  2019-01023
  Trump
  Donald
  SE
  150
 
  ```
  
2. `S` (Search student record(s))
  ```
  S
  <student id | last name | first name | course | year level>
  ```
  
3. `D` (Delete student record)
  ```
  D
  <student id>
  ```
  
4. `R` (Generate Report)
5. `L` (list/Print all records)
6. `P` (Purge/Erase all records)
7. `Q` (Quit application)
