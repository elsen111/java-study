package com.customer_crud.backend.repositories;

import com.customer_crud.backend.models.Customer;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerRepository {

    private final JdbcTemplate jdbcTemplate;

    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Customer> findAll() {
        String sql = "SELECT * FROM customers";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Customer.class));
    }

    public Optional<Customer> findById(Integer id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try {
            Customer customer = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Customer.class), id);
            return Optional.ofNullable(customer);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Customer save(Customer customer) {
        if (customer.getId() != null && findById(customer.getId()).isPresent()) {
            String sql = "UPDATE customers SET name = ?, surname = ?, age = ? WHERE id = ?";
            jdbcTemplate.update(sql, customer.getName(), customer.getSurname(), customer.getAge(), customer.getId());
        } else {
            String sql = "INSERT INTO customers (name, surname, age) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, customer.getName(), customer.getSurname(), customer.getAge());
        }
        return customer;
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM customers WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
