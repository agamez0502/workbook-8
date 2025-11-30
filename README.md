# 📘 Workbook 8 — Working with Databases Using JDBC  
**Student Workbook – Version 2.2**

Workbook 8 introduces connecting Java applications to real databases using JDBC.  
This workbook covers Maven configuration, JDBC drivers, DataSources, CRUD operations, DAOs, and stored procedures.

---

## 📚 Table of Contents

### 🔹 Module 1: Updating the Maven pom.xml
- Understanding the `pom.xml` file  
- Adding dependencies  
- Reloading Maven changes  
- Viewing external libraries  
- Exercises (adding Log4j2 dependency)

---

### 🔹 Module 2: JDBC Basics
- JDBC overview  
- Drivers & MySQL Connector  
- Database URLs  
- JDBC programming flow  
- Connecting with `DriverManager`  
- PreparedStatement vs Statement  
- Avoiding SQL injection  
- Executing queries  
- Extracting data with `ResultSet`  
- Closing resources  
- Passing arguments in Run Configurations  
- try/catch/finally  
- try-with-resources  
- Exercises (NorthwindTraders queries)

---

### 🔹 Module 3: JDBC DataSources
- Using `BasicDataSource`  
- Configuring a DataSource  
- Connections with DataSource  
- Prepared statements with parameters  
- ResultSet processing  
- JOIN queries  
- Creating a DataManager class  
- DAO pattern (ProductDao, CategoryDao)  
- Exercises (refactoring to DataSource, SakilaMovies project)

---

### 🔹 Module 4: CRUD Operations
- INSERT operations  
- Getting auto-generated keys  
- UPDATE operations  
- DELETE operations  
- Running CRUD methods from main  
- Exercises (NorthwindShippers CRUD workflow)

---

### 🔹 Module 5: Miscellaneous Topics
- Creating tables with SQL  
- Stored procedures overview  
- Calling stored procedures in MySQL Workbench  
- Calling stored procedures using `CallableStatement` in Java  
- Exercises (NorthwindProcedures project)

---

### 🔹 Module 6: CodeWars
- Java + SQL themed Katas  
- Practice using strings, formatting, loops, and reasoning skills  

---

## 💡 Core Concepts
- 📦 **Maven Dependencies** — managing external libraries through `pom.xml`  
- 🔌 **JDBC Driver Loading** — enabling Java to communicate with MySQL  
- 🌐 **Database URLs** — defining host, port, and schema for connection  
- 🧵 **PreparedStatement** — safe, parameterized SQL execution  
- 🗃️ **ResultSet Processing** — retrieving data row-by-row  
- 🔄 **try-with-resources** — automatic closing of JDBC resources  
- 🗄️ **DataSource** — modern connection management  
- 🧩 **DAO Pattern** — separating database logic from application logic  
- 🛠️ **CRUD Operations** — inserting, updating, deleting, and retrieving data  
- 📞 **Stored Procedures** — executing predefined SQL logic from Java  

---

### ✨ Final Slay Note  
Connecting Java to a real database is where applications start feeling *alive*.  
Write clean queries, manage connections responsibly, and let your JDBC code slay — safely and efficiently. 💅⚡  
