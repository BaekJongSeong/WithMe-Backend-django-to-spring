package com.server.withme.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.server.withme.entity.SafeZone;

import lombok.RequiredArgsConstructor;
/*

@RequiredArgsConstructor
public class SafeZoneRepositoryImpl implements SafeZoneRepository{

    private final JdbcTemplate jdbcTemplate;

    @Value("${batchSize}")
    private int batchSize;

    @Override
    public void saveAll(List<SafeZone> items) {
        int batchCount = 0;
        List<SafeZone> subItems = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            subItems.add(items.get(i));
            if ((i + 1) % batchSize == 0) {
                batchCount = batchInsert(batchSize, batchCount, subItems);
            }
        }
        if (!subItems.isEmpty()) {
            batchCount = batchInsert(batchSize, batchCount, subItems);
        }
        System.out.println("batchCount: " + batchCount);
    }

    private int batchInsert(int batchSize, int batchCount, List<SafeZone> subItems) {
        jdbcTemplate.batchUpdate("INSERT INTO ITEM_JDBC (`NAME`, `DESCRIPTION`) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, "sdf");//subItems.get(i).getName());
                        ps.setString(2, "sdf");//subItems.get(i).getDescription());
                    }
                    @Override
                    public int getBatchSize() {
                        return subItems.size();
                    }
                });
        subItems.clear();
        batchCount++;
        return batchCount;
    }
}
*/