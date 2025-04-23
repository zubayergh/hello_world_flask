pipeline {
    agent any

    triggers {
        // Optional if you want periodic checks. Otherwise rely on GitHub webhook
        // pollSCM('H/5 * * * *')
    }

    options {
        skipDefaultCheckout true
    }

    stages {
        stage('Build') {
            when {
                changeRequest(target: 'main') // Triggers only on PRs to main
            }
            steps {
                echo "Building Pull Request to main branch..."
                // Add build steps here
            }
        }

        stage('Test') {
            when {
                changeRequest(target: 'main')
            }
            steps {
                echo "Running tests on PR to main..."
                // Add test steps here
            }
        }
    }
}