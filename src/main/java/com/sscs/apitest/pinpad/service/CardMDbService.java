package com.sscs.apitest.pinpad.service;

import com.sscs.apitest.pinpad.config.SqlConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CardMDbService {

    @Autowired
    private SqlConfig sqlConfig;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(rollbackFor={Exception.class})
    public void resetCardToInitPin(String card) throws Exception {

        String sql = sqlConfig.getSql("UPDATE_CARD_MASTER_TO_PIN");
        jdbcTemplate.update(sql, new Object[]{card});

        sql = sqlConfig.getSql("UPDATE_CARD_MASTER_WK_TO_PIN");
        jdbcTemplate.update(sql, new Object[]{card});

        sql = sqlConfig.getSql("DELETE_CIN_TO_PIN");
        jdbcTemplate.update(sql, new Object[]{card});

        sql = sqlConfig.getSql("DELETE_PVV_TB_TO_PIN");
        jdbcTemplate.update(sql, new Object[]{card});

        sql = sqlConfig.getSql("DELETE_CUST_TXN_AGGREGATE_TO_PIN");
        jdbcTemplate.update(sql, new Object[]{card});

        sql = sqlConfig.getSql("DELETE_CARD_HISTORY_TO_PIN");
        jdbcTemplate.update(sql, new Object[]{card});

        sql = sqlConfig.getSql("DELETE_CARDACCT_HISTORY_TO_PIN");
        jdbcTemplate.update(sql, new Object[]{card});

        sql = sqlConfig.getSql("DELETE_ANNUAL_FEE_PAID_TO_PIN");
        jdbcTemplate.update(sql, new Object[]{card});

        sql = sqlConfig.getSql("DELETE_PARENTCHILD_TO_PIN");
        jdbcTemplate.update(sql, new Object[]{card, card});

    }

}
