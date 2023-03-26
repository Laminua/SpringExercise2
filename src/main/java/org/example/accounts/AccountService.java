package org.example.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AccountService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<UserProfile> index() {
        return jdbcTemplate.query("SELECT * FROM UserProfile", new BeanPropertyRowMapper<>(UserProfile.class));
    }

    public void addUser(UserProfile profile) {
        int id = profile.hashCode();
        jdbcTemplate.update("INSERT INTO UserProfile VALUES(?, ?, ?)",
                id, profile.getName(), profile.getEmail());
    }

    public UserProfile getUserById(int id) {
        return jdbcTemplate.query("SELECT * FROM UserProfile WHERE id=?", new Object[]{id},
                        new BeanPropertyRowMapper<>(UserProfile.class))
                .stream()
                .findAny()
                .orElse(null);
    }

    public void updateUser(int id, UserProfile profile) {
        jdbcTemplate.update("UPDATE UserProfile SET name=?, email=? WHERE id=?",
                profile.getName(), profile.getEmail(), id);
    }

    public void deleteUserById(int id) {
        jdbcTemplate.update("DELETE FROM UserProfile WHERE id=?", id);
    }
}
