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
                sh '''
                    # 🔑 Jenkins 실행 시점에 .env 생성
                    cat <<EOF > .env
MYSQL_USER=user
MYSQL_PASSWORD=password
MYSQL_DATABASE=techblog
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/techblog?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul
SPRING_DATASOURCE_USERNAME=user
SPRING_DATASOURCE_PASSWORD=password
EOF

                    docker-compose down
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