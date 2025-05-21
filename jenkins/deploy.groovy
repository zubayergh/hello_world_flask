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
    agent {
        docker {
            image 'python:3.11'
        }
    }

    stages {
        stage('Clone Repo') {
            steps {
                checkout scm
            }
        }

        stage('Install Dependencies') {
            steps {
                sh 'pip install -r requirements.txt'
            }
        }

        stage('Run Tests') {
            steps {
                sh 'pytest'
            }
        }

        stage('Build Image') {
            steps {
                sh 'docker build -t my-flask-app .'
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker compose down && docker compose up -d'
            }
        }
    }
}