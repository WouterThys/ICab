#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <xc.h>

#include "TMR0_Driver.h"

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
void D_TMR0_Init() {
    // Disable timer
    D_TMR0_Enable(false);
    
    // T0CON
    T0CONbits.T08BIT = 0;   // Timer0 is configured as a 16-bit timer/counter 
    T0CONbits.T0CS = 0;     // Internal instruction cycle clock (CLKO) 
    T0CONbits.T0SE = 0;     // Increment on low-to-high transition on T0CKI pin 
    T0CONbits.PSA = 0;      // Timer0 pre-scaler is assigned. Timer0 clock input comes from pre-scaler output.
    T0CONbits.T0PS = 2;     // 1:8 pre-scale value
    
    
    // Interrupts
    RCONbits.IPEN = 1;      // Enable priority levels on interrupts
    INTCONbits.GIEH = 1;    // Enable high interrupt
    INTCONbits.GIEL = 1;    // Enable low interrupt
    
    INTCONbits.TMR0IF = 0;  // Clear flag
    INTCON2bits.TMR0IP = 0; // Priority is low
    INTCONbits.TMR0IE = 1;  // Enable 
}

void D_TMR0_Enable(bool enable) {
    if (enable) {
        T0CONbits.TMR0ON = 1;
    } else {
        T0CONbits.TMR0ON = 0;
    }
}
