# Overview
A black and white smartwatch. This application has been designed for a 2BPP 240x240 screen. It has been tested on a STM32-F412-DISCO board.

![BlackClock](screenshots/BlackClock.png) ![Weather](screenshots/Weather.png) ![DigitalClock](screenshots/DigitalClock.png) ![Distance](screenshots/Distance.png) ![Battery](screenshots/Battery.png) ![Notifications](screenshots/Notifications.png)

## Usage
## Run on MicroEJ Simulator
1. Right Click on the project
2. Select **Run as -> MicroEJ Application**
3. Select your platform 
4. Press **Ok**


## Run on device
### Build
1. Right Click on [SmartWatchApp.java](src/main/java/com/microej/demo/smartwatch/SmartWatchApp.java)
2. Select **Run as -> Run Configuration**
3. Select **MicroEJ Application** configuration kind
4. Click on **New launch configuration** icon
5. In **Execution** tab
	1. In **Target** frame, in **Platform** field, select a relevant platform (but not a virtual device)
	2. In **Execution** frame
		1. Select **Execute on Device**
		2. In **Settings** field, select **Build & Deploy**
6. Press **Apply**
7. Press **Run**
8. Copy the generated `.out` file path shown by the console

### Flash
1. Use the appropriate flashing tool.


# Requirements
* MicroEJ Studio or SDK 4.0 or later
* A platform with at least:
	* EDC-1.2 or higher
	* MICROUI-2.0 or higher
	* MWT-2.0 or higher

## Dependencies
_All dependencies are retrieved transitively by Ivy resolver_.

# Source
N/A

## Restrictions
None.