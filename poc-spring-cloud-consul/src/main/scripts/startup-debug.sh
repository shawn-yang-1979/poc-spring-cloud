cd `dirname $0`;
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8000 -jar ${project.build.finalName}-exec.jar