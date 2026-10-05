pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }

    parameters {
        choice(name: 'TARGET_ENV', choices: ['test', 'prod'], description: 'Środowisko docelowe dla etapu Deploy')
        booleanParam(name: 'RUN_DEPLOY', defaultValue: true, description: 'Pozwala pominąć etap Deploy')
    }

    environment {
        APP_NAME = 'lesson26-jenkins-demo'
        APP_VERSION = '1.0.0'
        DEPLOY_ENV = "${params.TARGET_ENV}"
        NOTIFY_CHANNEL = 'console'
        REPOSITORY_URL = 'https://github.com/wojtekp2002/lesson26-jenkins-pipeline-homework.git'
        BUILD_FAILED = 'false'
        TEST_FAILED = 'false'
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    try {
                        echo "Checking out ${env.REPOSITORY_URL}"
                        checkout scm
                    } catch (Exception error) {
                        currentBuild.result = 'FAILURE'
                        echo "Checkout failed: ${error.getMessage()}"
                        error('Stopping pipeline because Checkout failed.')
                    }
                }
            }
        }

        stage('Build') {
            when {
                expression { currentBuild.result == null || currentBuild.result == 'SUCCESS' }
            }
            steps {
                script {
                    try {
                        echo "Building ${env.APP_NAME} version ${env.APP_VERSION}"
                        sh './scripts/build.sh'
                        archiveArtifacts artifacts: 'build/build-info.txt', fingerprint: true
                    } catch (Exception error) {
                        env.BUILD_FAILED = 'true'
                        currentBuild.result = 'FAILURE'
                        echo "Build failed: ${error.getMessage()}"
                        error('Stopping pipeline because Build failed.')
                    }
                }
            }
        }

        stage('Test') {
            when {
                allOf {
                    expression { env.BUILD_FAILED != 'true' }
                    expression { currentBuild.result == null || currentBuild.result == 'SUCCESS' }
                }
            }
            steps {
                script {
                    try {
                        echo "Running tests before deployment to ${env.DEPLOY_ENV}"
                        sh './scripts/test.sh'
                        archiveArtifacts artifacts: 'reports/test-output.txt', allowEmptyArchive: true
                    } catch (Exception error) {
                        env.TEST_FAILED = 'true'
                        currentBuild.result = 'FAILURE'
                        echo "Tests failed: ${error.getMessage()}"
                        error('Stopping pipeline because Test failed.')
                    }
                }
            }
        }

        stage('Deploy') {
            when {
                allOf {
                    expression { params.RUN_DEPLOY }
                    expression { env.BUILD_FAILED != 'true' }
                    expression { env.TEST_FAILED != 'true' }
                    expression { env.DEPLOY_ENV in ['test', 'prod'] }
                    anyOf {
                        branch 'main'
                        branch 'master'
                    }
                }
            }
            steps {
                script {
                    try {
                        if (env.DEPLOY_ENV == 'prod') {
                            timeout(time: 5, unit: 'MINUTES') {
                                input message: "Deploy ${env.APP_VERSION} to production?", ok: 'Deploy'
                            }
                        }

                        echo "Deploying ${env.APP_NAME} to ${env.DEPLOY_ENV}"
                        sh './scripts/deploy.sh'
                        archiveArtifacts artifacts: 'build/deploy-info.txt', fingerprint: true
                    } catch (Exception error) {
                        currentBuild.result = 'FAILURE'
                        echo "Deploy failed: ${error.getMessage()}"
                        error('Stopping pipeline because Deploy failed.')
                    }
                }
            }
        }
    }

    post {
        success {
            echo "SUCCESS: ${env.APP_NAME} ${env.APP_VERSION} finished for ${env.DEPLOY_ENV}."
        }
        failure {
            echo "FAILURE: ${env.APP_NAME} pipeline failed. Check logs and archived reports."
        }
        unstable {
            echo "UNSTABLE: ${env.APP_NAME} needs attention."
        }
        always {
            echo "Notification to ${env.NOTIFY_CHANNEL}: build ${currentBuild.currentResult}"
            cleanWs deleteDirs: true, notFailBuild: true
        }
    }
}
