CREATE TABLE depertment (id SERIAL PRIMARY KEY, name VARCHAR(100));


CREATE TABLE employee (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100),
  department_id INT REFERENCES depertment
);


INSERT INTO depertment (name)
SELECT format('depertment_%s', i)
FROM generate_series(1, 10) as i;


INSERT INTO employee (name, department_id)
SELECT format('employee_%s', i),
  i % 10 + 1
FROM generate_series(1, 100) as i;