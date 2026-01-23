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
                sh '''
                    chmod +x gradlew
                    ./gradlew clean bootJar
                '''
            }
        }

        stage('Docker Build & Deploy') {
            steps {
                withCredentials([
                    string(credentialsId: 'MYSQL_USER', variable: 'MYSQL_USER'),
                    string(credentialsId: 'MYSQL_PASSWORD', variable: 'MYSQL_PASSWORD'),
                    string(credentialsId: 'MYSQL_DATABASE', variable: 'MYSQL_DATABASE'),
                    string(credentialsId: 'SPRING_DATASOURCE_URL', variable: 'SPRING_DATASOURCE_URL'),
                    string(credentialsId: 'SPRING_DATASOURCE_USERNAME', variable: 'SPRING_DATASOURCE_USERNAME'),
                    string(credentialsId: 'SPRING_DATASOURCE_PASSWORD', variable: 'SPRING_DATASOURCE_PASSWORD')
                ]) {
                    sh '''
                        docker-compose down
                        docker-compose up -d --build
                    '''
                }
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