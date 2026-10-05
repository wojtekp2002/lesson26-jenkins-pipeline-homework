"""Small sample application used by the Jenkins CI/CD homework."""


def build_message(version: str, environment: str) -> str:
    return f"Application version {version} is ready for {environment}."


def health_check() -> dict[str, str]:
    return {"status": "ok", "service": "lesson26-jenkins-demo"}


if __name__ == "__main__":
    print(build_message("1.0.0", "local"))
    print(health_check())
