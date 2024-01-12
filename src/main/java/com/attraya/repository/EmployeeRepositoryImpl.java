package com.attraya.repository;

import com.attraya.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(Employee employee) {
        return jdbcTemplate.update("INSERT INTO EMPLOYEES_DATA (name, dept, email, doj) VALUES (?, ?, ?, ?)",
                employee.getName(), employee.getDept(), employee.getEmail(), employee.getDoj());
    }

    /*
    Four Approaches:
    1. Using separate class that implements RowMapper
    2. Using Anonymous implementation
    3. Using Lambda Expression
    4. Using BeanPropertyRowMapper
     */
    @Override
    public List<Employee> findAll() {
        return jdbcTemplate.query("SELECT * FROM EMPLOYEES_DATA",
                (rs, rowNum)->
                    Employee.builder()
                            .id(rs.getInt("id"))
                            .name(rs.getString("name"))
                            .dept(rs.getString("dept"))
                            .email(rs.getString("email"))
                            .doj(rs.getDate("doj"))
                            .build());
    }

    @Override
    public List<Employee> getAllEmployees() {
        return jdbcTemplate.query("SELECT * FROM EMPLOYEES_DATA", new BeanPropertyRowMapper<>(Employee.class));
    }

    @Override
    public Employee findById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM EMPLOYEES_DATA WHERE id = ?",
                new BeanPropertyRowMapper<>(Employee.class), id);
    }

    @Override
    public String getNameById(int id) {
        return jdbcTemplate.queryForObject("SELECT name FROM EMPLOYEES_DATA WHERE id = ?",
                String.class, id);
    }

    @Override
    public List<Employee> findByNameAndDept(String name, String dept) {
        return jdbcTemplate.query("SELECT * FROM EMPLOYEES_DATA WHERE name = ? AND dept = ?",
                new BeanPropertyRowMapper<>(Employee.class), name, dept);
    }

    @Override
    public int update(Employee employee) {
        return jdbcTemplate.update("UPDATE EMPLOYEES_DATA SET name = ?, dept = ?, email = ?, doj = ? WHERE id = ?",
                employee.getName(), employee.getDept(), employee.getEmail(), employee.getDoj(), employee.getId());
    }

    @Override
    public int delete(int id) {
        return jdbcTemplate.update("DELETE FROM EMPLOYEES_DATA WHERE id = ?", id);
    }
}
