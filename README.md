# Lesson 26 - Jenkins CI/CD Pipeline Homework

Projekt realizuje dwa zadania z lekcji 26:

1. zaawansowany pipeline Jenkins z obsługą błędów i powiadomieniami,
2. dodatkowy skrypt Job DSL tworzący środowisko CI/CD.

## Struktura

```text
.
├── Jenkinsfile
├── README.md
├── app/
│   ├── app.py
│   └── test_app.py
├── job-dsl/
│   └── seed.groovy
└── scripts/
    ├── build.sh
    ├── deploy.sh
    └── test.sh
```

## Zadanie 1: Jenkinsfile

`Jenkinsfile` definiuje pipeline składający się z etapów:

- `Checkout` - pobiera kod z repozytorium,
- `Build` - uruchamia symulowaną kompilację przez `scripts/build.sh`,
- `Test` - uruchamia testy przez `scripts/test.sh`,
- `Deploy` - wykonuje symulowane wdrożenie przez `scripts/deploy.sh`.

Pipeline używa zmiennych środowiskowych:

- `APP_NAME`,
- `APP_VERSION`,
- `DEPLOY_ENV`,
- `NOTIFY_CHANNEL`,
- `REPOSITORY_URL`,
- `BUILD_FAILED`,
- `TEST_FAILED`.

Etapy mają obsługę błędów przez `try-catch`. Jeżeli etap zakończy się błędem,
pipeline ustawia wynik na `FAILURE`, zapisuje informację w logu i zatrzymuje dalsze
wykonanie. Etapy `Test` i `Deploy` są wykonywane warunkowo, zależnie od wyniku
poprzednich etapów.

Etap `Deploy` działa tylko gdy:

- parametr `RUN_DEPLOY` jest ustawiony na `true`,
- build i testy nie zgłosiły błędu,
- gałąź to `main` albo `master`,
- `DEPLOY_ENV` ma wartość `test` albo `prod`.

Dla środowiska `prod` pipeline wymaga ręcznego zatwierdzenia przez krok `input`.

Sekcja `post` działa dla całego pipeline'u:

- `success` - wypisuje komunikat sukcesu,
- `failure` - wypisuje komunikat błędu,
- `unstable` - wypisuje komunikat ostrzegawczy,
- `always` - symuluje powiadomienie i czyści workspace.

## Zadanie 2: Job DSL

Plik `job-dsl/seed.groovy` tworzy:

- folder `lesson26-jenkins-demo`,
- zadania build/test dla gałęzi `develop`, `master` i `feature/*`,
- zadanie `deploy-test`,
- zadanie `deploy-prod` z ręcznym zatwierdzeniem.

## Jak uruchomić w Jenkinsie

1. Utwórz w Jenkinsie zadanie typu Pipeline lub Multibranch Pipeline.
2. Jako repozytorium ustaw:

```text
https://github.com/wojtekp2002/lesson26-jenkins-pipeline-homework.git
```

3. Jako ścieżkę skryptu ustaw `Jenkinsfile`.
4. Uruchom pipeline z parametrami:

```text
TARGET_ENV = test
RUN_DEPLOY = true
```

Dla produkcji ustaw `TARGET_ENV = prod`; pipeline poprosi wtedy o ręczne zatwierdzenie.

## Jak uruchomić Job DSL

W Jenkinsie zainstaluj plugin **Job DSL**, utwórz seed job i wskaż plik:

```text
job-dsl/seed.groovy
```

Po uruchomieniu seed joba Jenkins utworzy folder i zadania opisane w sekcji Job DSL.

## Lokalne sprawdzenie skryptów

Skrypty pomocnicze można uruchomić lokalnie:

```bash
APP_VERSION=1.0.0 DEPLOY_ENV=test ./scripts/build.sh
APP_VERSION=1.0.0 DEPLOY_ENV=test ./scripts/test.sh
APP_VERSION=1.0.0 DEPLOY_ENV=test ./scripts/deploy.sh
```
