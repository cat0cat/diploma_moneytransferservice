# Курсовой проект "Сервис перевода денег"

## Описание проекта

Разработано приложение - REST-сервис. Сервис предоставляет интерфейс для перевода денег с одной карты на другую по заранее описанной [спецификации](MoneyTransferServiceSpecification.yaml). Заранее подготовленное веб-приложение (FRONT) должно подключается к разработанному сервису без доработок и использует его функционал для перевода денег.

## Описание приложения

- Сервис предоставляет REST интерфейс для интеграции с FRONT
- Сервис реализовывает все методы перевода с одной банковской карты на другую, описанные в [спецификации](MoneyTransferServiceSpecification.yaml)
- Все изменения записываются в файл (лог переводов в произвольном формате с указанием даты, времени, карта с которой было списание, карта зачисления, сумма, комиссия, результат операции если был)

## Реализация

- Приложение разработано с использованием Spring Boot
- Использован сборщик пакетов gradle/maven
- Для запуска используется docker, docker-compose
- Код размещен на github
- Код покрыт unit тестами с использованием mockito
- Добавлены интеграционные тесты с использованием testcontainers


## Описание интеграции с FRONT
FRONT доступен по адресу https://github.com/serp-ya/card-transfer, можно выкачать репозиторий и запустить nodejs приложение локально
(в описании репозитория фронта добавлена информация как запустить) или использовать уже развернутое демо приложение по адресу https://serp-ya.github.io/card-transfer/ (тогда ваш api должен быть запущен по адресу http://localhost:5500/).