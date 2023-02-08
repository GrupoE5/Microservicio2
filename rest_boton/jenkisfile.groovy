pipeline {
  agent any
  tools {
      gradle  'Gradle 7.5.1'
      //dockerTool 'Docker 20.10.21'
    }
  stages {
    stage('Build') {
      steps {
        script{
          sh """
          ls -la
          cd micro_imagen
          ls -la
          pwd
          gradle build
          """
          }
      }
    }
    stage('test') {
      steps {
        //sh 'gradle docker'
        echo 'test succesfull'
      }
    }
    stage('Build Docker Image') {
      steps {
        sh"""
        cd micro_imagen
        docker login -u $DOCKER_USER -p $DOCKER_PASSWORD
        docker build -t testjenkinsdocker:1.0 .
        docker push $DOCKER_USER/testjenkinsdocker:1.0
        """
      }
    }
    stage('Push Docker Image') {
      steps {
        echo 'succesfull'
      }
    }
  }
}
