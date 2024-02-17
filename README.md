# FileLinesSorter
Simple console application to sort large text files (more than 1 GB).
## Usage
Run the Main class to start the application.
From cmd:
```
java Main input.txt output.txt txt true
```
Where:
- input.txt - the name of the input file
- output.txt - the name of the file in which the sorted result will be written
- txt - output format ("json", "xml" or "txt")
- false - sorting direction (true for ascending, false for descending)
