@ECHO OFF
ECHO Setting JasperReportsChecker project version to: %1

CALL mvn versions:set "-DnewVersion=%1"
CALL mvn versions:set "-DnewVersion=%1"

CALL mvn clean install -Dmaven.test.skip=true