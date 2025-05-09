package org.currencyexchange.database.repository.impl;

import org.currencyexchange.database.entity.Currency;
import org.currencyexchange.database.repository.CurrencyDao;
import org.currencyexchange.util.DataBaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDaoImpl implements CurrencyDao {

    public Optional<List<Currency>> findAll() {
        List<Currency> currencies = new ArrayList<>();
        String sql = "SELECT * FROM currencies";

        try (Connection conn = DataBaseUtils.getConnection(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                currencies.add(new Currency(
                        rs.getLong("id"),
                        rs.getString("code"),
                        rs.getString("fullname"),
                        rs.getString("sign")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(currencies);
    }

    public Optional<Currency> findById(long id) {
        String sql = "SELECT * FROM currencies WHERE id = ?";
        return findCurrency(sql, id);
    }

    public Optional<Currency> findByCode(String code) {
        String sql = "SELECT * FROM currencies WHERE code = ?";
        return findCurrency(sql, code);
    }

    private Optional<Currency> findCurrency(String sql, Object param) {
        Currency currency = null;
        try (Connection conn = DataBaseUtils.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (param instanceof Long) {
                stmt.setLong(1, (Long) param);
            } else if (param instanceof String) {
                stmt.setString(1, (String) param);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    currency = new Currency();
                    currency.setId(rs.getLong("id"));
                    currency.setCode(rs.getString("code"));
                    currency.setFullName(rs.getString("fullname"));
                    currency.setSign(rs.getString("sign"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(currency);
    }

    public Optional<Currency> save(Currency currency) {
        String sql = "INSERT INTO currencies (code, fullname, sign) VALUES (?, ?, ?)";

        try (Connection conn = DataBaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, currency.getCode());
            stmt.setString(2, currency.getFullName());
            stmt.setString(3, currency.getSign());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                return Optional.empty();
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    currency.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(currency);
    }
}