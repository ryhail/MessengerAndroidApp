# Требования к проекту
## 1 Введение
Messanger – это мобильное приложение для обмена сообщениями с
другими пользователями. Приложение позволяет находить других
пользователей и добавлять их в свои контакты, создавать с ними
чаты, создавать групповые чаты с несколькими пользователями,
редактировать профиль и личную информацию. Приложение не
будет поддерживать звонки другим пользователям.

## 2 Требования пользователя
### 2.1 Программные интерфейсы
Система будет разработана с использованием следующих систем и
библиотек:
- Spring boot
- Spring cloud
- Spring security
- PostgreSQL

### 2.2 Интерфейс пользователя
Интерфейс – мобильное приложение, которое включает следующие
страницы:
- Страница входа в аккаунт/создания аккаунта
- Список текущих чатов пользователя
- Страница чата для отправки сообщений
- Боковое меню приложения
- Профиль пользователя
- Поиск других пользователей

2.2.1 **Вход в аккаунт**  
![Аутентификация](mocks/login_page.png)

2.2.2 **Регистрация**  
![Регистрация](mocks/register_page.png)

2.2.3 **Список чатов**  
![Список чатов](mocks/chat_list.png)

2.2.4 **Чат**  
![Чат](mocks/chat.png)

2.2.5 **Профиль пользователя**  
![Профиль](mocks/user_profile.png)

2.2.6 **Боковое меню**  
![Меню](mocks/side_menu.png)

2.2.7 **Поиск пользователей**  
![Поиск](mocks/search_users.png)

### 2.3 Характеристики пользователей
Пользователями приложения могут быть любые люди, которым
необходимо быстрая связь посредством текстовых сообщений. Для
использования приложения не требуются особые навыки обращения
с мобильными приложениями.
### 2.4 Предположения и зависимости
Для использования приложения необходимо:
• Интернет-соединение
• Телефон с операционной системой Android
## 3 Системные требования
Приложение должно иметь понятный и удобный интерфейс, а также
стабильно работать, не допуская потерь и искажений сообщений
пользователей.
### 3.1 Функциональные требования
1. Возможность поиска других пользователей и создания чатов
   с ними.
2. Возможность редактировать профиль пользователя
3. Сохранение истории переписки и времени каждого
   сообщения
4. Аутентификация и регистрация пользователей 
### 3.2 Нефункциональные требования
#### 3.2.1 АТРИБУТЫ КАЧЕСТВА
- Безопасность: аккаунт пользователя и личная информация
- должны быть защищены.
- Приватность: все чаты пользователя должны быть защищены от посторонних. Должна быть возможность очистить историю
 переписки.
- Скорость: сообщения должны отправлять без существенных задержек.