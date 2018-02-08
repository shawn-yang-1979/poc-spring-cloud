cd ..
call mvn clean release:clean release:prepare release:perform
cd %~dp0