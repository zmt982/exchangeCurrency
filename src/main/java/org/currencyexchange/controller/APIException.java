package org.currencyexchange.controller;

public class APIException extends RuntimeException {
    private final int statusCode;
    private final String message;

    public APIException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    //400 Bad Request
    class BadRequestException extends APIException {
        public BadRequestException(String message) {
            super(400, message);
        }
    }

    // 404 Not Found
    class NotFoundException extends APIException {
        public NotFoundException(String message) {
            super(404, message);
        }
    }

    //409 Conflict
    class ConflictException extends APIException {
        public ConflictException(String message) {
            super(409, message);
        }
    }

    //500 Internal Server Error
    class DataBaseException extends APIException {
        public DataBaseException(String message) {
            super(500, message);
        }
    }

    // Исключения для валют
    class CurrencyNotFoundException extends NotFoundException {
        public CurrencyNotFoundException() {
            super("Валюта не найдена");
        }
    }

    class CurrencyAlreadyExistsException extends ConflictException {
        public CurrencyAlreadyExistsException() {
            super("Валюта с таким кодом уже существует");
        }
    }

    class MissingCurrencyCodeException extends BadRequestException {
        public MissingCurrencyCodeException() {
            super("Код валюты отсутствует в адресе");
        }
    }

    class MissingFormFieldException extends BadRequestException {
        public MissingFormFieldException() {
            super("Отсутствует нужное поле формы");
        }
    }

    // Исключения для обменных курсов
    class ExchangeRateNotFoundException extends NotFoundException {
        public ExchangeRateNotFoundException() {
            super("Обменный курс для пары не найден");
        }
    }

    class ExchangeRateAlreadyExistsException extends ConflictException {
        public ExchangeRateAlreadyExistsException() {
            super("Валютная пара с таким кодом уже существует");
        }
    }

    class MissingCurrencyPairException extends BadRequestException {
        public MissingCurrencyPairException() {
            super("Коды валют пары отсутствуют в адресе");
        }
    }

    class CurrencyPairNotFoundException extends NotFoundException {
        public CurrencyPairNotFoundException() {
            super("Одна (или обе) валюта из валютной пары не существует в БД");
        }
    }

}