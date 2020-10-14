# ImdbClientApi

	Is an application executed from a .jar file whose objective is to listen on the TCP port through a socket 
	and consult on the IMDB titles of films and series according to the search performed by the client.

# Technology
	Java 8
	Socket TCP
	Threads
	Log4J
	JUnit
	Gradle
	
# To Build
	In project folder execute gradlew clean fatjar
	Executable jar will be generated in <project_folder>\build\libs
	
# To Run
	In <project folder>\build\libs execute java -jar ImdbClientApi.jar
	Command prompt will show something like 'listening at port 5050'
	
# To Use
	Send the command via client socket. Ex.: <query length>:<query>
	The return could be:
		- In case of success <payload length>:<payload>
		- No results: when there is no result in the query
		- Invalid mensage recived: performed when the protocol is broken
	
	
