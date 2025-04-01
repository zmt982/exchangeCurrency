package org.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.currencyexchange.database.repository.impl.CurrencyDaoImpl;
import org.currencyexchange.database.repository.impl.ExchangeRateDaoImpl;
import org.currencyexchange.service.CurrencyExchangeService;
import org.currencyexchange.service.impl.CurrencyExchangeServiceImpl;
import org.currencyexchange.service.mapper.CurrencyMapper;
import org.currencyexchange.service.model.CurrencyExchangeDto;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/currency-exchanges")
public class CurrencyExchangeController extends HttpServlet {
    private final CurrencyExchangeService currencyExchangeService =
            new CurrencyExchangeServiceImpl(new CurrencyDaoImpl(), new CurrencyMapper(), new ExchangeRateDaoImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String baseCurrency = req.getParameter("baseCurrency");
        String targetCurrency = req.getParameter("targetCurrency");
        String amountString = req.getParameter("amount");
        BigDecimal amount = new BigDecimal(amountString);

        CurrencyExchangeDto exchange = currencyExchangeService.exchange(baseCurrency, targetCurrency, amount);
        String jsonResponse = new ObjectMapper().writeValueAsString(exchange);

        resp.getWriter().write(jsonResponse);
    }
}