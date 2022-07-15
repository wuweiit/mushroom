pipeline {
    agent any

   stages {
           stage('Build') {
               steps {
                   echo "env: ${env.BRANCH_NAME}"
                   echo "Building.."
                   sh "/opt/maven/bin/mvn package -Dmaven.test.skip=true -P${env.BRANCH_NAME}"
               }
           }
           stage('Test') {
               steps {
                   echo "env: ${env.BRANCH_NAME}"
                   echo 'Testing..'
               }
           }
           stage('Deploy') {
               steps {
                   echo "env: ${env.BRANCH_NAME}"
                   echo "mrcms-${env.BRANCH_NAME} docker stop...."


                   sh '''
                       ssh -o StrictHostKeyChecking=no root@192.168.1.6 "docker stop mrcms-${BRANCH_NAME}"

                       echo "mrcms-${BRANCH_NAME}.war deploying...."
                       scp ./target/mrcms-1.0.0.war root@192.168.1.6:/opt/data/tomcat/mkewl/tomcat/data/ROOT.war
                       echo "mrcms-${BRANCH_NAME} docker deploying...."


                       ssh -o StrictHostKeyChecking=no root@192.168.1.6 "kubectl rollout restart deployment mkewl --namespace home"

                   '''
               }
           }
       }
}