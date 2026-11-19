STUDENT PROFILE AND SKILL DEVELOPMENT MANAGEMENT SYSTEM
Spring Boot 3.2.5 | Java 17+ | MySQL (XAMPP) | Port 8081

HOW TO RUN
1. Open XAMPP Control Panel -> click Start next to MySQL (Apache is not needed).
2. Unzip this folder, then double-click START.bat.
   - FIRST RUN ONLY: it builds student-management.jar. This needs internet and
     takes a few minutes (it downloads Maven and the libraries automatically).
   - Later runs start immediately.
3. Wait for "Started StudentManagementApplication", then use Postman.

DATABASE: nothing to create. Database "student_db" and tables "students" and
"student_skills" are created automatically.
OPTIONAL: run database\database_setup.sql in phpMyAdmin if you want 6 sample students.

NEEDED ON YOUR PC: JDK 17 or newer (a JDK, not only a JRE) and internet on the first run.

POSTMAN
File -> Import -> choose Student_API.postman_collection.json. It contains all 19
requests below in a good testing order (run 01 to 19 from top to bottom).
Or create requests manually. Base URL: http://localhost:8081/api/students
For POST and PUT: Body -> raw -> JSON.

ENDPOINTS
Q3.1 Add student        POST   /api/students                          -> 201
   {
     "studentId": "S101", "studentName": "Aarav Sharma",
     "email": "aarav@example.com", "courseName": "B.Tech CSE",
     "programmingScore": 92, "mathematicsScore": 88,
     "skills": ["Java", "Spring Boot", "MySQL"]
   }
   Response also shows averageScore (90.0), performanceCategory ("Excellent")
   and strongerSubject ("Programming"). 
   Duplicate ID -> 409. Score outside 0-100, missing/invalid data -> 400.
Q3.2 All students       GET    /api/students                          -> 200
Q3.3 Student by ID      GET    /api/students/S101                     -> 200 / 404
Q3.4 Update scores      PUT    /api/students/S101                     -> 200 / 404 / 400
   Any field can be left out:
   { "programmingScore": 72, "mathematicsScore": 60, "skills": ["Java", "Docker"] }
Q3.5 Delete student     DELETE /api/students/S101                     -> 200 / 404
Q3.6 Merit list         GET    /api/students/merit-list               -> 200
   (sorted by average, highest first, with rank)

Q2 business rules
Q2.4 Search             GET /api/students/search?id=S101
                        GET /api/students/search?email=aarav@example.com
                        GET /api/students/search?course=B.Tech CSE
Q2.6 Same skills        GET /api/students/same-skills            (groups with identical skill sets)
                        GET /api/students/same-skills/by-skill   (students sharing each skill)
Q2.7 Programming < 50 and Mathematics >= 75
                        GET /api/students/needs-programming-improvement
Q2.1-2.3 (average, category, stronger subject) appear in every student response.
Q2.8 Duplicate IDs and score validation happen when adding/updating (409 / 400).

Error responses look like: { "status": 404, "error": "Not Found", "message": "Student not found with ID: S999" }

TROUBLESHOOTING
- "MySQL is not running": start MySQL in XAMPP, then run START.bat again.
- "Port 8081 already in use": close the other window running the app.
- Build failed: install JDK 17+, check internet, then send me the error text.
- To force a rebuild after any change: delete student-management.jar and run START.bat.
