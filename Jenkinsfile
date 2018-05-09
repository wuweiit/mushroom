pipeline {
    agent any

   stages {
           stage('Build') {
               steps {
                   echo "env: ${env.BRANCH_NAME}"
                   echo "Building.."
                   sh "/opt/maven/bin/mvn clean package -Dmaven.test.skip=true"
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
                   sh "docker stop mrcms-${env.BRANCH_NAME}"


                   echo "douruimi-web bakup...."
                   sh ''' if [ ! -f "/opt/data/tomcat/mrcms/$BRANCH_NAME.war" ];then
                            echo "文件不存在"
                       else
                           mv /opt/data/tomcat/mrcms/$BRANCH_NAME.war  /opt/data/tomcat/app-$(date +%Y%m%d%h%m%s).war
                       fi
                   '''

                   echo "mrcms-${env.BRANCH_NAME} docker deploying...."
                   sh 'cp ./target/mrcms-1.0.0.war /opt/data/tomcat/mrcms/$BRANCH_NAME.war'

                   echo "mrcms-${env.BRANCH_NAME}  docker start...."
                   sh "docker start mrcms-${env.BRANCH_NAME}"
               }
           }
       }
}