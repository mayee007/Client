pipeline {
    agent { node { label 'dd' } }
    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'yelekeri@gmail.com', url: 'https://github.com/mayee007/Client.git']]]) 
            }
        }
        stage('Postman Run') {
            steps {
                sh """ 
newman run --disable-unicode \
src/test/InfoClient.postman_collection.json \
-e src/test/PROD.postman_environment.json \
-r htmlextra \
--reporter-htmlextra-export newman/index.html 
 """ 
            }
        } //end of postman 
        stage('Publish') {
            steps {
                publishHTML (target: [
		  allowMissing: false,
		  alwaysLinkToLastBuild: false,
		  keepAll: true,
		  reportDir: 'newman',
		  reportFiles: 'index.html',
		  reportName: "Newman Test Status"
		])
            }
        } //end of publish 
    } // end of stages 
    
    post {
        failure {
            script {
                emailext mimeType: 'text/html', 
                         to: 'yelekeri@gmail.com', 
                         subject: 'build failure', 
                         body: 'mail from jenkins'
            }
        } // failure
         success {
             script {
                emailext attachmentsPattern: 'newman/index.html',
                        mimeType: 'text/html', 
                        to: 'yelekeri@gmail.com', 
                        subject: 'build success, ', 
                        body: 'mail from jenkins'
             }
         } // success
    }
    
}