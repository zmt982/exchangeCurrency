package org.currencyexchange.database.repository.impl;

import org.currencyexchange.database.entity.ExchangeRate;
import org.currencyexchange.database.repository.DataBaseUtils;
import org.currencyexchange.database.repository.ExchangeRateDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDaoImpl implements ExchangeRateDao {
    public Optional<List<ExchangeRate>> findAll() {
        List<ExchangeRate> rates = new ArrayList<>();
        String sql = "SELECT * FROM exchange_rates";

        try (Connection conn = DataBaseUtils.getConnection(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rates.add(new ExchangeRate(
                        rs.getLong("id"),
                        rs.getLong("basecurrency_id"),
                        rs.getLong("targetcurrency_id"),
                        rs.getBigDecimal("rate")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(rates);
    }

    public Optional<ExchangeRate> findByPairId(long baseCurrencyId, long targetCurrencyId) {
        ExchangeRate rate = new ExchangeRate();
        String sql = "SELECT * FROM exchange_rates WHERE basecurrency_id = " + baseCurrencyId +
                " AND targetcurrency_id = " + targetCurrencyId;

        try (Connection conn = DataBaseUtils.getConnection(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rate.setId(rs.getLong("id"));
                rate.setBaseCurrencyId(rs.getLong("basecurrency_id"));
                rate.setTargetCurrencyId(rs.getLong("targetcurrency_id"));
                rate.setRate(rs.getBigDecimal("rate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(rate);
    }

    public Optional<ExchangeRate> save(ExchangeRate rate) {
        String sql = "INSERT INTO exchange_rates (basecurrency_id, targetcurrency_id, rate) VALUES (?, ?, ?)";

        try (Connection conn = DataBaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, rate.getBaseCurrencyId());
            stmt.setLong(2, rate.getTargetCurrencyId());
            stmt.setBigDecimal(3, rate.getRate());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                return Optional.empty();
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    rate.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(rate);
    }

    public Optional<ExchangeRate> update(ExchangeRate rateToUpdate) {
        String sql = "UPDATE exchange_rates SET rate = ? WHERE id =?";

        try (Connection conn = DataBaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, rateToUpdate.getRate());
            stmt.setLong(2, rateToUpdate.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                return Optional.empty();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(rateToUpdate);
    }
}