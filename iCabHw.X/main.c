#include <xc.h>
#include <stdbool.h>
#include <stdint.h>
#include <string.h>

#include "Drivers/PORT_Driver.h"
#include "Drivers/UART_Driver.h"
#include "Controllers/DOOR_Controller.h"

#define _XTAL_FREQ 16000000

READ_Data read;

void main(void) {
    // Initialize ports to defaults
    D_PORT_Init();
    
    // Initialze doors
    C_DOOR_Init();
    C_DOOR_LockAll();
    
    // Initialize the UART module with a baud rate of 9600, with the use 
    // of interrupts.
    D_UART_Init("P", 9600, true);
    D_UART_Enable(true);
    
    __delay_ms(200);
    D_UART_Write("I", "Start");
    while(1) {
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
    }
}
