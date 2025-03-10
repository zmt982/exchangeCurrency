package org.currencyexchange.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.currencyexchange.database.repository.impl.CurrencyDaoImpl;
import org.currencyexchange.service.impl.CurrencyServiceImpl;
import org.currencyexchange.service.mapper.CurrencyMapper;
import org.currencyexchange.service.model.CurrencyDto;

import java.io.IOException;
import java.util.List;

@WebServlet("/currency")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyDaoImpl currencyDao = new CurrencyDaoImpl();
    private final CurrencyMapper currencyMapper = new CurrencyMapper();
    private final CurrencyServiceImpl currencyServiceImpl = new CurrencyServiceImpl(currencyDao, currencyMapper);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");

        String action = request.getParameter("action");
        String currencyCode = request.getParameter("code");
        try {
            if ("list".equals(action)) {
                doGetList(response); // получить список всех валют
            } else if (currencyCode != null) {
                doGetByCode(currencyCode, response); // получить валюту по коду
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid request: specify action=list or code=<currency_code");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void doGetList(HttpServletResponse response) throws IOException {
        List<CurrencyDto> currencies = currencyServiceImpl.getAllCurrencies();
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("Available currencies: " + currencies);
    }

    private void doGetByCode(String currCode, HttpServletResponse response) throws IOException {
        CurrencyDto currencyCode = currencyServiceImpl.getCurrencyByCode(currCode);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("org.currencyexchange.database.entity.Currency: " + currencyCode);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            String name = request.getParameter("name");
            response.getWriter().println("Created item: " + name);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }
}