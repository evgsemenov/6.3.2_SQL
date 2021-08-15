Запуск SUT

java -jar artifacts/app-deadline.jar -P:jdbc.url=jdbc:mysql://localhost:3306/db -P:jdbc.user=user -P:jdbc.password=pass


Запуск консольного клиента SQL

docker-compose exec mysql mysql -u user -p db