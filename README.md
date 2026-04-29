# Ready 2 Read — Book Review & Reading Tracker

A Goodreads-inspired desktop application built with Java, JDBC, and MySQL.

## Team
- Katia Yarkov (018141825)
- Geeta Renavikar (017035109)

## Tech Stack
- Java
- JDBC
- MySQL / MySQL Workbench

## Database Setup

### Schema
![ER Diagram](docs/er_diagram.png)

### Prerequisites
- MySQL installed and running
- MySQL Workbench (optional but recommended)

### Steps

1. Open MySQL Workbench and connect to your local instance
2. Open a new query tab and run the schema file:
   - Go to **File → Open SQL Script** and select `schema.sql`, or
   - Copy and paste the contents of `schema.sql` into the query tab
3. Hit **Ctrl+Shift+Enter** to run the full script
4. Refresh the Schemas panel to confirm `ready2read` appears
5. Run the seed file the same way using `seed.sql` (coming soon)

### Verify it worked
```sql
USE ready2read;
SHOW TABLES;
```
You should see all 5 tables: Users, Authors, Books, Reviews, ReadingList.

## Project Structure
(to be updated as the project develops)