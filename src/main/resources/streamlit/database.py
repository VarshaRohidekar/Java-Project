import mysql.connector

# Connect to the MySQL database
db = mysql.connector.connect(
    host="localhost",
    user="root",
    database="cm",
    password="newyork1176"
)

# Create a cursor object
cursor = db.cursor()

# Create tables
cursor.execute("""
    CREATE TABLE IF NOT EXISTS teachers (
        srn VARCHAR(20) PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        password VARCHAR(255) NOT NULL
    )
""")

cursor.execute("""
    CREATE TABLE IF NOT EXISTS students (
        srn VARCHAR(20) PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        password VARCHAR(255) NOT NULL
    )
""")

cursor.execute("""
    CREATE TABLE IF NOT EXISTS team_mappings (
        id INT AUTO_INCREMENT PRIMARY KEY,
        student1_srn VARCHAR(20) NOT NULL,
        student2_srn VARCHAR(20) NOT NULL,
        student3_srn VARCHAR(20) NOT NULL,
        student4_srn VARCHAR(20) NOT NULL,
        teacher_srn VARCHAR(20) NOT NULL,
        domain TEXT,
        problem_statement TEXT,
        request_accepted BOOLEAN DEFAULT FALSE,
        FOREIGN KEY (student1_srn) REFERENCES students(srn),
        FOREIGN KEY (student2_srn) REFERENCES students(srn),
        FOREIGN KEY (student3_srn) REFERENCES students(srn),
        FOREIGN KEY (student4_srn) REFERENCES students(srn),
        FOREIGN KEY (teacher_srn) REFERENCES teachers(srn)
    )
""")

db.commit()

# Other database operations...

def get_user(srn, password, role):
    sql = f"SELECT srn, name FROM {role}s WHERE srn = %s AND password = %s"
    values = (srn, password)
    cursor.execute(sql, values)
    return cursor.fetchone()

def get_teachers():
    sql = "SELECT srn, name FROM teachers"
    cursor.execute(sql)
    return cursor.fetchall()

def insert_user(srn, name, password, role):
    sql = f"INSERT INTO {role}s (srn, name, password) VALUES (%s, %s, %s)"
    values = (srn, name, password)
    cursor.execute(sql, values)
    db.commit()

def insert_team_request(student1_srn, student2_srn, student3_srn, student4_srn, teacher_srn, domain, problem_statement):
    sql = "INSERT INTO team_mappings (student1_srn, student2_srn, student3_srn, student4_srn, teacher_srn, domain, problem_statement) VALUES (%s, %s, %s, %s, %s, %s, %s)"
    values = (student1_srn, student2_srn, student3_srn, student4_srn, teacher_srn, domain, problem_statement)
    cursor.execute(sql, values)
    db.commit()

def get_team_requests(teacher_srn):
    sql = """
        SELECT tm.id, tm.student1_srn, tm.student2_srn, tm.student3_srn, tm.student4_srn, tm.domain, tm.problem_statement, tm.request_accepted
        FROM team_mappings tm
        WHERE tm.teacher_srn = %s
    """
    values = (teacher_srn,)
    cursor.execute(sql, values)
    return cursor.fetchall()


def get_student_team_requests(student_srn):
    sql = """
        SELECT tm.id, s1.name AS student1_name, s2.name AS student2_name, s3.name AS student3_name, s4.name AS student4_name, tm.domain, tm.problem_statement, tm.request_accepted, t.name AS teacher_name
        FROM team_mappings tm
        JOIN students s1 ON tm.student1_srn = s1.srn
        JOIN students s2 ON tm.student2_srn = s2.srn
        JOIN students s3 ON tm.student3_srn = s3.srn
        JOIN students s4 ON tm.student4_srn = s4.srn
        JOIN teachers t ON tm.teacher_srn = t.srn
        WHERE tm.student1_srn = %s OR tm.student2_srn = %s OR tm.student3_srn = %s OR tm.student4_srn = %s
    """
    values = (student_srn, student_srn, student_srn, student_srn)
    cursor.execute(sql, values)
    return cursor.fetchall()

def update_team_request(request_id, accepted):
    sql = "UPDATE team_mappings SET request_accepted = %s WHERE id = %s"
    values = (accepted, request_id)
    cursor.execute(sql, values)
    db.commit()

# Close the database connection
db.close()