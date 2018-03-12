#include <xc.h>
#include <stdbool.h>
#include <stdint.h>
#include <string.h>

#include "Drivers/PORT_Driver.h"
#include "Drivers/UART_Driver.h"
#include "Drivers/TMR0_Driver.h"
#include "Controllers/DOOR_Controller.h"

#define _XTAL_FREQ 16000000 /* 16 MHz crystal                                 */

#define COMMAND_LOCK    "L" /* Command to lock doors                          */
#define COMMAND_UNLOCK  "U" /* Command to unlock doors                        */
#define COMMAND_INIT    "I" /* Command to initialize                          */
#define COMMAND_RESET   "R" /* Command to reset PIC                           */
#define COMMAND_PING    "P" /* Command to ping PIC                            */
#define COMMAND_ERROR   "E" /* Command to indicate error                      */

#define ERROR_UNKNOWN   "U" /* Error message to send when unknown command     */

static READ_Data read;
static bool tick;

static void initDoors(uint8_t door_cnt);

static void initDoors(uint8_t door_cnt) {
    if (door_cnt < 1) {
        door_cnt = 1;
    }
    // Doors
    C_DOOR_Init(door_cnt);
    C_DOOR_LockAll();
    
    // Start timer
    D_TMR0_Enable(true);
}

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
    
    __delay_ms(200);
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
                initDoors((uint8_t)(*read.message - 0x30));
            } else if (strcmp(read.command, COMMAND_RESET) == 0) {
                __delay_ms(20);
                Reset();
            } else if (strcmp(read.command, COMMAND_PING) == 0) {
                // Do nothing, acknowledge message will be send automatically
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
