package com.example.practice.spring6intoduction.ch8;

import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class MyJdbcTemplate {
    public static void main(String[] args) {
        // DataSource 설정
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/practice");
        dataSource.setUsername("root");
        dataSource.setPassword("Vkdvkd123!");

        //Jdbc template 생성
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        //한 레코드의 한 컬럼 가져오기
        String title = jdbcTemplate.queryForObject("SELECT title FROM training WHERE id=?", String.class, "t01");
        System.out.println("title = " + title);
        //여러개의 파라미터 지정하기
        String title2 = jdbcTemplate.queryForObject("SELECT title FROM training WHERE id=? AND title =?", String.class, "t01", "비지니스예절교육");
        System.out.println("title2 = " + title2);
        //날짜 형식으로 가져오기
        LocalDateTime startDateTime = jdbcTemplate.queryForObject("SELECT start_date_time FROM training WHERE id=?", LocalDateTime.class, "t01");
        System.out.println("startDateTime = " + startDateTime);

        //여러 레코드에서 한 컬럼 가져오기
        List<Integer> reserveds = jdbcTemplate.queryForList("SELECT reserved FROM training", Integer.class);
        for (Integer reserved : reserveds) {
            System.out.println("reserved = " + reserved);
        }

        //레코드를 Map 객체로 변환해서 가져오기
        Map<String, Object> map = jdbcTemplate.queryForMap("SELECT * FROM training WHERE id=?", "t01");
        System.out.println("map = " + map);

        //여러개의 레코드를 map으로 가져오기
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT * FROM training");
        for (Map<String, Object> stringObjectMap : maps) {
            for (Map.Entry<String, Object> stringObjectEntry : stringObjectMap.entrySet()) {
                System.out.print(stringObjectEntry + " ");
            }
            System.out.println();
        }

        //한 레코드를 Entity로 변환해서 가져오기
        Training training = jdbcTemplate.queryForObject("SELECT * FROM training WHERE id=?", new DataClassRowMapper<>(Training.class), "t01");
        /**
         * DataClassRowMapper 객체는 레코드의 값을 생성자에서 지정한 클래스의 객체로 자동 변환해줌
         */

        //갱신 계열 처리
        //jdbcTemplate.update("INSERT INTO training VALUES (?,?,?,?,?,?)", "t04", "Spring교육", "2021-12-01", "2021-12-03", 0, 8);
        jdbcTemplate.update("UPDATE training SET title=? WHERE id=?", "컴퓨터구조교육","t04");
        int count = jdbcTemplate.update("DELETE FROM training WHERE id=?", "t04");
        System.out.println("delete count = " + count);
    }
}
