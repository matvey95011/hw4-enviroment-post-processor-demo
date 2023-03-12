# Домашняя работа #4

## Задание
Реализовать загрузку property-файлов с файловой системы и из classpath в общий пул properties Spring'а.

## Требования
1. Загрузить все property-файлы из папки config на файловой системе рядом с jar-файлом и в classpath.
1. Файлы загружать в алфавитном порядке.
1. Если в параметрах содержится переменная, то вычислить ее на основе остальных переменных.

## Сборка приложения 
```shell script
# загружает gradle wrapper 6.6
./gradlew wrapper

# сборка проекта, прогон unit-тестов
./gradlew clean test 
```

##  Комментарии
1. Boot yourself, Spring is coming [Часть 1](https://habr.com/ru/company/jugru/blog/424503/), [Часть 2](https://habr.com/ru/company/jugru/blog/425333/).
1. [Customize the Environment or ApplicationContext Before It Starts](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-customize-the-environment-or-application-context)
1. [Application Events and Listeners](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-application-events-and-listeners)

##  Как сдавать?
* Fork этого репозитория
* Merge request вашей реализации в этот репозиторий

## Дедлайн
