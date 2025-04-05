package org.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.currencyexchange.database.repository.impl.CurrencyDaoImpl;
import org.currencyexchange.database.repository.impl.ExchangeRateDaoImpl;
import org.currencyexchange.service.ExchangeRateService;
import org.currencyexchange.service.impl.ExchangeRateServiceImpl;
import org.currencyexchange.service.mapper.ExchangeRateMapper;
import org.currencyexchange.service.model.ExchangeRateDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/exchangeRate")
public class ExchangeRateController extends HttpServlet {
    private final ExchangeRateService exchangeRateService =
            new ExchangeRateServiceImpl(new ExchangeRateDaoImpl(), new CurrencyDaoImpl(), new ExchangeRateMapper());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pair = req.getParameter("pair");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse;

        if (pair != null) {
            ExchangeRateDto exchangeRateDto = exchangeRateService.getByCodePair(pair);
            jsonResponse = objectMapper.writeValueAsString(exchangeRateDto);
        } else {
            List<ExchangeRateDto> rates = exchangeRateService.getAllRates();
            jsonResponse = objectMapper.writeValueAsString(rates);
        }

        resp.getWriter().write(jsonResponse);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rateString = req.getParameter("rate");
        BigDecimal rate = new BigDecimal(rateString);

        ExchangeRateDto exchangeRateToAdd = exchangeRateService.add(baseCurrencyCode, targetCurrencyCode, rate);
        String jsonResponse = new ObjectMapper().writeValueAsString(exchangeRateToAdd);

        resp.getWriter().write(jsonResponse);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pair = req.getParameter("pair");
        String rateString = req.getParameter("rate");
        BigDecimal rate = new BigDecimal(rateString);

        ExchangeRateDto exchangeRateToUpdate = exchangeRateService.updateByPair(pair, rate);
        String jsonResponse = new ObjectMapper().writeValueAsString(exchangeRateToUpdate);

        resp.getWriter().write(jsonResponse);
    }
}