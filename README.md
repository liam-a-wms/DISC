# DISC (Dynamic Instruction-Scenario Components)

This API is designed for easy, dynamic handling of instruction sets for autonomous operation of robots in Java. It does not specify commands, but supplies advanced data structures for storing and using them.

A scenario file (used to build a Scenario structure) begins with a starting block, which contains the name and arguments of the file. The name of the arguments other than name does not matter. Below is an example:<br><br>
##name=Example Scenario<br>
##arg0=LLL<br>
##arg1=1<br>
##arg2=bohemia<br>

Args are stored as Strings, so it does not matter what you put in them. Anything else in the file will be treated as either a comment or an Instruction. Comments begin with # and an Instruction is a comma-separated value line, such as:<br><br>
#This is a comment<br>
start                           <- Delimiter, the text of the delimiter is stored in args[0], anything else is also in args<br>
nav.goto, waypointName          <- Command, the first item is stored in target, anything after the fullstop in args<br>
control.set, manual             <- Control State, the key word is "control", anything after the fullstop is stored in args<br>
stop, 1                         <- Also a Delimiter, the key words for such are "start" and "stop"<br>
#This is also a comment<br>

When writing scenarios, please ensure to use Notepad++ so random unnecessary characters don't show up (like \par at the end of each line if you use WordPad), generally they should be saved as .scenario files, but the API does not check.

The ScenarioCompressor is used to chain Scenarios together into one file, preferably stored as .scenariox for differentiation. It also can read a .scenariox file that it wrote and give an array or queue of Scenarios. If you are writing your own .scenariox for reading by the ScenarioCompressor, keep in mind that it uses #! as a separating sequence.
