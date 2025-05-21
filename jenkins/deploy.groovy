// pipeline {
//     agent any

//     environment {
//         REPO_URL = 'https://github.com/zubayergh/hello_world_flask'
//         BRANCH = 'main'
//         PROJECT_DIR = "${env.WORKSPACE}/your-app"  // Adjust as needed
//     }

//     stages {
//         stage('Build') { 
//             steps {
//                 echo "Building......"
//             }
//         }

//         stage('Test') { 
//             steps {
//                 echo "testing......"
//             }
//         }

//         stage('Checkout Code') {
//             steps {
//                 script {
//                     sh """
//                     if [ -d "${env.PROJECT_DIR}" ]; then
//                         echo "📂 Pulling latest changes..."
//                         cd "${env.PROJECT_DIR}"
//                         git fetch origin ${env.BRANCH}
//                         git reset --hard origin/${env.BRANCH}
//                     else
//                         echo "📥 Cloning repository..."
//                         git clone -b ${env.BRANCH} ${env.REPO_URL} ${env.PROJECT_DIR}
//                     fi
//                     """
//                 }
//             }
//         }

//         stage('Deploy') { 
//             steps {
//                 dir("${env.PROJECT_DIR}") {
//                     sh """
//                     echo "🐳 Rebuilding Docker containers..."
//                     docker compose down
//                     docker compose up -d --build
//                     """
//                 }
//             }
//         }
//     }
// }

pipeline {
    agent any

    environment {
        APP_DIR = 'hello_world_flask'
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/zubayergh/hello_world_flask', branch: 'main'
            }
        }

        stage('Install Dependencies') {
            steps {
                sh '''
                    python3 --version
                    pip install -r requirements.txt
                '''
            }
        }

        stage('Run Tests') {
            steps {
                sh '''
                    echo "Running dummy test with pytest..."
                    pytest tests
                '''
            }
        }

        // Optional: Run Flask App with Docker
        /*
        stage('Build & Run with Docker') {
            steps {
                sh '''
                    docker build -t flask-app .
                    docker run -d -p 5050:5000 --name flask_app flask-app
                '''
            }
        }
        */
    }

    post {
        always {
            echo 'Cleaning up...'
            // Uncomment if using Docker
            // sh 'docker stop flask_app || true && docker rm flask_app || true'
        }
    }
}
