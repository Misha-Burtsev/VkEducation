# Практическое задание 6 — Clean Architecture

Ветка `hw6-clean-architecture` продолжает `hw4-showcase`.

## Открытие и запуск

В Android Studio нужно открыть **`hw4-showcase/`**, а не корень репозитория: в корне находится исходный проект предыдущих заданий.
Нужны JDK 17 или новее и Android SDK 35. При необходимости укажите путь к SDK в `hw4-showcase/local.properties` (`sdk.dir=...`).

```sh
cd hw4-showcase
./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

## Слои

Исходники находятся в `hw4-showcase/app/src/main/java/io/mmaltsev/vkeducation/`.

- `data/dto`: `AppDto`, `AppDetailsDto`; категория представлена строкой, как во внешних данных.
- `data/source`: `LocalAppsDataSource` хранит захардкоженный каталог и демонстрационную карточку в DTO.
- `data/mapper`: `AppMapper` преобразует DTO в доменные модели, включая категорию.
- `data/repository`: `LocalAppsRepository` получает DTO и возвращает результат Mapper.
- `domain/model`: `App`, `AppDetails`, `Category` — модели без Android-зависимостей.
- `domain/repository`: интерфейс `AppsRepository`.
- `presentation`: Compose-вёрстка, навигация, `AppListViewModel`, `AppDetailsViewModel` и состояние карточки.
- `di`: сборка зависимостей и фабрика ViewModel; превью экранов используют тот же источник данных.

Поток данных: `LocalAppsDataSource → DTO → AppMapper → domain model → Repository → ViewModel → UI`.
Зависимости: `data → domain`, `presentation → domain`; `di` связывает реализации. Activity использует фабрику из `di`.

ViewModel получают Repository через конструктор и отдают неизменяемый `StateFlow`. Экраны подписываются с учётом lifecycle в навигационном узле и получают готовое состояние и обработчики событий. Состояние раскрытия описания хранится во ViewModel и переживает пересоздание Activity. Локальный источник синхронный и не выполняет I/O, поэтому корутины загрузки и состояния сетевых ошибок здесь не нужны.

## Поведение

Сохранены список из девяти приложений, демонстрационная карточка «Гильдия Героев», витрина и прежние переходы. Как и в ДЗ 4, нажатие на любой элемент списка открывает одну демонстрационную карточку, а нажатие в витрине возвращает к списку. Загрузка карточки по ID не входит в это задание.

## Самопроверка

- [x] Созданы слои `data`, `domain`, `presentation`.
- [x] Вёрстка и ViewModel находятся в `presentation`.
- [x] Данные вынесены в `data` и сначала представлены DTO.
- [x] Repository возвращает доменные модели через Mapper.
- [x] Domain не зависит от Android, UI и Data.
- [x] Unit-тесты проверяют преобразование полей и категорий, каталог Repository и состояние ViewModel с подставным Repository.

Ручная проверка: открыть список, перейти в карточку, раскрыть описание, повернуть экран, пролистать витрину и нажать на её элемент. Список должен открыться без дублирования экранов в стеке.

Для сдачи: открыть PR из `hw6-clean-architecture` в `master` и приложить ссылки на репозиторий и PR.
