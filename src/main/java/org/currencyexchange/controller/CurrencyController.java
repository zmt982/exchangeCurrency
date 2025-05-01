package org.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.currencyexchange.database.repository.impl.CurrencyDaoImpl;
import org.currencyexchange.service.CurrencyService;
import org.currencyexchange.service.impl.CurrencyServiceImpl;
import org.currencyexchange.service.mapper.CurrencyMapper;
import org.currencyexchange.service.model.CurrencyDto;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrencyController extends HttpServlet {
    private final CurrencyService currencyService =
            new CurrencyServiceImpl(new CurrencyDaoImpl(), new CurrencyMapper());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String code = req.getParameter("code");
        String jsonResponse;

        if (code != null) {
            CurrencyDto currencyDto = currencyService.getCurrencyByCode(code);
            jsonResponse = objectMapper.writeValueAsString(currencyDto);
        } else {
            List<CurrencyDto> currencies = currencyService.getAllCurrencies();
            jsonResponse = objectMapper.writeValueAsString(currencies);
        }

        resp.getWriter().write(jsonResponse);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");

        CurrencyDto currencyToAdd = currencyService.add(name, code, sign);
        String jsonResponse = objectMapper.writeValueAsString(currencyToAdd);

        resp.getWriter().write(jsonResponse);
    }
}