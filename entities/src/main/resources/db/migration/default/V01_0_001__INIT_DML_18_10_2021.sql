  CREATE TABLE IF NOT EXISTS employees (
    id integer PRIMARY KEY,
    first_name character varying(256) NOT NULL,
    last_name character varying(256) NOT NULL,
    email character varying(256) NOT NULL,
    password character varying(256) NOT NULL,
    token character varying(256),

    UNIQUE(email)

);


  CREATE TABLE IF NOT EXISTS admins (
    id integer PRIMARY KEY,
    name character varying(256) NOT NULL,
    email character varying(256) NOT NULL,
    password character varying(256) NOT NULL,
    token character varying(256),

    UNIQUE(email)

);

  CREATE TABLE IF NOT EXISTS attendance (
    id integer PRIMARY KEY,
    employee_id integer NOT NULL,
    posting_date  timestamp with time zone NOT NULL,

    CONSTRAINT attendance_employee_fk FOREIGN KEY (employee_id) REFERENCES employees (id) MATCH simple ON UPDATE cascade ON DELETE NO action


);


INSERT INTO admins
    (id, name, email, password)
SELECT 1, 'Admin', 'admin@encentral.com', 'admin'
WHERE
    NOT EXISTS (
        SELECT email FROM admins WHERE email = 'admin@encentral.com'
    );


CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 2;
