CREATE TABLE department (id SERIAL PRIMARY KEY, name VARCHAR(100));
CREATE TABLE employee (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100),
  department_id INT REFERENCES department
);
INSERT INTO department (name)
SELECT format('department_%s', i)
FROM generate_series(1, 10) as i;
INSERT INTO employee (name, department_id)
SELECT format('employee_%s', i),
  i % 10 + 1
FROM generate_series(1, 100) as i;