# FileLinesSorter
Simple console application to sort large text files (more than 1 GB).
## Usage
Run the Main class to start the application.

From cmd:
```
javac -d target\classes -sourcepath src\main\java src\main\java\org\example\Main.java
java -classpath target\classes org.example.Main "inputFileName" "outputFileName" txt true
```
Where:
- input - the absolute path of the input file (quotation marks are required)
- output - the absolute path of the file in which the sorted result will be written (quotation marks are required)
- txt - output format ("json", "xml" or "txt")
- true - sorting direction (true for ascending, false for descending)
