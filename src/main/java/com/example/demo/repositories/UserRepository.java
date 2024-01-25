package com.example.demo.repositories;

import com.example.demo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Получение списка пользователей из базы данных
     *
     * @return
     */
    public List<User> findAll() {
        String sql = "SELECT * FROM userTable";
        RowMapper<User> userRowMapper = (r, i) -> {
            User rowObject = new User();
            rowObject.setId(r.getInt("id"));
            rowObject.setFirstName(r.getString("firstName"));
            rowObject.setLastName(r.getString("lastName"));
            return rowObject;
        };
        return jdbc.query(sql, userRowMapper);
    }

    /**
     * Запись пользоветля в базу
     *
     * @param user
     * @return
     */
    public User save(User user) {
        String sql = "INSERT INTO userTable (firstName,lastName) VALUES (?, ?)";
        jdbc.update(sql, user.getFirstName(), user.getLastName());
        return user;
    }

    /**
     * Удаление пользоветля по id
     *
     * @param id
     */
    public void deleteById(int id) {
        String sql = "DELETE FROM userTable WHERE id=?";
        jdbc.update(sql, id);
    }

    /**
     * Получить user из базы по id
     *
     * @param id
     * @return - возвращает null если нет записей в базе
     */
    public User getById(int id) {
        if (!isExistUserById(id)) return null;//если нет записи с таким id - то отдаем null
        String sql = "SELECT id,firstName,lastName FROM userTable WHERE id = ?";
        return jdbc.queryForObject(sql,
                (resultSet, rowNum) -> {
                    User newUser = new User();
                    newUser.setId(Integer.parseInt(resultSet.getString("id")));
                    newUser.setFirstName(resultSet.getString("firstName"));
                    newUser.setLastName(resultSet.getString("lastName"));
                    return newUser;
                },
                id);
    }

    /**
     * Обновить пользователя user, ключ - id
     *
     * @param user
     */
    public void updateUser(User user) {
        if (isExistUserById(user.getId())) { //если есть user с таким же id,  то обновляем
            String sql = "update userTable set firstName = ?, lastName = ? where id = ?";
            jdbc.update(sql,
                    user.getFirstName(),
                    user.getLastName(),
                    user.getId());
        }
    }


    /**
     * Проверка существования записи
     *
     * @param id
     * @return true если есть запись с указанным id
     */
    private boolean isExistUserById(int id) {
        String sql = "Select count(*) from userTable where id = ?";
        int countRow = jdbc.queryForObject(sql, Integer.class, id);
        return countRow > 0;
    }
}