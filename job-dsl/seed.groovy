folder('lesson26-jenkins-demo') {
    description('Folder z zadaniami CI/CD tworzonymi automatycznie przez Job DSL.')
}

['develop', 'master', 'feature/*'].each { branchPattern ->
    pipelineJob("lesson26-jenkins-demo/build-and-test-${branchPattern.replace('/', '-').replace('*', 'all')}") {
        description("Kompilacja i testy dla gałęzi ${branchPattern}.")
        definition {
            cpsScm {
                scm {
                    git {
                        remote {
                            url('https://github.com/wojtekp2002/lesson26-jenkins-pipeline-homework.git')
                        }
                        branches(branchPattern)
                    }
                }
                scriptPath('Jenkinsfile')
            }
        }
        triggers {
            scm('H/5 * * * *')
        }
    }
}

pipelineJob('lesson26-jenkins-demo/deploy-test') {
    description('Wdrożenie na środowisko testowe.')
    parameters {
        stringParam('APP_VERSION', '1.0.0', 'Wersja aplikacji do wdrożenia.')
    }
    definition {
        cps {
            script('''
pipeline {
    agent any
    environment {
        DEPLOY_ENV = 'test'
    }
    stages {
        stage('Deploy test') {
            steps {
                echo "Deploying version ${params.APP_VERSION} to ${env.DEPLOY_ENV}"
            }
        }
    }
}
'''.stripIndent())
            sandbox()
        }
    }
}

pipelineJob('lesson26-jenkins-demo/deploy-prod') {
    description('Wdrożenie produkcyjne wymagające ręcznego zatwierdzenia.')
    parameters {
        stringParam('APP_VERSION', '1.0.0', 'Wersja aplikacji do wdrożenia.')
    }
    definition {
        cps {
            script('''
pipeline {
    agent any
    environment {
        DEPLOY_ENV = 'prod'
    }
    stages {
        stage('Manual approval') {
            steps {
                input message: "Deploy version ${params.APP_VERSION} to production?", ok: 'Approve deploy'
            }
        }
        stage('Deploy production') {
            steps {
                echo "Deploying version ${params.APP_VERSION} to ${env.DEPLOY_ENV}"
            }
        }
    }
}
'''.stripIndent())
            sandbox()
        }
    }
}
