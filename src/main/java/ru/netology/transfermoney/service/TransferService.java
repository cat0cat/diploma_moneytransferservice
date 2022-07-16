package ru.netology.transfermoney.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.netology.transfermoney.exceptions.InputDataException;
import ru.netology.transfermoney.model.ConfirmOperationRequest;
import ru.netology.transfermoney.model.TransferResponse;
import ru.netology.transfermoney.model.TransferRequest;
import ru.netology.transfermoney.repository.TransferRepository;

import java.time.LocalDate;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TransferService {
    private final TransferRepository transferRepository;

    public TransferResponse transfer(TransferRequest transferRequest) {
        final String cardFrom = transferRequest.getCardFromNumber();
        final String cardTo = transferRequest.getCardToNumber();

        // проверка номеров карт
        if (cardFrom == null){
            throw new InputDataException("Номер карты отправителя обязателен");
        } else if (cardTo == null){
            throw new InputDataException("Номер карты получателя обязателен");
        } else if (!cardFrom.matches("[0-9]{16}")){
            throw new InputDataException("Номер карты отправителя должен быть 16 символов");
        } else if (!cardTo.matches("[0-9]{16}")){
            throw new InputDataException("Номер карты получателя должен быть 16 символов");
        }

        //проверка CVV
        final String cvv = transferRequest.getCardFromCVV();
        if (cvv == null) {
            throw new InputDataException("CVC / CVC2 номер карты отправителя обязателен");
        } else if (cvv.length()>0 && !cvv.matches("[0-9]{3}")) {
            throw new InputDataException("CVC / CVC2 код отправителя должен быть 3 символов");
        }

        // проверка корректности даты
        final String period =  transferRequest.getCardFromValidTill();
        StringBuilder sb = new StringBuilder(period);
        int cardMonth = Integer.parseInt(sb.substring(0, 2 ));
        if (cardMonth > 12){
            throw new InputDataException("Текущий месяц не может быть больше 12");
        }

        // проверка срока действия карты
        int cardYear = Integer.parseInt("20" + sb.substring(3, 5 ));
        if (LocalDate.now().getYear() >= cardYear) {
            if (LocalDate.now().getMonthValue() > cardMonth) {
                throw new InputDataException("Истекла дата действия карты отправителя");
            }
        } else {
            throw new InputDataException("Истекла дата действия карты отправителя");
        }

        // проверка заполнения суммы перевода
        final Integer amount = transferRequest.getAmount().getValue();
        if (amount <= 0) {
            throw new InputDataException("Необходимо указать сумму перевода");
        }

        final String transferId = Integer.toString(transferRepository.getOperationId());
        transferRepository.addTransfer(transferId, transferRequest);
        transferRepository.addCode(transferId, UUID.randomUUID().toString());
        return new TransferResponse(transferId);
    }

    public TransferResponse confirmOperation(ConfirmOperationRequest confirmOperationRequest) {
        final String operationId = confirmOperationRequest.getOperationId();

        final TransferRequest transferRequest = transferRepository.removeTransfer(operationId);
        if (transferRequest == null) {
            throw new InputDataException("Операция не найдена");
        }
        final String code = transferRepository.removeCode(operationId);
        return new TransferResponse(operationId);
    }
}
