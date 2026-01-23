pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Jar') {
            steps {
                sh './gradlew clean bootJar'
            }
        }

        stage('Docker Build & Deploy') {
            steps {
                sh '''
                docker compose down
                docker compose up -d --build
                '''
            }
        }
    }

    post {
        success {
            echo '✅ Deploy SUCCESS'
        }
        failure {
            echo '❌ Deploy FAILED'
        }
    }
}
