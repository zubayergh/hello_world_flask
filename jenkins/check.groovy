pipeline {
    agent any

    environment {
        GITHUB_TOKEN = credentials('github')  // stored in Jenkins Credentials
        PYTHONUNBUFFERED = 1
    }

    stages {
        stage('Validate PR') {
            when {
                expression {
                    def isPR = env.CHANGE_ID != null
                    def hasTestTag = env.CHANGE_TITLE?.toLowerCase().contains('test') || env.CHANGE_DESCRIPTION?.toLowerCase().contains('test')
                    return isPR && hasTestTag
                }
            }
            steps {
                echo "This PR has 'test' tag. Proceeding with test pipeline."
            }
        }

        stage('Run Pytest') {
            echo "Running pytest"
        }

        stage('Auto Merge PR') {
            when {
                expression { return env.CHANGE_ID != null }
            }
            steps {
                script {
                    def repo = env.CHANGE_URL.split('.com/')[1].replace('.git', '')
                    def prNumber = env.CHANGE_ID
                    def apiUrl = "https://api.github.com/repos/${repo}/pulls/${prNumber}/merge"

                    echo "Attempting to auto-merge PR #${prNumber}..."

                    sh """
                    curl -s -X PUT -H "Authorization: token ${GITHUB_TOKEN}" \
                        -H "Accept: application/vnd.github+json" \
                        ${apiUrl} \
                        -d '{"commit_title": "Auto-merge after test success", "merge_method": "merge"}'
                    """
                }
            }
        }
    }

    post {
        failure {
            echo "Tests failed. PR will not be merged."
        }
    }
}
