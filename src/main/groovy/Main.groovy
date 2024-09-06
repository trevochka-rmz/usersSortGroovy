import groovy.json.JsonSlurper
import java.nio.file.Files
import java.nio.file.Paths

// URL к API
def url = "https://eltex-co.ru/test/users.php"

// Получаем данные из API
def connection = new URL(url).openConnection()
def response = connection.content.text

// Парсим ответ JSON
def jsonSlurper = new JsonSlurper()
def users = jsonSlurper.parseText(response)

// Фильтруем пользователей по salary > 3500
def filteredUsers = users.findAll { user -> user.salary > 3500 }

// Сортируем пользователей по имени
def sortedUsers = filteredUsers.sort { it.name }

// Формируем CSV
def csvData = new StringBuilder()
csvData.append("id,name,email,salary\n") // Заголовки столбцов
sortedUsers.each { user ->
  def id = user.id ?: "Пустая строка" // Если id пустой, записываем пустую строку
  def name = user.name ?: "Пустая строка" // Если name пустой, записываем пустую строку
  def email = user.email ?: "Пустая строка" // Если email пустой, записываем пустую строку
  def salary = user.salary ?: "0" // Если salary пустой, записываем пустую строку

  csvData.append("${id},${name},${email},${salary}\n")
}

// Записываем данные в файл
def outputFilePath = "users.csv"
Files.write(Paths.get(outputFilePath), csvData.toString().getBytes())

println "Данные сохранены в ${outputFilePath}"