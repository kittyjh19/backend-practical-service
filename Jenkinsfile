pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Prepare Env') {
            steps {
                sh '''
                cat <<EOF > .env
MYSQL_ROOT_PASSWORD=password
MYSQL_USER=user
MYSQL_PASSWORD=password
MYSQL_DATABASE=techblog

SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/techblog?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul
SPRING_DATASOURCE_USERNAME=user
SPRING_DATASOURCE_PASSWORD=password
EOF
                '''
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
                sh '''
                    docker-compose down --remove-orphans || true
                    docker-compose up -d --build
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
