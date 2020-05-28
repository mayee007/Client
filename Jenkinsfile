// Powered by Infostretch 

timestamps {

node ('master') { 

	stage ('Names/Client_Postman - Checkout') {
 	 checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'yelekeri@gmail.com', url: 'https://github.com/mayee007/Client.git']]]) 
	}
	stage ('Names/Client_Postman - Build') {
 			// Shell build step
sh """ 
newman run --disable-unicode \
src/test/InfoClient.postman_collection.json \
-e src/test/PROD.postman_environment.json \
-r htmlextra \
--reporter-htmlextra-export newman/index.html 
 """ 
	}
}
}