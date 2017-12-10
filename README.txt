Basic Eclipse configuration in order to run the project:
	Lombok:
	1 - Download lombok.jar from https://projectlombok.org/download
	2 - Copy the jar into the eclipse folder
	3 - Open eclipe.ini and add after -vmargs the following 2 lines:
		-Xbootclasspath/a:lombok.jar
		-javaagent:lombok.jar
	4 - Restart Eclipse
	5 - Clean project
	