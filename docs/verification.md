# Verification

Lokalne sprawdzenie projektu:

```bash
./scripts/build.sh
./scripts/test.sh
./scripts/deploy.sh
```

Sprawdzono, że:

- aplikacja testowa kompiluje się przez `compileall`,
- testy jednostkowe przechodzą,
- skrypt deploy generuje symulowany raport wdrożenia,
- `Jenkinsfile` zawiera etapy `Checkout`, `Build`, `Test`, `Deploy`,
- `Jenkinsfile` zawiera bloki `try-catch`, warunki `when`, zmienne `environment` i sekcję `post`,
- `job-dsl/seed.groovy` definiuje folder oraz zadania build/test i deploy.
