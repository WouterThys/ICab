/*
 * File:   main.c
 * Author: wouter
 *
 * Created on October 22, 2016, 5:17 PM
 */

#include <xc.h>
#include <stdbool.h>
#include <stdint.h>
#include <string.h>

#include "Drivers/PORT_Driver.h"
#include "Drivers/UART_Driver.h"

#define _XTAL_FREQ 16000000

READ_Data read;

/**
 * INFO: test
 * ----------
 * 
 * - Write "&[M]:C:led:on:2$" to put a 'led' 'on'
 * - Write "&[M]:C:led:off:3$" to pul a 'led' 'off'
 * 
 */

void main(void) {
    
    D_PORT_Init();
    // Initialize the UART module with a baud rate of 9600, with the use 
    // of interrupts.
    D_UART_Init("P", 9600, true);
    D_UART_Enable(true);
    
    D_UART_Write("I", "In");
    while(1) {
        if(readReady) {
            readReady = false;
            read = D_UART_Read();
//            D_UART_Write("com", read.command);
//            D_UART_Write("mes", read.message);
            if (strcmp(read.command, "led") == 0) {
                if (strcmp(read.message, "on") == 0) {
                    PORTAbits.RA0 = 1;
                    D_UART_Write("Led", "I put it on");
                }
                if (strcmp(read.message,  "off") == 0) {
                    PORTAbits.RA0 = 0;
                    D_UART_Write("Led", "Putting led off sir");
                }
            }
        }    
    }
    return;
}
