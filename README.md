# TaskCube Hackaton Project
This project is about using our time efficiently. \
We have a 6 face cube, each face in the cube represents a task we need to
 do, be it homework in various subjects, some time for social media or a
  break. \
The TaskCube lights corresponds to the upwards face and allows us to know
 which task we are currently doing, 
 for example if we assigned the blue color to homework and the cube is lit
 with blue color it means we are supposed to work on out homework, while
  the TaskCube is blue the amount of time given to task is being used. when
   the time is up the TaskCube will turn red and will beep, forcing us to
    switch task. \
    The TaskCube is accompanied by an Android App that allows us to set the
     name and time of the tasks with a corresponding color. 

## Video
https://www.youtube.com/watch?v=dJXPWvCA7zg

## Usage
Using the App we set the tasks and time for each task with a corresponding
 color. \
 The BlueTooth_AT_MODE.ino file is used to define the name of the Bluetooth module
 Simply use the command AT+NAME=TaskCube

### Setup
1. Print the 3D Cube, using the provided model JK.stl and XY type of 3D
 printer
 with WZ type of matter
2. Make sure you have all the required components, BOM file. \
Attach Arduino to the base of the TaskCube and assemble as instructed in the hackathon_bb.PNG 
3. Download Arduino code and upload.
4. Download the Android application project and upload to an Android phone
 (Min API level of 28).
