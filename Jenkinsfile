timestamps {

node ('dd') { 

	stage ('Names/Client_Postman - Checkout') {
	 agent { node { label 'dd' }} 
 	 checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'yelekeri@gmail.com', url: 'https://github.com/mayee007/Client.git']]]) 
	} // end of checkout stage 
	
	stage ('Names/Client_Postman - Build') {
	agent { node { label 'dd' }} 
 			// Shell build step
sh """ 
newman run --disable-unicode \
src/test/InfoClient.postman_collection.json \
-e src/test/PROD.postman_environment.json \
-r htmlextra \
--reporter-htmlextra-export newman/index.html 
 """ 
	} // end of build stage 
	
	stage ('publish reports') {
	agent { node { label 'dd' }} 
	    publishHTML (target: [
		  allowMissing: false,
		  alwaysLinkToLastBuild: false,
		  keepAll: true,
		  reportDir: 'newman',
		  reportFiles: 'index.html',
		  reportName: "Newman Test Status"
		])
	} // end of publshing stage 
} // end of node block 
}