#!groovy

String GIT_VERSION = "1.57.60-SNAPSHOT-morpheus"

//noinspection GroovyAssignabilityCheck
node {

  def buildEnv
  def devAddress

  stage ('Checkout') {
    deleteDir()
    checkout scm
  }

  stage ('Deploy to DEV') {
    devAddress = deployContainer("docker-repo.repo.lottoland.io/dreamit/lotto-web:${GIT_VERSION}", 'dev')
  }

  stage ('Verify Deployment') {
    buildEnv.inside {
      sh "sample-client/target/universal/stage/bin/demo-client ${devAddress}"
    }
  }
}

stage 'Deploy to LIVE'
timeout(time:2, unit:'DAYS') {
  input message:'Approve deployment to LIVE?'
}
node {
  deployContainer("sambott/grpc-test:${GIT_VERSION}", 'LIVE')
}

def deployContainer(image, env) {
  docker.image('lachlanevenson/k8s-kubectl:v1.5.2').inside {
    withCredentials([[$class: "FileBinding", credentialsId: 'KubeConfig', variable: 'KUBE_CONFIG']]) {
      def kubectl = "kubectl  --kubeconfig=\$KUBE_CONFIG --context=${env}"
      sh "${kubectl} set image deployment/grpc-demo grpc-demo=${image}"
      sh "${kubectl} rollout status deployment/grpc-demo"
      return sh (
              script: "${kubectl} get service/grpc-demo -o jsonpath='{.status.loadBalancer.ingress[0].hostname}'",
              returnStdout: true
      ).trim()
    }
  }
}