#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <xc.h>

#include "TMR1_Driver.h"

/*******************************************************************************
 *          DEFINES
 ******************************************************************************/


/*******************************************************************************
 *          MACRO FUNCTIONS
 ******************************************************************************/


/*******************************************************************************
 *          VARIABLES
 ******************************************************************************/


/*******************************************************************************
 *          BASIC FUNCTIONS
 ******************************************************************************/


/*******************************************************************************
 *          DRIVER FUNCTIONS
 ******************************************************************************/
void D_TMR1_Init() {
    // Disable timer
    D_TMR1_Enable(false);
    
    // T0CON
    T1CONbits.RD16 = 1;      // Enables register read/write of Timer1 in two 8-bit operations
    T1CONbits.T1RUN = 0;     // Device clock is derived from another source
    T1CONbits.T1CKPS = 0b00; // 1:8 pre-scale value
    T1CONbits.T1OSCEN = 0;   // Timer1 oscillator is shut off
    T1CONbits.TMR1CS = 0;    // Internal clock (FOSC/4)
    
    // Interrupts
    RCONbits.IPEN = 1;       // Enable priority levels on interrupts
    INTCONbits.GIEH = 1;     // Enable high interrupt
    INTCONbits.GIEL = 1;     // Enable low interrupt
    
    PIR1bits.TMR1IF = 0;     // Clear flag
    IPR1bits.TMR1IP = 0;     // Priority is low
    PIE1bits.TMR1IE = 1;     // Enable 
}

void D_TMR1_Enable(bool enable) {
    if (enable) {
        T1CONbits.TMR1ON = 1;
    } else {
        T1CONbits.TMR1ON = 0;
    }
}
