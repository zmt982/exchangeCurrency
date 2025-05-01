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

@WebServlet(urlPatterns = {"/exchangeRates", "/exchangeRate/*"})
public class ExchangeRateController extends HttpServlet {
    private final ExchangeRateService exchangeRateService =
            new ExchangeRateServiceImpl(
                    new ExchangeRateDaoImpl(),
                    new CurrencyDaoImpl(),
                    new ExchangeRateMapper()
            );
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pair = req.getParameter("pair");
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
        String jsonResponse = objectMapper.writeValueAsString(exchangeRateToAdd);

        resp.getWriter().write(jsonResponse);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() < 6) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Currency pair must be specifies in URL");
            return;
        }

        String currencyPair = pathInfo.substring(1).toUpperCase();
        String rateString = req.getParameter("rate");
        if (rateString == null || rateString.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Rate parameter is required");
            return;
        }

        try {
            BigDecimal rate = new BigDecimal(rateString);

            ExchangeRateDto exchangeRateToUpdate = exchangeRateService.updateByPair(currencyPair, rate);
            resp.setStatus(HttpServletResponse.SC_OK);
            String jsonResponse = objectMapper.writeValueAsString(exchangeRateToUpdate);

            resp.getWriter().write(jsonResponse);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid rate format");
        } catch (RuntimeException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }
}