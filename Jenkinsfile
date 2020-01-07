//pipeline {
//  agent {
//    docker {
//      args '-v /work/apps/maven/:/root/.m2 -v /work/website:/work/website'
//      image 'maven:3.6.3'
//    }
//
//  }
//  options { disableConcurrentBuilds() }
//  triggers {
//    pollSCM('* * * * *')
//  }
//  parameters {
//    choice(name:'Project',choices:'kgms\nkgsdk\nkgpreview',description:'select project?')
//  }
//
//  stages {
//    stage('maven build') {
//      steps {
//        sh 'mvn clean package -DskipTests '
//      }
//    }
//
//    stage('copy') {
//      steps {
//        sh 'chmod 777 move.sh'
//        sh "./move.sh ${params.Project}"
//      }
//    }
//
//    stage('start') {
//      steps {
//        sh 'chmod 777 start.sh'
//        sh "./start.sh ${params.Project}"
//      }
//    }
//
//  }
//  post {
//    always {
//      echo 'clean up our workspace'
//      deleteDir()
//    }
//
//    success {
//      echo 'I succeeeded!'
//    }
//
//    unstable {
//      echo 'I am unstable :/'
//    }
//
//    failure {
//      echo 'I failed :('
//    }
//
//    changed {
//      echo 'Things were different before...'
//    }
//
//  }
//}