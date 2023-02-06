pipeline {
    agent any
    options {
        skipStagesAfterUnstable()
    }
    stages {

         stage('Build') {
          when {
                branch '*'
            }
            steps {
                sh 'java -version'
                sh 'npm run build:dev'
                sh 'make' 
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true 
            }
        }
        stage('Test'){
        when {
                branch 'master'
            }
            steps {
                sh 'make check || true'
                junit 'reports/**/*.xml'
            }
    }
         stage('Test-veracode'){
        when {
                branch 'Master'
                branch 'Develop'
                branch 'Release'
            }
            steps {
               sh 'make check'
                junit 'reports/**/*.xml'
            }
     }
         stage('Test-publicar'){
         steps {
             sh 'make check'
             junit 'reports/**/*.xml'
             }
         }
         stage('public-toDockerhub') {
         when {
             branch 'Develop'
             }
             steps {
         withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: jenkins_registry_cred_id, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
             sh "docker login -e ${docker_email} -u ${env.USERNAME} -p ${env.PASSWORD} ${docker_registry_url}"
             }
             }
         }
     stage('Deploy-qa') {
         when {
             branch 'Master'
             }
             steps {
             sh 'make publish'
             
         }
     }
     }
}