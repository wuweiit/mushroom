pipeline {
    agent any

   stages {
           stage('Build') {
               steps {
                   echo "env: ${env.BRANCH_NAME}"
                   echo "Building.."
                   sh "/opt/maven/bin/mvn clean package -Dmaven.test.skip=true -P${env.BRANCH_NAME}"
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


                       ssh root@192.168.1.6 "docker stop mrcms-${BRANCH_NAME}"


                       echo "douruimi-web bakup...."
                       if [ ! -f "/opt/data/tomcat/mrcms/$BRANCH_NAME.war" ];then
                            echo "文件不存在"
                       else
                           mv /opt/data/tomcat/mrcms/$BRANCH_NAME.war  /opt/data/tomcat/app-$(date +%Y%m%d%h%m%s).war
                       fi

                       scp ./target/mrcms-1.0.0.war root@192.168.1.6:/opt/data/tomcat/mrcms/tomcat/data/ROOT.war


                       echo "mrcms-${BRANCH_NAME} docker deploying...."


                       echo "mrcms-${BRANCH_NAME}  docker start...."


                       ssh root@192.168.1.6 "docker start mrcms-${BRANCH_NAME}"

                   '''
               }
           }
       }
}