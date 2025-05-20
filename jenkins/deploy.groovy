pipeline {
    agent any

    environment {
        REPO_URL = 'https://github.com/zubayergh/hello_world_flask'
        BRANCH = 'main'
        PROJECT_DIR = '${env.WORKSPACE}/your-app'  // Adjust as needed
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
                    sh """
                    if [ -d "${env.PROJECT_DIR}" ]; then
                        echo "📂 Pulling latest changes..."
                        cd "${env.PROJECT_DIR}"
                        git fetch origin ${env.BRANCH}
                        git reset --hard origin/${env.BRANCH}
                    else
                        echo "📥 Cloning repository..."
                        git clone -b ${env.BRANCH} ${env.REPO_URL} ${env.PROJECT_DIR}
                    fi
                    """
                }
            }
        }

        stage('Deploy') { 
            steps {
                dir("${env.PROJECT_DIR}") {
                    sh """
                    echo "🐳 Rebuilding Docker containers..."
                    docker compose down
                    docker compose up -d
                    """
                }
            }
        }
    }
}
