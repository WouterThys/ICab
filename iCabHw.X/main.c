#include <xc.h>
#include <stdbool.h>
#include <stdint.h>
#include <string.h>

#include "Drivers/PORT_Driver.h"
#include "Drivers/UART_Driver.h"
#include "Drivers/TMR0_Driver.h"
#include "Controllers/DOOR_Controller.h"

#define _XTAL_FREQ 16000000

#define COMMAND_LOCK    "L" /* Command to lock doors                          */
#define COMMAND_UNLOCK  "U" /* Command to unlock doors                        */
#define COMMAND_INIT    "I" /* Command send by application when initializing  */
#define COMMAND_RESET   "R" /* Command to reset PIC                           */
#define COMMAND_ERROR   "E" /* Command to indicate error                      */

#define ERROR_UNKNOWN   "U" /* Error message to send when unknown command     */

static READ_Data read;
static bool tick;

void main(void) {
    __delay_ms(200);
    // Initialize ports to defaults
    D_PORT_Init();
    
    // Initialize the UART module with a baud rate of 9600, with the use 
    // of interrupts.
    D_UART_Init("P", 9600, true);
    D_UART_Enable(true);
    
    // Initialize timer
    D_TMR0_Init();
    
    // Initialize doors
    C_DOOR_Init();
    C_DOOR_UnlockAll();
    
    __delay_ms(200);
    D_UART_Write(COMMAND_INIT, "I");
    
    // Start
    D_TMR0_Enable(true);
    
    while(1) {
        // Serial
        if (readReady) {
            readReady = false;
            read = D_UART_Read();
            if (strcmp(read.command, COMMAND_LOCK) == 0) {
                C_DOOR_LockAll();
            } else if (strcmp(read.command, COMMAND_UNLOCK) == 0) {
                C_DOOR_UnlockAll();
            } else if (strcmp(read.command, COMMAND_INIT) == 0) {
                // Do nothing, acknowledge will answer
            } else if (strcmp(read.command, COMMAND_RESET) == 0) {
                D_TMR0_Enable(false);
                D_UART_Enable(false);
                __delay_ms(20);
                Reset();
            } else {
                D_UART_Write(COMMAND_ERROR, ERROR_UNKNOWN);
            }
        }
        
        // FSM
        if (tick) {
            tick = false;
            
            // Read inputs
            C_DOOR_ReadSensors();
            
            // Send states
            C_DOOR_SendStates();
        }
    
    }
}

void interrupt HighISR(void) {
    if (INTCONbits.TMR0IF) {
        tick = true;
        INTCONbits.TMR0IF = 0;
    }
}



//        if(readReady) {
//            readReady = false;
//            read = D_UART_Read();
//            if (strcmp(read.command, "led") == 0) {
//                if (strcmp(read.message, "on") == 0) {
//                    PORTAbits.RA0 = 1;
//                    D_UART_Write("Led", "I put it on");
//                }
//                if (strcmp(read.message,  "off") == 0) {
//                    PORTAbits.RA0 = 0;
//                    D_UART_Write("Led", "Putting led off sir");
//                }
//            }
//        }
