# Personal Macro Tool
## Description
The Personal Macro Tool is a versatile application designed to empower users by allowing the creation of custom capabilities, focusing primarily on macros. It's built to accommodate a wide range of tasks from simple keyboard shortcuts to complex automation scripts, making repetitive tasks more efficient and user-friendly.

## Features

### Macros
- Custom Macros: Creating custom macros for any series of actions or commands.
- Global Key Listeners: Utilise's JNativeHook in Scala to listen for global macro triggers.
- Complex Workflows: Merge various macros to create complex workflows that can be initiated with simple macro triggers.

## System Integration
- Lockfile Mechanism: Implemented a lockfile system to prevent multiple instances of the same macro from running concurrently, ensuring system stability and predictability.
- Task Management: Interact directly with Windows Task Manager, using PIDs to manage and reset applications as needed, increasing efficiency.

## Utilization of Technologies
- JNativeHook: Leverages the power of JNativeHook in Scala for global key listening and system-wide macro activation.
- Scala: Built with Scala, ensuring a robust, efficient, and scalable application capable of handling complex tasks.
