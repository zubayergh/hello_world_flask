pipeline {
    agent any

    environment {
        REPO_URL = 'https://github.com/zubayergh/hello_world_flask'
        BRANCH = 'main'
        PROJECT_DIR = 'C:\\my-app'  // Adjust as needed
    }

    stages {
        stage('Build') { 
            steps {
                echo "Building......"
            }
        }

        stage('Test') { 
            steps {
                echo "testing......"
            }
        }

        stage('Checkout Code') {
            steps {
                script {
                    bat """
                    IF EXIST "${env.PROJECT_DIR}" (
                        echo Pulling latest changes...
                        cd /d "${env.PROJECT_DIR}"
                        git fetch origin ${env.BRANCH}
                        git reset --hard origin/${env.BRANCH}
                    ) ELSE (
                        echo Cloning repository...
                        git clone -b ${env.BRANCH} ${env.REPO_URL} "${env.PROJECT_DIR}"
                    )
                    """
                }
            }
        }

        stage('Deploy') { 
            steps {
                dir("${env.PROJECT_DIR}") {
                    bat """
                    echo Rebuilding Docker containers...
                    docker compose down
                    docker compose build --no-cache
                    docker compose up -d
                    """
                }
            }
        }
    }
}
