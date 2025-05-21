pipeline {
    agent any

    environment {
        REPO_URL = 'https://github.com/zubayergh/hello_world_flask'
        BRANCH = 'main'
        PROJECT_DIR = "${env.WORKSPACE}/hello_world_flask"  // Adjust as needed
    }

    stages {

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
                        git clone https://github.com/zubayergh/hello_world_flask
                        cd hello_world_flask
                    fi
                    """
                }
            }
        }

        stage('Build') { 
            steps {
                echo "Building......"
            }
        }

        stage('Test') { 
            steps {
                sh """
                apt install -y python3-pip
                echo "testing......"
                pip install -r requirements.txt
                """
            }
        }

        stage('Deploy') { 
            steps {
                dir("${env.PROJECT_DIR}") {
                    sh """
                    echo "🐳 Rebuilding Docker containers..."
                    docker compose down
                    docker compose up -d --build
                    """
                }
            }
        }
    }
}
