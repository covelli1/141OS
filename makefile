build: 
	@echo ----- creating a jar file with java -----
	javac src/*java
	jar cfm 141OS.jar src/MANIFEST.MF src/*.class

clean:
	rm -f 141OS.jar
	rm -f src/*.class

