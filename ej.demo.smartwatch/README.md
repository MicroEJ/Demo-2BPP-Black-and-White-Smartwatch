<!--
	Markdown
	Copyright 2014-2016 IS2T. All rights reserved.
	IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->
# Overview
A black and white smartwatch. This application has been designed for a 2BPP 240x240 screen. It has been tested on a STM32-F412-DISCO board.

## Usage
## Run on MicroEJ Simulator
1. Right Click on the project
1. Select **Run as -> MicroEJ Application**
1. Select your platform 
1. Press **Ok**


## Run on device
### Build
1. Right Click on [SmartWatchApp.java](com.ausy.microej.smartwatch/src/main/java/ej/demo/smartwatch/SmartWatchApp.java)
1. Select **Run as -> Run Configuration**
1. Select **MicroEJ Application** configuration kind
1. Click on **New launch configuration** icon
1. In **Execution** tab
	1. In **Target** frame, in **Platform** field, select a relevant platform (but not a virtual device)
	1. In **Execution** frame
		1. Select **Execute on Device**
		2. In **Settings** field, select **Build & Deploy**
1. Press **Apply**
1. Press **Run**
1. Copy the generated `.out` file path shown by the console

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